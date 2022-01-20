package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMediaCategoriaEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSPagtoFornecedorTipoEnum;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoPesquisa;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.GetProductsByDateRQ;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import br.com.infotera.it.novaxs.model.Product;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author Lucas
 **/

@Service
public class DisponibilidadeWS {

    @Autowired
    private NovaxsClient novaxsClient;


    public WSDisponibilidadeIngressoRS disponibilidade(WSDisponibilidadeIngressoRQ dispRQ) throws ErrorException {
        WSDisponibilidadeIngressoRS result = null;
        WSIntegrador integrador = dispRQ.getIntegrador();
        try {
            List<WSIngressoPesquisa> ingressoPesquisaList = pesquisarIngresso(dispRQ);
            result = new WSDisponibilidadeIngressoRS(ingressoPesquisaList, integrador, WSIntegracaoStatusEnum.OK);
        } catch (ErrorException ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            if (ex.getIntegrador() == null) {
                ex.setIntegrador(integrador);
            }
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "disponibilidade", WSMensagemErroEnum.SDI, "Erro ao pesquisar atividades", WSIntegracaoStatusEnum.NEGADO, ex);
        }
        return result;
    }

    private List<WSIngressoPesquisa> pesquisarIngresso(WSDisponibilidadeIngressoRQ dispRQ) throws ErrorException {
        List<WSIngressoPesquisa> result = null;
        List<GetProductsByDateRS> getProductsByDateRSList = novaxsClient.getProductsByDateRQ(dispRQ.getIntegrador(),
                montaRequestGetProductsByDateRQ(dispRQ));
        if (getProductsByDateRSList != null) {
            try {
                if (!getProductsByDateRSList.isEmpty()) {
                    result = montaPesquisarIngressoResult(dispRQ, getProductsByDateRSList);
                }
            } catch (NullPointerException ex) {
                throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, true);
            } catch (ErrorException ex) {
                throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
            }
        } else {
            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, "Nenhuma atividade retornada", WSIntegracaoStatusEnum.NEGADO, null);
        }
        return result;
    }

    private List<WSIngressoPesquisa> montaPesquisarIngressoResult(WSDisponibilidadeIngressoRQ dispRQ, List<GetProductsByDateRS> getProductsByDateRSList) throws ErrorException {
        List<WSIngressoPesquisa> result = new ArrayList<>();
        int sqPesquisa = 0;
        for (GetProductsByDateRS productsByDateRS : getProductsByDateRSList) {
            sqPesquisa++;
            WSIngressoPesquisa ingressoPesquisa = montaIngressoPesquisa(sqPesquisa, dispRQ, productsByDateRS);
            result.add(ingressoPesquisa);
        }

        return result;
    }

    private WSIngressoPesquisa montaIngressoPesquisa(int sqPesquisa, WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {
        WSIngressoPesquisa result = new WSIngressoPesquisa(sqPesquisa,
                montaIngresso(dispRQ, productsByDateRS),
                montaIngressoModalidadeList(dispRQ, productsByDateRS));

        return result;
    }

    private List<WSIngressoModalidade> montaIngressoModalidadeList(WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {
        List<WSIngressoModalidade> ingressoModalidadeList = null;

        try {
            if (productsByDateRS.getProducts() != null) {
                ingressoModalidadeList = montaIngressoModalidade(montaWSTarifa(dispRQ, productsByDateRS), productsByDateRS.getProducts());
            }
        } catch (NullPointerException ex){
            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaIngressoModalidadeList", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade" + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        } catch (ErrorException ex) {
            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaIngressoModalidadeList", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }


        return ingressoModalidadeList;
    }

    private WSTarifa montaWSTarifa(WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {
        WSTarifa tarifa = null;
        double vlNeto = montaVlNeto(productsByDateRS);
        Integer qtPax = dispRQ.getReservaNomeList().size();
        try {
            tarifa = new WSTarifa(productsByDateRS.getCurrency(),
                    vlNeto,
                    vlNeto / (qtPax.doubleValue()),
                    null,
                    null,
                    WSPagtoFornecedorTipoEnum.FATURADO,
                    UtilsWS.montaPoliticaList(productsByDateRS));

//            tarifa.setTarifaNomeList(UtilsWS.montaTarifaNome(dispRQ.getIntegrador(), m.getAmountsFrom(), ac.getCurrency()));

            return tarifa;

        } catch (Exception ex) {
            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade", WSIntegracaoStatusEnum.NEGADO, ex);
        }
    }

    private double montaVlNeto(GetProductsByDateRS productsByDateRS) throws ErrorException {
        String strValue = Optional.ofNullable(productsByDateRS.getValue()).orElseThrow(() ->
                new ErrorException("Erro ao calcular valores por pax"));
        double vlNeto = Double.parseDouble(strValue) / 100;
        return vlNeto;
    }

    private List<WSIngressoModalidade> montaIngressoModalidade(WSTarifa tarifa, List<Product> productList) {
        List<WSIngressoModalidade> wsIngressoModalidade = new ArrayList<>();
        if (!Utils.isListNothing(productList)) {
            for (Product product : productList) {
                wsIngressoModalidade.add(montaIngressoModalidade(tarifa, product));
            }
        }
        return wsIngressoModalidade;
    }

    private WSIngressoModalidade montaIngressoModalidade(WSTarifa tarifa, Product product) {
        return new WSIngressoModalidade(product.getId(),
                product.getName(),
                tarifa);
    }

    private WSIngresso montaIngresso(WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) {
        String dsParamTarifar = dispRQ.getCdDestino() + "|#|" + Utils.formatData(dispRQ.getDtInicio(), "yyyy-MM-dd") + "|#|" + Utils.formatData(dispRQ.getDtFim(), "yyyy-MM-dd");
        List<WSMedia> mediaList = null;
        if (!productsByDateRS.getType().equals("Combo")) {
            mediaList = montaMediaList(productsByDateRS);
        } else {
            mediaList = montaMediaList(productsByDateRS.getProducts());
        }
        WSIngresso wsIngresso = new WSIngresso(productsByDateRS.getPath(),
                productsByDateRS.getName(),
                UtilsWS.variavelTemporaria,
                null,
                null,
                null,
                dispRQ.getReservaNomeList(),
                null,
                mediaList,
                dsParamTarifar,
                null);

//        wsIngresso.setDsParametro(productsByDateRS.toString());

        return wsIngresso;
    }

    private List<WSMedia> montaMediaList(GetProductsByDateRS productsByDateRS) {
        List<WSMedia> result = new ArrayList<>();
        WSMedia media = new WSMedia(WSMediaCategoriaEnum.SERVICO, productsByDateRS.getImage());
        result.add(media);
        return result;
    }

    private List<WSMedia> montaMediaList(List<Product> products) {
        List<WSMedia> result = new ArrayList<>();
        WSMedia media = null;
        for (Product product : products) {
            media = new WSMedia(WSMediaCategoriaEnum.SERVICO, product.getImage());
            result.add(media);
        }
        return result;
    }

    public GetProductsByDateRQ montaRequestGetProductsByDateRQ(WSDisponibilidadeIngressoRQ dispRQ) throws ErrorException {
        Date dtInicio = Optional.ofNullable(dispRQ.getDtInicio())
                .orElseThrow(() -> new ErrorException("Data Inicio não informada"));
        try {
            return new GetProductsByDateRQ().setLogin(dispRQ.getIntegrador().getDsCredencialList().get(0))
                    .setPassword(dispRQ.getIntegrador().getDsCredencialList().get(1))
                    .setDate(Utils.formatData(dtInicio, "dd/MM/yyyy"))
                    .setToken(dispRQ.getIntegrador().getDsCredencialList().get(2));

        } catch (Exception ex) {
            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "disponibilidade", WSMensagemErroEnum.SDI, "Erro ao pesquisar atividades", WSIntegracaoStatusEnum.NEGADO, ex);
        }
    }


}

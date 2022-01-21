package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSPagtoFornecedorTipoEnum;
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

import static br.com.infotera.it.novaxs.utils.UtilsWS.montaIngresso;
import static br.com.infotera.it.novaxs.utils.UtilsWS.montaIngressoModalidadeList;

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
                montaIngresso(dispRQ.getIntegrador(), dispRQ.getReservaNomeList(),  productsByDateRS),
                montaIngressoModalidadeList(dispRQ.getIntegrador(), dispRQ.getReservaNomeList(), productsByDateRS));
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

        } catch (NullPointerException ex) {
            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "disponibilidade", WSMensagemErroEnum.SDI, "Erro ao pesquisar atividades", WSIntegracaoStatusEnum.NEGADO, ex);
        }
    }


}

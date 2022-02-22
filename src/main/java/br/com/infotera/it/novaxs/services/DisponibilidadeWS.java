package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.servico.WSIngressoPesquisa;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.GetProductsByDateRQ;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static br.com.infotera.it.novaxs.utils.UtilsWS.*;

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

    public List<WSIngressoPesquisa> pesquisarIngresso(WSDisponibilidadeIngressoRQ dispRQ) throws ErrorException {
        List<WSIngressoPesquisa> result = null;
        List<GetProductsByDateRS> getProductsByDateRSList = novaxsClient.getProductsByDateRQ(dispRQ.getIntegrador(),
                montaRequestGetProductsByDateRQ(dispRQ.getIntegrador(), dispRQ.getDtInicio()));

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
        WSIngressoPesquisa ingressoPesquisa;
        int sqPesquisa = 0;

        for (GetProductsByDateRS productsByDateRS : getProductsByDateRSList) {
            sqPesquisa++;
            ingressoPesquisa = montaIngressoPesquisa(sqPesquisa, dispRQ, productsByDateRS);
            result.add(ingressoPesquisa);
        }

        return result;
    }

    private WSIngressoPesquisa montaIngressoPesquisa(int sqPesquisa, WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {
        WSIngressoPesquisa result = new WSIngressoPesquisa(sqPesquisa,
                montaIngresso(dispRQ.getIntegrador(), dispRQ.getReservaNomeList(), productsByDateRS),
                montaIngressoModalidadeList(dispRQ.getIntegrador(), dispRQ, productsByDateRS));
        return result;
    }

    public GetProductsByDateRQ montaRequestGetProductsByDateRQ(WSIntegrador integrador, Date dtInicialIngresso) throws ErrorException {
        Date dtInicio = Optional.ofNullable(dtInicialIngresso)
                .orElseThrow(() -> new ErrorException("Data Inicio não informada"));
        try {
            return new GetProductsByDateRQ(UtilsWS.montaCredenciaisNovaXS(integrador))
                    .setDate(UtilsWS.montaDataNovaxs(dtInicio));

        } catch (NullPointerException ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "disponibilidade", WSMensagemErroEnum.SDI, "Erro ao pesquisar atividades", WSIntegracaoStatusEnum.NEGADO, ex);
        }
    }


}

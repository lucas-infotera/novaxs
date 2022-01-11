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


    public WSDisponibilidadeIngressoRS disponibilidade(WSDisponibilidadeIngressoRQ disponibilidadeIngressoRQ) throws ErrorException {
        WSDisponibilidadeIngressoRS result;
        WSIntegrador integrador = disponibilidadeIngressoRQ.getIntegrador();
        try {
            List<WSIngressoPesquisa> ingressoPesquisaList = pesquisarIngresso(disponibilidadeIngressoRQ);
            result = new WSDisponibilidadeIngressoRS(ingressoPesquisaList, integrador, WSIntegracaoStatusEnum.OK);
        } catch (ErrorException ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            ex.setIntegrador(integrador);
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "disponibilidade", WSMensagemErroEnum.SDI, "Erro ao pesquisar atividades", WSIntegracaoStatusEnum.NEGADO, ex);
        }
        return result;
    }

    private List<WSIngressoPesquisa> pesquisarIngresso(WSDisponibilidadeIngressoRQ disponibilidadeIngressoRQ) throws ErrorException {
        List<WSIngressoPesquisa> result = null;
        List<GetProductsByDateRS> getProductsByDateRS = novaxsClient.getProductsByDateRQ(disponibilidadeIngressoRQ.getIntegrador(),
                montaRequestGetProductsByDateRQ(disponibilidadeIngressoRQ));
        if (getProductsByDateRS != null) {
            if (!getProductsByDateRS.isEmpty()) {
                result = new ArrayList<>();
                result = montaPesquisarIngressoResult(disponibilidadeIngressoRQ, getProductsByDateRS);
            }
        }
        return result;
    }

    private List<WSIngressoPesquisa> montaPesquisarIngressoResult(WSDisponibilidadeIngressoRQ disponibilidadeIngressoRQ, List<GetProductsByDateRS> getProductsByDateRS) {
        List<WSIngressoPesquisa> result = new ArrayList<>();

        WSIngressoPesquisa ingressoPesquisa = montaIngressoPesquisa();


        return null;
    }

    private WSIngressoPesquisa montaIngressoPesquisa() {
        WSIngressoPesquisa result = new WSIngressoPesquisa();


        return result;
    }

    public GetProductsByDateRQ montaRequestGetProductsByDateRQ(WSDisponibilidadeIngressoRQ disponibilidadeIngressoRQ) throws ErrorException {
        Date dtFim = Optional.ofNullable(disponibilidadeIngressoRQ.getDtFim())
                .orElseThrow(() -> new ErrorException("Data fim não informada"));
        try {
            return new GetProductsByDateRQ().setLogin(disponibilidadeIngressoRQ.getIntegrador().getDsCredencialList().get(0))
                    .setPassword(disponibilidadeIngressoRQ.getIntegrador().getDsCredencialList().get(1))
                    .setDate(Utils.formatData(dtFim, "dd-MM-yyyy"))
                    //                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843");
                    .setToken(disponibilidadeIngressoRQ.getIntegrador().getDsCredencialList().get(2));

        } catch (Exception ex) {
            throw new ErrorException(disponibilidadeIngressoRQ.getIntegrador(), DisponibilidadeWS.class, "disponibilidade", WSMensagemErroEnum.SDI, "Erro ao pesquisar atividades", WSIntegracaoStatusEnum.NEGADO, ex);
        }
    }


}

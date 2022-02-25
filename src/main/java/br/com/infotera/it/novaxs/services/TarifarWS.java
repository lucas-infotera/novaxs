package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.GetProductsByDateRQ;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import br.com.infotera.it.novaxs.utils.Parametro;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @Author Lucas
 **/
@Service
public class TarifarWS {

    @Autowired
    private NovaxsClient novaxsClient;
    @Autowired
    private DisponibilidadeWS disponibilidadeWS;
    @Autowired
    private ObjectMapper objectMapper;

    public WSTarifarServicoRS tarifar(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        WSReservaServico reservaServico = null;
        try {
            reservaServico = montaWSReservaServico(tarifarServicoRQ,
                    montaProdutoTarifado(tarifarServicoRQ, montaProdutoReferencia(tarifarServicoRQ))
                            .orElseThrow(() -> new ErrorException("Ingresso não encontrado")));

        } catch (ErrorException ex) {
            tarifarServicoRQ.getIntegrador().setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            tarifarServicoRQ.getIntegrador().setDsMensagem(ex.getMessage());
            ex.setIntegrador(tarifarServicoRQ.getIntegrador());
            throw ex;
        } catch (NullPointerException ex) {
            throw new ErrorException(tarifarServicoRQ.getIntegrador(), TarifarWS.class, "tarifar", WSMensagemErroEnum.SPR, "Erro no processo de tarifar : NullpointerException " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        }

        return new WSTarifarServicoRS(reservaServico, tarifarServicoRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }

    private WSReservaServico montaWSReservaServico(WSTarifarServicoRQ tarifarServicoRQ, GetProductsByDateRS product) throws ErrorException {


        WSDisponibilidadeIngressoRQ dispRQ = new WSDisponibilidadeIngressoRQ();
        dispRQ.setIntegrador(tarifarServicoRQ.getIntegrador());
        dispRQ.setDtInicio(tarifarServicoRQ.getReservaServico().getServico().getDtServico());
        dispRQ.setDtFim(tarifarServicoRQ.getReservaServico().getServico().getDtServicoFim());
        dispRQ.setReservaNomeList(tarifarServicoRQ.getReservaServico().getServico().getReservaNomeList());

        WSIngresso servico = UtilsWS.montaIngresso(tarifarServicoRQ.getIntegrador(), dispRQ, product);
        servico.setDsParametro(tarifarServicoRQ.getReservaServico().getServico().getDsParametro());
        servico.setIngressoModalidadeList(UtilsWS.montaIngressoModalidadeList(null, dispRQ, product));

        WSReservaServico reservaServico = new WSReservaServico(tarifarServicoRQ.getIntegrador(),
                WSServicoTipoEnum.INGRESSO,
                servico,
                product.getPath(),
                null,
                tarifarServicoRQ.getReservaServico().getDsParametro());
        return reservaServico;
    }

    private Optional<GetProductsByDateRS> montaProdutoTarifado(WSTarifarServicoRQ tarifarServicoRQ, Parametro produtoReferencia) throws ErrorException {
        Optional<GetProductsByDateRS> product = null;

        if (tarifarServicoRQ.getReservaServico().getServico().getDtServico().toString().equals(produtoReferencia.getDt())) {
            Predicate<GetProductsByDateRS> isPathEquals = e -> e.getPath().equals(produtoReferencia.getCd());
            Predicate<GetProductsByDateRS> isScheduleTypeEquals = null;
            if (produtoReferencia.getHorario() != null) {
                isScheduleTypeEquals = e -> e.getSchedule_type().equals(produtoReferencia.getHorario());
                product = chamaWebServiceNovaxsGetProductsByDateRQ(tarifarServicoRQ).stream()
                        .filter(isPathEquals.and(isPathEquals).and(isScheduleTypeEquals))
                        .findFirst();
            }
            product = chamaWebServiceNovaxsGetProductsByDateRQ(tarifarServicoRQ).stream()
                    .filter(isPathEquals.and(isPathEquals))
                    .findFirst();
        }
        return product;
    }

    private Parametro montaProdutoReferencia(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        Parametro result;
        String parametro;
        try {
            parametro = (tarifarServicoRQ.getReservaServico().getDsParametro() != null ?
                    tarifarServicoRQ.getReservaServico().getDsParametro() :
                    tarifarServicoRQ.getReservaServico().getServico().getDsParametro());

            result = UtilsWS.converterDSParametro(parametro);

        } catch (NullPointerException ex) {
            throw ex;
        }
        return result;
    }

    public List<GetProductsByDateRS> chamaWebServiceNovaxsGetProductsByDateRQ(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        GetProductsByDateRQ montaGetProductsByDateRQ = disponibilidadeWS.montaRequestGetProductsByDateRQ(tarifarServicoRQ.getIntegrador(), tarifarServicoRQ.getReservaServico().getServico().getDtServico());
        return Optional.ofNullable(novaxsClient.
                        getProductsByDateRQ(tarifarServicoRQ.getIntegrador(),
                                montaGetProductsByDateRQ)).
                orElseThrow(() -> new ErrorException("Ingressos indisponiveis para a data selecionada no processo de tarifar"));
    }


}

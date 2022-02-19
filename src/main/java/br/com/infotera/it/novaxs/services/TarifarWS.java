package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.GetProductsByDateRQ;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        GetProductsByDateRS produtoReferencia;
        Optional<GetProductsByDateRS> product;

        WSReservaServico reservaServico = null;
        try {
            produtoReferencia = montaProdutoReferencia(tarifarServicoRQ);

            product = montaProdutoTarifado(tarifarServicoRQ, produtoReferencia);

            reservaServico = montaWSReservaServico(tarifarServicoRQ, product
                    .orElseThrow(() -> new ErrorException("Product não encontrado")));

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
        WSReservaServico reservaServico = new WSReservaServico(tarifarServicoRQ.getIntegrador(),
                WSServicoTipoEnum.INGRESSO,
                UtilsWS.montaIngresso(tarifarServicoRQ.getIntegrador(), tarifarServicoRQ.getReservaServico().getServico().getReservaNomeList(), product),
                null,
                null,
                tarifarServicoRQ.getReservaServico().getDsParametro());
        return reservaServico;
    }

    private Optional<GetProductsByDateRS> montaProdutoTarifado(WSTarifarServicoRQ tarifarServicoRQ, GetProductsByDateRS produtoReferencia) throws ErrorException {
        Optional<GetProductsByDateRS> product;
        Predicate<GetProductsByDateRS> isIdEquals = e -> e.getId().equals(produtoReferencia.getId());
        Predicate<GetProductsByDateRS> isScheduleTypeEquals = e -> e.getSchedule_type().equals(produtoReferencia.getSchedule_type());
        Predicate<GetProductsByDateRS> isTypeEquals = e -> e.getType().equals(produtoReferencia.getType());

        product = chamaWebServiceNovaxsGetProductsByDateRQ(tarifarServicoRQ).stream()
                .filter(isIdEquals.and(isIdEquals).and(isScheduleTypeEquals).and(isTypeEquals))
                .findFirst();
        return product;
    }

    private GetProductsByDateRS montaProdutoReferencia(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        GetProductsByDateRS produtoReferencia;
        String parametro;
        try {
            parametro = (tarifarServicoRQ.getReservaServico().getDsParametro() != null ?
                    tarifarServicoRQ.getReservaServico().getDsParametro() :
                    tarifarServicoRQ.getReservaServico().getServico().getDsParametro());

            produtoReferencia = UtilsWS.converterDSParametro(parametro);

        } catch (NullPointerException ex) {
            throw ex;
        }
        return produtoReferencia;
    }

    public List<GetProductsByDateRS> chamaWebServiceNovaxsGetProductsByDateRQ(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        GetProductsByDateRQ montaGetProductsByDateRQ = disponibilidadeWS.montaRequestGetProductsByDateRQ(tarifarServicoRQ.getIntegrador(), tarifarServicoRQ.getReservaServico().getServico().getDtServico());
        return Optional.ofNullable(novaxsClient.
                        getProductsByDateRQ(tarifarServicoRQ.getIntegrador(),
                                montaGetProductsByDateRQ)).
                orElseThrow(() -> new ErrorException("Ingressos indisponiveis para a data selecionada no processo de tarifar"));
    }


}

package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.GetProductsByDateRQ;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        WSIntegrador integrador = tarifarServicoRQ.getIntegrador();
        GetProductsByDateRS produtoReferencia;
        GetProductsByDateRS product;


        GetProductsByDateRS referenciaGetProductsByDateRS = convertDSParametroParaGetProductsByDate(tarifarServicoRQ);
        product = chamaWebServiceNovaxsGetProductsByDateRQ(tarifarServicoRQ).forEach(getProductsByDateRS -> {

        });



        WSReservaServico reservaServico = new WSReservaServico(integrador, );

        return new WSTarifarServicoRS(reservaServico, tarifarServicoRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }

    public List<GetProductsByDateRS> chamaWebServiceNovaxsGetProductsByDateRQ(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        GetProductsByDateRQ montaGetProductsByDateRQ = disponibilidadeWS.montaRequestGetProductsByDateRQ(tarifarServicoRQ.getIntegrador(), tarifarServicoRQ.getReservaServico().getServico().getDtServico());
        return Optional.ofNullable(novaxsClient.
                        getProductsByDateRQ(tarifarServicoRQ.getIntegrador(),
                                montaGetProductsByDateRQ)).
                orElseThrow(() -> new ErrorException("Ingressos indisponiveis para a data selecionada"));
    }

    private GetProductsByDateRS convertDSParametroParaGetProductsByDate(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        GetProductsByDateRS result;
        try {
            String parametro = (tarifarServicoRQ.getReservaServico().getDsParametro() != null ?
                    tarifarServicoRQ.getReservaServico().getDsParametro() :
                    tarifarServicoRQ.getReservaServico().getServico().getDsParametro());

            result = objectMapper.readValue(parametro, GetProductsByDateRS.class);
        } catch (JsonProcessingException ex) {
            throw new ErrorException("Erro a o converter dsParametro para GetProductsByDateRS");
        }
        return result;
    }
}

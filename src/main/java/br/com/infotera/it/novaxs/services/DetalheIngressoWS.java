package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.servico.WSDetalheIngresso;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRS;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import br.com.infotera.it.novaxs.model.Product;
import br.com.infotera.it.novaxs.utils.UtilsWS;
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
public class DetalheIngressoWS {

    @Autowired
    private  ObjectMapper mapper;

    public WSDetalheIngressoRS detalheIngresso(WSDetalheIngressoRQ wsRQ) throws ErrorException {
        WSDetalheIngressoRS result = null;
        WSIntegrador integrador = wsRQ.getIntegrador();

        if (wsRQ != null) {
            result = new WSDetalheIngressoRS();
            result.setIntegrador(integrador);
            WSIngresso ingresso = Optional.ofNullable(wsRQ.getIngressoDetalhe().getIngresso()).orElseThrow(() -> new ErrorException("Ingresso vazio em detalhe ingresso"));
            result.setDetalheIngresso(
                    montaDetalheIngresso(integrador, ingresso.getReservaNomeList(), Optional.ofNullable(ingresso.getDsParametro())
                    .orElseGet(() -> {
                        return null;
                    })));
        }
        return result;
    }

    private WSDetalheIngresso montaDetalheIngresso(WSIntegrador integrador, List<WSReservaNome> reservaNomeList,  String dsParametro) throws ErrorException {
        WSDetalheIngresso result = new WSDetalheIngresso();
        GetProductsByDateRS products;
        try {
            products = mapper.readValue(dsParametro, GetProductsByDateRS.class);
        } catch (JsonProcessingException ex) {
            throw new ErrorException("Erro a o converter dsParametro para GetProductsByDateRS");
        }
        result.setIngressoModalidadeList(UtilsWS.montaIngressoModalidadeList(integrador, reservaNomeList, products));
        result.setIngresso(UtilsWS.montaIngresso(integrador, reservaNomeList, products));

        return result;
    }
}

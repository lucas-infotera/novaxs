package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.*;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Lucas
 **/

@Service
public class PreReservarWS {


    @Autowired
    private TarifarWS tarifarWS;


    public WSPreReservarRS preReservar(WSPreReservarRQ preReservarRQ) throws ErrorException {

        WSIntegrador integrador =  preReservarRQ.getIntegrador();

        for (WSReservaServico reservaServico : preReservarRQ.getReserva().getTransReservaServicoList()){
            WSTarifarServicoRQ tarifarServicoRQ = new WSTarifarServicoRQ(integrador, reservaServico);

            WSTarifarServicoRS tarifar = tarifarWS.tarifar(tarifarServicoRQ);


        }


        return null;
    }
}

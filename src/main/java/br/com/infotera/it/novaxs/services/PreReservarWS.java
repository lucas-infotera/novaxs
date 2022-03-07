package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.WSDocumentoTipoEnum;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Lucas
 **/

@Service
public class PreReservarWS {


    @Autowired
    private TarifarWS tarifarWS;


    public WSPreReservarRS preReservar(WSPreReservarRQ preReservarRQ) throws ErrorException {

        WSIntegrador integrador = preReservarRQ.getIntegrador();

        WSReserva reserva = new WSReserva(montaReservaServicoList(preReservarRQ, integrador));

        return new WSPreReservarRS(reserva, preReservarRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }

    private List<WSReservaServico> montaReservaServicoList(WSPreReservarRQ preReservarRQ, WSIntegrador integrador) throws ErrorException {
        List<WSReservaServico> reservaServicoList = new ArrayList();

        for (WSReservaServico reservaServico : preReservarRQ.getReserva().getReservaServicoList()) {
            WSTarifarServicoRQ tarifarServicoRQ = new WSTarifarServicoRQ(integrador, reservaServico);

            WSTarifarServicoRS tarifar = tarifarWS.tarifar(tarifarServicoRQ);

            reservaServico = tarifar.getReservaServico();

            //adiciona apenas como opção de documento o CPF a o Pax principal

            reservaServico.getServico().getReservaNomeList().get(0).setDocumento(new WSDocumento(WSDocumentoTipoEnum.CPF, true));
//            reservaServico.getServico().getReservaNomeList().get(0).setDocumentoList(Arrays.asList(new WSDocumento(WSDocumentoTipoEnum.CPF, true)));

//            reservaServico.getServico().getReservaNomeList().get(1).setDocumento(null);
//            reservaServico.getServico().getReservaNomeList().get(1).setDocumentoList(Arrays.asList(new WSDocumento(WSDocumentoTipoEnum.RG, false)));

            reservaServicoList.add(reservaServico);
        }
        return reservaServicoList;
    }
}

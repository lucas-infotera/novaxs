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

        List<WSReservaServico> reservaServicoList = montaReservaServicoList(preReservarRQ, integrador);

        return new WSPreReservarRS(new WSReserva(reservaServicoList), preReservarRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }

    private List<WSReservaServico> montaReservaServicoList(WSPreReservarRQ preReservarRQ, WSIntegrador integrador) throws ErrorException {
        List<WSReservaServico> reservaServicoList = new ArrayList();

        for (WSReservaServico reservaServico : preReservarRQ.getReserva().getTransReservaServicoList()) {
            WSTarifarServicoRQ tarifarServicoRQ = new WSTarifarServicoRQ(integrador, reservaServico);

            WSTarifarServicoRS tarifar = tarifarWS.tarifar(tarifarServicoRQ);

            reservaServico = tarifar.getReservaServico();

            //adiciona apenas como opção de documento o CPF
            for (WSReservaNome rn : reservaServico.getServico().getReservaNomeList()) {
                rn.setDocumentoList(Arrays.asList(new WSDocumento(WSDocumentoTipoEnum.CPF, true)));
            }
            reservaServicoList.add(reservaServico);
        }
        return reservaServicoList;
    }
}

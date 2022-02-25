package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import org.springframework.stereotype.Service;

/**
 * @Author Lucas
 **/
@Service
public class PreCancelarWS {
    public WSReservaRS preCancelar(WSReservaRQ wsRQ) throws ErrorException {
        WSIntegrador integrador = wsRQ.getIntegrador();
        integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
        WSReservaRS wsReservaRS = new WSReservaRS(wsRQ.getReserva(), integrador);
        wsReservaRS.getReserva().setReservaStatus(WSReservaStatusEnum.CONFIRMADO);
        return wsReservaRS;
    }
}

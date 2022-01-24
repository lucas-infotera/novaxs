package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.reserva.rqrs.WSReservarRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRS;
import org.springframework.stereotype.Service;

/**
 * @Author Lucas
 **/
@Service
public class ReservarWS {
    public WSReservarRS reservar(WSReservarRQ reservarRQ) throws ErrorException {
        reservarRQ.getReserva().getReservaServicoList().get(0).setReservaStatus(WSReservaStatusEnum.RESERVADO);
        return new WSReservarRS(reservarRQ.getReserva(), reservarRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
}

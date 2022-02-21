
package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.CancelBillRQ;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author Lucas
 **/

@Service
public class CancelarWS {

    @Autowired
    private NovaxsClient novaxsClient;

    public WSReservaRS cancelar(WSReservaRQ wsRQ) throws ErrorException {
        CancelBillRQ cancelBillRQ;

        try {
            cancelBillRQ = montaRequestCancelBillRQ(wsRQ.getIntegrador(), wsRQ.getReserva().getReservaServicoList().get(0).getNrLocalizador());

            novaxsClient.cancelBillRQ(wsRQ.getIntegrador(), cancelBillRQ);
        } catch (Exception e) {
            wsRQ.getReserva().setReservaStatus(WSReservaStatusEnum.INCONSISTENTE);
        }
        wsRQ.getReserva().setReservaStatus(WSReservaStatusEnum.CANCELADO);
        return new WSReservaRS(wsRQ.getReserva(), wsRQ.getIntegrador());
    }

    private CancelBillRQ montaRequestCancelBillRQ(WSIntegrador integrador, String nrLocalizador) throws ErrorException {
        return new CancelBillRQ(UtilsWS.montaCredenciaisNovaXS(integrador)).setBill(nrLocalizador);
    }
}

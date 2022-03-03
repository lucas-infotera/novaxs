package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.CancelBillRQ;
import br.com.infotera.it.novaxs.model.CancelBillRS;
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

        CancelBillRS cancelBillRS = null;
        WSIntegrador integrador = wsRQ.getIntegrador();
        try {
            cancelBillRQ = montaRequestCancelBillRQ(integrador, wsRQ.getReserva().getReservaServicoList().get(0).getNrLocalizador());

                cancelBillRS = novaxsClient.cancelBillRQ(integrador, cancelBillRQ);
            if (cancelBillRS.getSuccess()) {
                wsRQ.getReserva().setReservaStatus(WSReservaStatusEnum.CANCELADO);
                wsRQ.getReserva().getReservaServicoList().get(0).setReservaStatus(WSReservaStatusEnum.CANCELADO);
            }
        } catch (Exception e) {
            wsRQ.getReserva().setReservaStatus(WSReservaStatusEnum.INCONSISTENTE);
        }

        return new WSReservaRS(wsRQ.getReserva(), integrador, WSIntegracaoStatusEnum.OK);
    }

    private CancelBillRQ montaRequestCancelBillRQ(WSIntegrador integrador, String nrLocalizador) throws ErrorException {
        return new CancelBillRQ(UtilsWS.montaCredenciaisNovaXS(integrador)).setBill(nrLocalizador);
    }
}

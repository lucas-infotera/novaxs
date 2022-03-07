package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.it.novaxs.model.BillForRS;
import br.com.infotera.it.novaxs.model.VoucherRS;
import br.com.infotera.it.novaxs.utils.Parametro;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Lucas
 **/
@Service
public class ConsultarWS {

    @Autowired
    private ConfirmarWS confirmarWS;

    public WSReservaRS consultar(WSReservaRQ wsRQ) throws ErrorException {

        Parametro parametro = UtilsWS.converterDSParametro(wsRQ.getReserva().getReservaServicoList().get(0).getDsParametro());

        VoucherRS voucherRS = null;
        try {
            voucherRS = confirmarWS.chamaObtencaodeVoucherVoucherRQ(wsRQ.getIntegrador(),
                    new BillForRS()
                    .setVoucher(parametro.getVoucher())
                    .setToken(parametro.getTokenVoucher()));

            if (voucherRS.getStatus() != null) {
                if (voucherRS.getStatus().contains("cancelado")) {
                    wsRQ.getIntegrador().setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
                    wsRQ.getReserva().setReservaStatus(WSReservaStatusEnum.CANCELADO);
                    wsRQ.getReserva().getReservaServicoList().get(0).setReservaStatus(WSReservaStatusEnum.CANCELADO);
                }
            }

        } catch (ErrorException ex) {
            ex.setIntegrador(wsRQ.getIntegrador());
            throw ex;
        }

        return new WSReservaRS(wsRQ.getReserva(), wsRQ.getIntegrador());
    }
}

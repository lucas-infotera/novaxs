/*
package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.BillForRQ;
import br.com.infotera.it.novaxs.model.BuyToBillForRQ;
import br.com.infotera.it.novaxs.model.GetAccessListRQ;
import br.com.infotera.it.novaxs.model.SetAccessListRQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

*/
/**
 * @Author Lucas
 **//*


@Service
public class ConfirmarWS {

    @Autowired
    NovaxsClient novaxsClient;

    public WSReservaRS confirmar(WSReservaRQ wsRQ) throws ErrorException {

        WSReservaRS wsReservaRS
        BuyToBillForRQ buyToBillForRQ = new BuyToBillForRQ();
        buyToBillForRQ.setPersonAsString();



        BillForRQ billForRQ = new BillForRQ();

        GetAccessListRQ getAccessListRQ = new GetAccessListRQ();

        SetAccessListRQ setAccessListRQ = new SetAccessListRQ();


        return new WSReservaRS(reservaRS.getReserva(), reservaRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
}
*/

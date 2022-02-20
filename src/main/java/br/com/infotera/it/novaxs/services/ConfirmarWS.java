package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReserva;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.*;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Lucas
 */


@Service
public class ConfirmarWS {

    @Autowired
    NovaxsClient novaxsClient;

    public WSReservaRS confirmar(WSReservaRQ reservaRQ) throws ErrorException {

        BuyToBillForRQ buyToBillForRQ = montaRequestBuytoBillForRQ(reservaRQ.getIntegrador(), reservaRQ.getReserva());



        BillForRQ billForRQ = new BillForRQ();

        GetAccessListRQ getAccessListRQ = new GetAccessListRQ();

        SetAccessListRQ setAccessListRQ = new SetAccessListRQ();


//        return new WSReservaRS(reservaRS.getReserva(), reservaRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
        return null;
    }

    private BuyToBillForRQ montaRequestBuytoBillForRQ(WSIntegrador integrador, WSReserva reserva) throws ErrorException {
        CredenciaisNovaxsRQ credenciaisNovaxsRQ = UtilsWS.montaCredenciaisNovaXS(integrador);
//        CredenciaisNovaxsRQ credenciaisNovaxsRQ = UtilsWS.montaCredenciaisNovaXS(integrador, "token");

        ProductsArray productsArray = UtilsWS.montaProductsArray(reserva);


        BuyToBillForRQ buyToBillForRQ =
                new BuyToBillForRQ(credenciaisNovaxsRQ)
                .setProductsArray(productsArray)
                .setPersonAsString(UtilsWS.montaPersonAsStringDadosDoComprador(reserva.getContato()));
//                .setCustomData();

        return buyToBillForRQ;
    }




}

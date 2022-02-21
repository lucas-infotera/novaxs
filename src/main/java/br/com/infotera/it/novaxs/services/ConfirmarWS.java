package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReserva;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.*;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author Lucas
 */


@Service
public class ConfirmarWS {

    @Autowired
    NovaxsClient novaxsClient;

    public WSReservaRS confirmar(WSReservaRQ reservaRQ) throws ErrorException {
        WSIntegrador integrador = reservaRQ.getIntegrador();

        try {
            BuyToBillForRQ buyToBillForRQ = montaRequestBuytoBillForRQ(integrador, reservaRQ.getReserva());

            BuyToBillForRS buyToBillForRS = novaxsClient.buyToBillForRQ(integrador, buyToBillForRQ);

            BillForRQ billForRQ = montaRequestBillforRQ(integrador, Optional.ofNullable(buyToBillForRS)
                    .orElseThrow(() -> new ErrorException("Bloqueio não foi realizado")));

            BillForRS billForRS = novaxsClient.billForRQ(integrador, billForRQ);
            if (billForRS == null || billForRS.getLocalizador() == null) {
                throw new ErrorException("Erro na confirmação de venda");
            }

            GetAccessListRQ getAccessListRQ = montaRequestGetAccessListRQ(integrador, buyToBillForRS);

            List<GetAccessListRS> getAccessListRS = novaxsClient.getAccessListRQ(integrador, getAccessListRQ);

            if (getAccessListRS == null || getAccessListRS.isEmpty()) {
                throw new ErrorException("Erro na obtenção da Lista de acesso para preenchimento");
            }

            SetAccessListRQ setAccessListRQ = montaRequestSetAccessListRQ(integrador, buyToBillForRS, getAccessListRS, reservaRQ.getReserva().getReservaServicoList().get(0).getServico().getReservaNomeList());
            /* Retorno vazio não ha tratamento a fazer no metodo setAccessListRQ */
            novaxsClient.setAccessListRQ(integrador, setAccessListRQ);

            VoucherRQ voucherRQ = montaRequestVoucherRQ(integrador, billForRS);

            VoucherRS voucherRS = novaxsClient.voucherRQ(integrador, voucherRQ);


        } catch (ErrorException ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            if (ex.getIntegrador() == null) {
                ex.setIntegrador(integrador);
            }
            throw ex;
        } catch (NullPointerException ex) {
            throw new ErrorException(integrador, ConfirmarWS.class, "confirmar", WSMensagemErroEnum.SCF, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConfirmarWS.class, "confirmar", WSMensagemErroEnum.SCF, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        }


//        return new WSReservaRS(reservaRS.getReserva(), integrador, WSIntegracaoStatusEnum.OK);
        return null;
    }

    private VoucherRQ montaRequestVoucherRQ(WSIntegrador integrador, BillForRS billForRS) throws ErrorException {
        VoucherRQ voucherRQ = null;
        if (billForRS.getVoucher() != null) {
            voucherRQ = new VoucherRQ(UtilsWS.montaCredenciaisNovaXS(integrador))
                    .setVoucher(billForRS.getVoucher());
        } else {
            throw new ErrorException("Valor do voucher retornado no metodo BillForRS esta null");
        }
        return voucherRQ;
    }

    private SetAccessListRQ montaRequestSetAccessListRQ(WSIntegrador integrador, BuyToBillForRS buyToBillForRS, List<GetAccessListRS> getAccessListRS, List<WSReservaNome> reservaNomeList) throws ErrorException {
        SetAccessListRQ setAccessListRQ = null;
        if (buyToBillForRS.getLocalizador() != null) {
            setAccessListRQ = new SetAccessListRQ(UtilsWS.montaCredenciaisNovaXS(integrador));
            setAccessListRQ.setBill(buyToBillForRS.getLocalizador());

            setAccessListRQ.setList(montaListSetAccessListRQ(getAccessListRS, reservaNomeList));

        }
        return setAccessListRQ;
    }

    private ListSetAccessListRQ montaListSetAccessListRQ(List<GetAccessListRS> getAccessListRS, List<WSReservaNome> reservaNomeList) {
        ListSetAccessListRQ listSetAccessListRQ = null;
        if (getAccessListRS != null && !reservaNomeList.isEmpty()) {
            if (getAccessListRS.size() == reservaNomeList.size()) {
                listSetAccessListRQ = new ListSetAccessListRQ();
                int indexGetAccessListrs = 0;
                for (WSReservaNome rn : reservaNomeList) {
                    getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setName(rn.getNmNomeCompleto());
                    getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setDocument(rn.getDocumento().getNrDocumento());
                    getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setBirth(UtilsWS.montaDataNovaxs(rn.getDtNascimento()));
                    indexGetAccessListrs++;
                }
            }
        }
        listSetAccessListRQ.setAccessPersonList(getAccessListRS);
        return listSetAccessListRQ;
    }



    private GetAccessListRQ montaRequestGetAccessListRQ(WSIntegrador integrador, BuyToBillForRS buyToBillForRS) throws ErrorException {
        GetAccessListRQ getAccessListRQ = null;
        if (buyToBillForRS.getLocalizador() != null) {
            getAccessListRQ = new GetAccessListRQ(UtilsWS.montaCredenciaisNovaXS(integrador));
            getAccessListRQ.setBill(buyToBillForRS.getLocalizador());
        }
        return getAccessListRQ;
    }

    private BillForRQ montaRequestBillforRQ(WSIntegrador integrador, BuyToBillForRS buyToBillForRS) throws ErrorException {
        BillForRQ billForRQ = null;
        if (buyToBillForRS.getLocalizador() != null) {
            billForRQ = new BillForRQ(UtilsWS.montaCredenciaisNovaXS(integrador));
            billForRQ.setBill(buyToBillForRS.getLocalizador());
        } else {
            throw new ErrorException("Localizador do bloqueio no metodo buyToBillFor não foi retornado pela Novaxs");
        }

        return billForRQ;
    }

    private BuyToBillForRQ montaRequestBuytoBillForRQ(WSIntegrador integrador, WSReserva reserva) throws ErrorException {
        CredenciaisNovaxsRQ credenciaisNovaxsRQ = UtilsWS.montaCredenciaisNovaXS(integrador);
//        CredenciaisNovaxsRQ credenciaisNovaxsRQ = UtilsWS.montaCredenciaisNovaXS(integrador, "token");

        BuyToBillForRQ buyToBillForRQ =
                new BuyToBillForRQ(credenciaisNovaxsRQ)
                        .setProductsArray(UtilsWS.montaProductsArray(reserva))
                        .setPersonAsString(UtilsWS.montaPersonAsStringDadosDoComprador(reserva.getContato()))
                        .setCustomData(UtilsWS.montaCustomData(reserva.getContato()));

        return buyToBillForRQ;
    }


}

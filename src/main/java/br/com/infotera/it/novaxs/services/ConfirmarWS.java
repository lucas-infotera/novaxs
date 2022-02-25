package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReserva;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
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

        WSReserva reserva = reservaRQ.getReserva();
        try {
            BuyToBillForRS buyToBillForRS = chamaBloqueioParaReservaBuytoBillForRQ(integrador, reserva);

            BillForRS billForRS = chamaReservarBillForRQ(integrador, buyToBillForRS);

            List<GetAccessListRS> getAccessListRS = chamaObtencaoDeListaDeAcessoGetAccessListRQ(integrador, buyToBillForRS);

            SetAccessListRS setAccessListRS = chamaMontagemListaDeAcessoSetAccessListRQ(integrador, reserva, buyToBillForRS, getAccessListRS);

            String erro = setAccessListRS.getErro();

            VoucherRS voucherRS = chamaObtencaodeVoucherVoucherRQ(integrador, billForRS);

            reserva.getReservaServicoList().forEach((reservaServico) -> {
                reservaServico.getServico().setDsURL(voucherRS.getEndpointVoucher());
            });

            reserva.getReservaServicoList().get(0).setReservaStatus(WSReservaStatusEnum.CONFIRMADO);
            reserva.getReservaServicoList().get(0).setNrLocalizador(buyToBillForRS.getId().toString());
            reserva.getReservaServicoList().get(0).setNrLocalizadorFornecedor(buyToBillForRS.getLocalizador());

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


        return new WSReservaRS(reserva, integrador, WSIntegracaoStatusEnum.OK);
    }

    public VoucherRS chamaObtencaodeVoucherVoucherRQ(WSIntegrador integrador, BillForRS billForRS) throws ErrorException {
        VoucherRQ voucherRQ = montaRequestVoucherRQ(integrador, billForRS);

        VoucherRS voucherRS = novaxsClient.voucherRQ(integrador, voucherRQ);
        return voucherRS;
    }

    public SetAccessListRS chamaMontagemListaDeAcessoSetAccessListRQ(WSIntegrador integrador, WSReserva reserva, BuyToBillForRS buyToBillForRS, List<GetAccessListRS> getAccessListRS) throws ErrorException {
        SetAccessListRQ setAccessListRQ = montaRequestSetAccessListRQ(integrador, buyToBillForRS, getAccessListRS, reserva.getReservaServicoList().get(0).getServico().getReservaNomeList());
        /* Retorno vazio não ha tratamento a fazer no metodo setAccessListRQ */
        SetAccessListRS setAccessListRS = novaxsClient.setAccessListRQ(integrador, setAccessListRQ);
        return setAccessListRS;
    }

    public List<GetAccessListRS> chamaObtencaoDeListaDeAcessoGetAccessListRQ(WSIntegrador integrador, BuyToBillForRS buyToBillForRS) throws ErrorException {
        GetAccessListRQ getAccessListRQ = montaRequestGetAccessListRQ(integrador, buyToBillForRS);

        List<GetAccessListRS> getAccessListRS = novaxsClient.getAccessListRQ(integrador, getAccessListRQ);

        if (getAccessListRS == null || getAccessListRS.isEmpty()) {
            throw new ErrorException("Erro na obtenção da Lista de acesso para preenchimento");
        }
        return getAccessListRS;
    }

    public BillForRS chamaReservarBillForRQ(WSIntegrador integrador, BuyToBillForRS buyToBillForRS) throws ErrorException {
        BillForRQ billForRQ = montaRequestBillforRQ(integrador, buyToBillForRS);

        BillForRS billForRS = novaxsClient.billForRQ(integrador, billForRQ);
        if (billForRS == null || billForRS.getLocalizador() == null) {
            throw new ErrorException("Erro na confirmação de venda");
        }
        return billForRS;
    }

    public BuyToBillForRS chamaBloqueioParaReservaBuytoBillForRQ(WSIntegrador integrador, WSReserva reserva) throws ErrorException {
        BuyToBillForRQ buyToBillForRQ = montaRequestBuytoBillForRQ(integrador, reserva);

        BuyToBillForRS buyToBillForRS = Optional.ofNullable((novaxsClient.buyToBillForRQ(integrador, buyToBillForRQ)))
                .orElseThrow(() -> new ErrorException("Bloqueio não foi realizado"));
        return buyToBillForRS;
    }

    public VoucherRQ montaRequestVoucherRQ(WSIntegrador integrador, BillForRS billForRS) throws ErrorException {
        VoucherRQ voucherRQ = null;
        if (billForRS.getVoucher() != null) {
            voucherRQ = new VoucherRQ(UtilsWS.montaCredenciaisNovaXS(integrador, billForRS.getToken()))
                    .setVoucher(billForRS.getVoucher());
        } else {
            throw new ErrorException("Valor do voucher retornado no metodo BillForRS esta null");
        }
        return voucherRQ;
    }

    public SetAccessListRQ montaRequestSetAccessListRQ(WSIntegrador integrador, BuyToBillForRS buyToBillForRS, List<GetAccessListRS> getAccessListRS, List<WSReservaNome> reservaNomeList) throws ErrorException {
        SetAccessListRQ setAccessListRQ = null;
        if (buyToBillForRS.getLocalizador() != null) {
            setAccessListRQ = new SetAccessListRQ(UtilsWS.montaCredenciaisNovaXS(integrador));
            setAccessListRQ.setBill(buyToBillForRS.getId().toString());

            setAccessListRQ.setList(montaListSetAccessListRQ(getAccessListRS, reservaNomeList));

        }
        return setAccessListRQ;
    }

    public ListSetAccessListRQ montaListSetAccessListRQ(List<GetAccessListRS> getAccessListRS, List<WSReservaNome> reservaNomeList) {
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



    public GetAccessListRQ montaRequestGetAccessListRQ(WSIntegrador integrador, BuyToBillForRS buyToBillForRS) throws ErrorException {
        GetAccessListRQ getAccessListRQ = null;
        if (buyToBillForRS.getLocalizador() != null) {
            getAccessListRQ = new GetAccessListRQ(UtilsWS.montaCredenciaisNovaXS(integrador));
            getAccessListRQ.setBill(buyToBillForRS.getId().toString());
        }
        return getAccessListRQ;
    }

    public BillForRQ montaRequestBillforRQ(WSIntegrador integrador, BuyToBillForRS buyToBillForRS) throws ErrorException {
        BillForRQ billForRQ = null;
        if (buyToBillForRS.getLocalizador() != null) {
            billForRQ = new BillForRQ(UtilsWS.montaCredenciaisNovaXS(integrador));
            billForRQ.setBill(buyToBillForRS.getId().toString());
        } else {
            throw new ErrorException("Localizador do bloqueio no metodo buyToBillFor não foi retornado pela Novaxs");
        }

        return billForRQ;
    }

    public BuyToBillForRQ montaRequestBuytoBillForRQ(WSIntegrador integrador, WSReserva reserva) throws ErrorException {
        CredenciaisNovaxsRQ credenciaisNovaxsRQ = UtilsWS.montaCredenciaisNovaXS(integrador);
//        CredenciaisNovaxsRQ credenciaisNovaxsRQ = UtilsWS.montaCredenciaisNovaXS(integrador, "token");

        BuyToBillForRQ buyToBillForRQ =
                new BuyToBillForRQ(credenciaisNovaxsRQ)
                        .setProductsArray(UtilsWS.montaProductsArray(reserva))
                        .setPersonAsString(UtilsWS.montaPersonAsStringDadosDoComprador(reserva))
                        .setCustomData(UtilsWS.montaCustomData());

        return buyToBillForRQ;
    }


}

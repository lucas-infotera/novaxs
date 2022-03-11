package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.*;
import br.com.infotera.it.novaxs.utils.Parametro;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Lucas
 */


@Service
public class ConfirmarWS {

    @Autowired
    NovaxsClient novaxsClient;

    public WSReservaRS confirmar(WSReservaRQ reservaRQ) throws ErrorException {
        WSIntegrador integrador = reservaRQ.getIntegrador();
        WSReserva result;
        WSReserva reserva = reservaRQ.getReserva();
        try {
            BuyToBillForRS buyToBillForRS = chamaBloqueioParaReservaBuytoBillForRQ(integrador, reserva);

            BillForRS billForRS = chamaReservarBillForRQ(integrador, buyToBillForRS);

            List<GetAccessListRS> getAccessListRS = chamaObtencaoDeListaDeAcessoGetAccessListRQ(integrador, buyToBillForRS);

            SetAccessListRS setAccessListRS = chamaMontagemListaDeAcessoSetAccessListRQ(integrador, reserva, buyToBillForRS, getAccessListRS);

            String erro = setAccessListRS.getErro();

            Parametro parametro = UtilsWS.converterDSParametro(reservaRQ.getReserva().getReservaServicoList().get(0).getDsParametro());

            parametro.setTokenVoucher(billForRS.getToken());
            parametro.setVoucher(billForRS.getVoucher());

            VoucherRS voucherRS = chamaObtencaodeVoucherVoucherRQ(integrador, billForRS);


            WSReservaServico reservaServico = new WSReservaServico();
            reservaServico.setReservaStatus(WSReservaStatusEnum.CONFIRMADO);
            reservaServico.setNrLocalizador(buyToBillForRS.getId().toString());
            reservaServico.setServico(reservaRQ.getReserva().getReservaServicoList().get(0).getServico());
            reservaServico.getServico().setDsURL(voucherRS.getEndpointVoucher());

            reservaServico.setDsParametro(parametro.toString());
            reservaServico.getServico().setDsParametro(parametro.toString());

            result = new WSReserva(reservaServico);


        } catch (ErrorException ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.INCONSISTENTE);
            if (ex.getIntegrador() == null) {
                ex.setIntegrador(integrador);
            }
            throw ex;
        } catch (NullPointerException ex) {
            throw new ErrorException(integrador, ConfirmarWS.class, "confirmar", WSMensagemErroEnum.SCF, ex.getMessage(), WSIntegracaoStatusEnum.INCONSISTENTE, ex);
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConfirmarWS.class, "confirmar", WSMensagemErroEnum.SCF, ex.getMessage(), WSIntegracaoStatusEnum.INCONSISTENTE, ex);
        }

        return new WSReservaRS(result, integrador, WSIntegracaoStatusEnum.OK);
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
            listSetAccessListRQ = new ListSetAccessListRQ();
            int indexGetAccessListrs = 0;
            do {
                for (WSReservaNome rn : reservaNomeList) {
                    if (indexGetAccessListrs < getAccessListRS.size()) {
                        if (getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).getName().contains(rn.getNmNome())) {
                            getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setBirth(UtilsWS.montaDataNovaxs(rn.getDtNascimento()));
                        } else {
                            if (!getAccessListRS.get(indexGetAccessListrs).getCustomData().getProductName().toUpperCase().contains("CHD")) {
                                getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setName(rn.getNmNomeCompleto());
                                if (rn.getDocumento() != null) {
                                    if (rn.getDocumento().getNrDocumento() != null) {
                                        getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setDocument(rn.getDocumento().getNrDocumento());
                                    } else {
                                        getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setDocument(null);
                                    }
                                } else {
                                    getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setDocument(null);
                                }
                                getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setBirth(UtilsWS.montaDataNovaxs(rn.getDtNascimento()));
                            } else {
                                WSReservaNome crianca = reservaNomeList.stream()
                                        .filter(rn1 -> {
                                            return rn1.getPaxTipo().isChd();
                                        })
                                        .findFirst()
                                        .orElse(null);

                                getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setName(crianca.getNmNomeCompleto());
                                if (crianca.getDocumento() != null) {
                                    if (crianca.getDocumento().getNrDocumento() != null) {
                                        getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setDocument(crianca.getDocumento().getNrDocumento());
                                    } else {
                                        getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setDocument(null);
                                    }
                                } else {
                                    getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setDocument(null);
                                }
                                getAccessListRS.get(indexGetAccessListrs).getAccessPersons().get(0).setBirth(UtilsWS.montaDataNovaxs(crianca.getDtNascimento()));
                            }
                        }

                        indexGetAccessListrs++;
                    }
                }
            } while (indexGetAccessListrs < getAccessListRS.size());
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

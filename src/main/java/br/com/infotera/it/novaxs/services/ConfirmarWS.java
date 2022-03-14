package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSPaxTipoEnum;
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

    public ListSetAccessListRQ montaListSetAccessListRQ(List<GetAccessListRS> getAccessListRS, List<WSReservaNome> reservaNomeList) throws ErrorException {
        ListSetAccessListRQ listSetAccessListRQ = null;
        if (getAccessListRS != null && !reservaNomeList.isEmpty()) {
            listSetAccessListRQ = new ListSetAccessListRQ();

            /* Bug novaxs
             * Em alguns casos API novaxs retorna um nome fora do contexto da reserva
             * Nesses casos é feito a total sobreescrita dos dados não permitindo que exista dados incongruentes */

            for (WSReservaNome rn : reservaNomeList) {
                if (rn.getPaxTipo().equals(WSPaxTipoEnum.ADT)) {
                    if (getAccessListRS.get(0).getAccessPersons().get(0).getName().toUpperCase().contains(rn.getNmNome().toUpperCase())
                            && rn.isStPrincipal()) {
                        getAccessListRS.get(0).getAccessPersons().get(0).setBirth(UtilsWS.montaDataNovaxs(rn.getDtNascimento()));
                    } else if (rn.isStPrincipal()
                            && !getAccessListRS.get(0).getAccessPersons().get(0).getName().isBlank()
                            && getAccessListRS.get(0).getAccessPersons().get(0).getName() != null
                            && getAccessListRS.get(0).getAccessPersons().get(0).getName() != " ") {

                        getAccessListRS.get(0).getAccessPersons().get(0).setName(rn.getNmNomeCompleto());
                        getAccessListRS.get(0).getAccessPersons().get(0).setDocument(rn.getDocumento().getNrDocumento());
                        getAccessListRS.get(0).getAccessPersons().get(0).setBirth(UtilsWS.montaDataNovaxs(rn.getDtNascimento()));

                    } else {
                        getAccessListRS.stream()
                                .filter(rs -> {
                                    if (!rs.getAccessPersons().get(0).getName().contains(rn.getNmNome())
                                            && !rs.getCustomData().getProductName().toUpperCase().contains("CHD")
                                            && rs.getAccessPersons().get(0).getName().isBlank()
                                            && rs.getAccessPersons().get(0).getDocument().isBlank()
                                            && rs.getAccessPersons().get(0).getBirth().isBlank()) {
                                        return true;
                                    }
                                    return false;
                                })
                                .map(rs -> {
                                    rs.getAccessPersons().get(0).setName(rn.getNmNomeCompleto());
                                    rs.getAccessPersons().get(0).setDocument(rn.getDocumento().getNrDocumento());
                                    rs.getAccessPersons().get(0).setBirth(UtilsWS.montaDataNovaxs(rn.getDtNascimento()));
                                    return rs;
                                })
                                .findFirst()
                                .orElseThrow(() -> new ErrorException("Erro a o montar lista de acesso"));

                    }
                } else if (rn.getPaxTipo().equals(WSPaxTipoEnum.CHD)) {
                    getAccessListRS.stream()
                            .filter(rs -> {
                                if (rs.getCustomData().getProductName().toUpperCase().contains("CHD")
                                        && rs.getAccessPersons().get(0).getName().isBlank()
                                        && rs.getAccessPersons().get(0).getDocument().isBlank()
                                        && rs.getAccessPersons().get(0).getBirth().isBlank()) {
                                    return true;
                                }
                                return false;
                            })
                            .map(rs -> {
                                rs.getAccessPersons().get(0).setName(rn.getNmNomeCompleto());
                                rs.getAccessPersons().get(0).setDocument(rn.getDocumento().getNrDocumento());
                                rs.getAccessPersons().get(0).setBirth(UtilsWS.montaDataNovaxs(rn.getDtNascimento()));
                                return rs;
                            })
                            .findFirst()
                            .orElseThrow(() -> new ErrorException("Erro a o montar lista de acesso com crianças"));
                }

            }


        }
        listSetAccessListRQ.setAccessPersonList(getAccessListRS);
        return listSetAccessListRQ;
    }


    public GetAccessListRQ montaRequestGetAccessListRQ(WSIntegrador integrador, BuyToBillForRS buyToBillForRS) throws
            ErrorException {
        GetAccessListRQ getAccessListRQ = null;
        if (buyToBillForRS.getLocalizador() != null) {
            getAccessListRQ = new GetAccessListRQ(UtilsWS.montaCredenciaisNovaXS(integrador));
            getAccessListRQ.setBill(buyToBillForRS.getId().toString());
        }
        return getAccessListRQ;
    }

    public BillForRQ montaRequestBillforRQ(WSIntegrador integrador, BuyToBillForRS buyToBillForRS) throws
            ErrorException {
        BillForRQ billForRQ = null;
        if (buyToBillForRS.getLocalizador() != null) {
            billForRQ = new BillForRQ(UtilsWS.montaCredenciaisNovaXS(integrador));
            billForRQ.setBill(buyToBillForRS.getId().toString());
        } else {
            throw new ErrorException("Localizador do bloqueio no metodo buyToBillFor não foi retornado pela Novaxs");
        }

        return billForRQ;
    }

    public BuyToBillForRQ montaRequestBuytoBillForRQ(WSIntegrador integrador, WSReserva reserva) throws
            ErrorException {
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

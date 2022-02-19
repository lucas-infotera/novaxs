package br.com.infotera.it.novaxs.utils;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.*;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author Lucas
 **/

@Component
public class UtilsWS {

    public static String variavelTemporaria = "VariavelDeTeste";
    private static ObjectMapper objectMapper;
    private static Validator validator;

    public static void verificaErro(WSIntegrador integrador, Object errorResponse) throws ErrorException {
        String dsErro = "";
        if (errorResponse != null) {
            dsErro = montaMensagemDeErro(errorResponse);
        }
        if (!dsErro.equals("")) {
            throw new ErrorException(integrador, NovaxsClient.class, "VerificaErro", WSMensagemErroEnum.GENCONEC, dsErro, WSIntegracaoStatusEnum.NEGADO, null);
        }
    }

    private static String montaMensagemDeErro(Object errorResponse) throws ErrorException {
        String errorsType = "";

        try {
            if (errorResponse instanceof GetProductsByDateRS) {
                GetProductsByDateRS getProductsByDateRS = (GetProductsByDateRS) errorResponse;
                if (getProductsByDateRS.getErro() != null) {
                    errorsType = getProductsByDateRS.getErro();
                }
            }
            if (errorResponse instanceof BuyToBillForRS) {
                BuyToBillForRS buyToBillForRS = (BuyToBillForRS) errorResponse;
                if (buyToBillForRS.getErro() != null) {
                    errorsType = buyToBillForRS.getErro();
                }
            }
            if (errorResponse instanceof BillForRS) {
                BillForRS billForRS = (BillForRS) errorResponse;
                if (billForRS.getErro() != null) {
                    errorsType = billForRS.getErro();
                }
            }
            if (errorResponse instanceof CreateBillPaymentLinkRS) {
                CreateBillPaymentLinkRS billPaymentLinkRS = (CreateBillPaymentLinkRS) errorResponse;
                if (billPaymentLinkRS.getErro() != null) {
                    errorsType = billPaymentLinkRS.getErro();
                }
            }

            if (errorResponse instanceof GetAccessListRS) {
                GetAccessListRS getAccessListRS = (GetAccessListRS) errorResponse;
                if (getAccessListRS.getErro() != null) {
                    errorsType = getAccessListRS.getErro();
                }
            }
            if (errorResponse instanceof SetAccessListRS) {
                SetAccessListRS setAccessListRS = (SetAccessListRS) errorResponse;
                if (setAccessListRS.getErro() != null) {
                    errorsType = setAccessListRS.getErro();
                }
            }
            if (errorResponse instanceof CancelBillRS) {
                CancelBillRS cancelBillRS = (CancelBillRS) errorResponse;
                if (cancelBillRS.getErro() != null) {
                    errorsType = cancelBillRS.getErro();
                }
            }
            if (errorResponse instanceof VoucherRS) {
                VoucherRS voucherRS = (VoucherRS) errorResponse;
                if (voucherRS.getVoucher() != null) {
                    String s = new String(voucherRS.getVoucher(), StandardCharsets.UTF_8);
                    if (s.contains("erro")) {
                        errorsType = montaRetorno(s, VoucherRS.class).getErro();
                    }
                }
            }

        } catch (Exception e) {
            throw new ErrorException("Erro a o montar ErrorResponse");
        }

        return errorsType;
    }

    private static double montaVlNeto(GetProductsByDateRS productsByDateRS) throws ErrorException {
        String strValue = Optional.ofNullable(productsByDateRS.getValue()).orElseThrow(() -> new ErrorException("Erro ao calcular valores por pax"));
        double vlNeto = Double.parseDouble(strValue) / 100;
        return vlNeto;
    }

    private static WSTarifa montaWSTarifa(WSIntegrador integrador, List<WSReservaNome> reservaNomeList, GetProductsByDateRS productsByDateRS) throws ErrorException {
        WSTarifa tarifa = null;
        double vlNeto = montaVlNeto(productsByDateRS);
        Integer qtPax = reservaNomeList.size();
        try {
            tarifa = new WSTarifa(productsByDateRS.getCurrency(), vlNeto, vlNeto / (qtPax.doubleValue()), null, null, WSPagtoFornecedorTipoEnum.FATURADO, UtilsWS.montaPoliticaList(productsByDateRS));

//            tarifa.setTarifaNomeList(UtilsWS.montaTarifaNome(integrador, m.getAmountsFrom(), ac.getCurrency()));

            return tarifa;

        } catch (Exception ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade", WSIntegracaoStatusEnum.NEGADO, ex);
        }
    }

    private static WSIngressoModalidade montaIngressoModalidade(WSTarifa tarifa, Product product) {
        return new WSIngressoModalidade(product.getId(), product.getName(), tarifa);
    }

    public static List<WSIngressoModalidade> montaIngressoModalidadeList(WSIntegrador integrador, List<WSReservaNome> reservaNomeList, GetProductsByDateRS productsByDateRS) throws ErrorException {
        List<WSIngressoModalidade> ingressoModalidadeList = null;
        try {
            if (productsByDateRS.getProducts() != null) {
                ingressoModalidadeList = montaIngressoModalidade(montaWSTarifa(integrador, reservaNomeList, productsByDateRS), productsByDateRS.getProducts());
            }
        } catch (NullPointerException ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "montaIngressoModalidadeList", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade" + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        } catch (ErrorException ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "montaIngressoModalidadeList", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return ingressoModalidadeList;
    }

    private static List<WSIngressoModalidade> montaIngressoModalidade(WSTarifa tarifa, List<Product> productList) {
        List<WSIngressoModalidade> wsIngressoModalidade = new ArrayList<>();
        if (!Utils.isListNothing(productList)) {
            for (Product product : productList) {
                wsIngressoModalidade.add(montaIngressoModalidade(tarifa, product));
            }
        }
        return wsIngressoModalidade;
    }

    public static WSIngresso montaIngresso(WSIntegrador integrador, List<WSReservaNome> reservaNomeList, GetProductsByDateRS productsByDateRS) {
        String dsParamTarifar = productsByDateRS.toString();
        List<WSMedia> mediaList = null;
        if (!productsByDateRS.getType().equals("Combo")) {
            mediaList = montaMediaList(productsByDateRS);
        } else {
            mediaList = montaMediaList(productsByDateRS.getProducts());
        }
        WSIngresso result = new WSIngresso(productsByDateRS.getPath(), productsByDateRS.getName(), UtilsWS.variavelTemporaria, null, null, null, reservaNomeList, null, mediaList, dsParamTarifar, null);

        result.setDsParametro(productsByDateRS.toString());

        return result;
    }

    private static List<WSMedia> montaMediaList(GetProductsByDateRS productsByDateRS) {
        List<WSMedia> result = new ArrayList<>();
        WSMedia media = new WSMedia(WSMediaCategoriaEnum.SERVICO, productsByDateRS.getImage());
        result.add(media);
        return result;
    }

    private static List<WSMedia> montaMediaList(List<Product> products) {
        List<WSMedia> result = new ArrayList<>();
        WSMedia media = null;
        for (Product product : products) {
            media = new WSMedia(WSMediaCategoriaEnum.SERVICO, product.getImage());
            result.add(media);
        }
        return result;
    }

    public static List<WSPolitica> montaPoliticaList(GetProductsByDateRS productsByDateRS) throws ErrorException {
        return null;
    }

    public static GetProductsByDateRS converterDSParametro(String dsParametro) throws ErrorException {
        GetProductsByDateRS result = null;
        try {
            result = montaRetorno(dsParametro, GetProductsByDateRS.class);
        } catch (ErrorException ex) {
            throw ex;
        }
        return result;
    }

    private static <T> T montaRetorno(String dsParametro, Class<T> retorno) throws ErrorException {
        Object objResponse = null;
        try {
            objResponse = objectMapper.readValue(dsParametro, retorno);
        } catch (JsonProcessingException ex) {
            throw new ErrorException("Erro a o converter dsParametro");
        }
        return retorno.cast(objResponse);
    }

    public static CredenciaisNovaxsRQ montaCredenciaisNovaXS(WSIntegrador integrador) throws ErrorException {
        CredenciaisNovaxsRQ credenciaisNovaxsRQ = null;
        try {
            credenciaisNovaxsRQ = new CredenciaisNovaxsRQ(integrador.getDsCredencialList().get(0), integrador.getDsCredencialList().get(1), integrador.getDsCredencialList().get(2));
            validator(credenciaisNovaxsRQ);
        } catch (ConstraintViolationException ex) {
            throw new ErrorException(ex.getMessage());
        }
        return credenciaisNovaxsRQ;
    }

    public static CredenciaisNovaxsRQ montaCredenciaisNovaXS(WSIntegrador integrador, String token) throws ErrorException {
        CredenciaisNovaxsRQ credenciaisNovaxsRQ = null;
        try {
            credenciaisNovaxsRQ = new CredenciaisNovaxsRQ(integrador.getDsCredencialList().get(0), integrador.getDsCredencialList().get(1), token);
            validator(credenciaisNovaxsRQ);
        } catch (ConstraintViolationException ex) {
            throw new ErrorException(ex.getMessage());
        }
        return credenciaisNovaxsRQ;
    }


    public static Person montaPersonAsStringDadosDoComprador(WSContato contato) throws ErrorException {
        Person result = null;
        try {
            result = new Person()
                    .setName(contato.getNome())
                    .setCellPhone(contato.getTelefone().getTransNrTelefone())
                    .setHomePhone(contato.getTelefone().getNrTelefone())
                    .setEmail(contato.getEmail());

            if (contato.getDocumento().getDocumentoTipo().equals(WSDocumentoTipoEnum.CPF)) {
                result.setCpf(contato.getDocumento().getNrDocumento());
            } else {
                throw new ErrorException("CPF é obrigatorio !");
            }
            validator(result);
        } catch (ErrorException ex) {
            throw ex;
        } catch (ConstraintViolationException ex) {
            throw new ErrorException(ex.getMessage());
        }
        return result;
    }

    public static <T> void validator(Object objeto) {

        Set<ConstraintViolation<Object>> violations = validator.validate(objeto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static ProductsArray montaProductsArray(WSReserva reserva) throws ErrorException {
        ProductsArray result = null;
        GetProductsByDateRS getProductsByDateRS;
        List<Product> products = new ArrayList<>();

        if (reserva != null) {
            result = new ProductsArray();
            if (reserva.getReservaServicoList() != null) {
                if (!Utils.isListNothing(reserva.getReservaServicoList())) {
                    for (WSReservaServico reservaServico : reserva.getReservaServicoList()) {
                        if (reservaServico.getDsParametro() != null) {
                            try {
                                getProductsByDateRS = Optional.ofNullable(converterDSParametro(reservaServico.getServico().getDsParametro())).
                                        orElseThrow(() -> new ErrorException("DsParametro com GetProductsByDate esta nulo !"));
                                Product product = montaProductBuytoBillForRQ(getProductsByDateRS, reservaServico);
                            } catch (NullPointerException ex) {
                                throw ex;
                            } catch (ErrorException ex) {
                                throw ex;
                            }
                        }
                    }
                }
            }
        }


        return result;
    }

    private static Product montaProductBuytoBillForRQ(GetProductsByDateRS productsByDateRS, WSReservaServico reservaServico) throws ErrorException {

        Product product = new Product()
                .setPath(productsByDateRS.getPath())
                .setAmount(productsByDateRS.getMinAmount())
                .setDate(montaDataNovaxs(reservaServico.getServico().getDtServico()));

        if (productsByDateRS.getSchedules() != null) {
            product.setSchedule(productsByDateRS.getSchedules().get(0).getSchedule());
        }

/*
        if () {
            product.setChildren()

            Child child = new Child();
            child.set
        }
*/
        return null;
    }

    public static String montaDataNovaxs(Date dtInicio) {
        return Utils.formatData(dtInicio, "dd/MM/yyyy");
    }

    @Autowired
    public void setValidator(Validator validator) {
        UtilsWS.validator = validator;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        UtilsWS.objectMapper = objectMapper;
    }
}

package br.com.infotera.it.novaxs.client;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.it.novaxs.model.*;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Lucas
 **/

@Component
public class NovaxsClient {

    @Autowired
    private RESTClient restClient;

    public List<GetProductsByDateRS> getProductsByDateRQ(WSIntegrador integrador, GetProductsByDateRQ getProductsByDateRQ) throws ErrorException {

        List<GetProductsByDateRS> result = null;
        integrador.setDsMetodo("getProductsByDateRQ");
        try {

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("token", getProductsByDateRQ.getToken());
            requestBody.add("login", getProductsByDateRQ.getLogin());
            requestBody.add("password", getProductsByDateRQ.getPassword());
            requestBody.add("method", getProductsByDateRQ.getMethod());
            requestBody.add("date", getProductsByDateRQ.getDate());

            result = Arrays.asList(restClient.sendReceive(integrador, requestBody, HttpMethod.POST, "getProductsByDate", GetProductsByDateRS[].class));
            UtilsWS.verificaErro(integrador, result);

            if (UtilsWS.variavelTemporaria != null) {
                result.forEach(getProductsByDateRS -> {
                    getProductsByDateRS.setImage("https://www.ifrr.edu.br/midia/teste/image");
                    if (getProductsByDateRS.getProducts() != null) {
                        getProductsByDateRS.getProducts().forEach(product -> {
                            product.setImage("https://www.ifrr.edu.br/midia/teste/image");
                        });
                    }
                });
            }

        } catch (ErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
            throw new ErrorException(integrador, NovaxsClient.class, "getProductsByDateRQ", WSMensagemErroEnum.SDI, ex.getMessage(), WSIntegracaoStatusEnum.OK, ex);
        }
        return result;
    }


    public BuyToBillForRS buyToBillForRQ(WSIntegrador integrador, BuyToBillForRQ buyToBillForRQ) throws ErrorException {
        BuyToBillForRS result = null;
        integrador.setDsMetodo("buyToBillForRQ");
        try {

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("token", buyToBillForRQ.getToken());
            requestBody.add("login", buyToBillForRQ.getLogin());
            requestBody.add("password", buyToBillForRQ.getPassword());
            requestBody.add("method", buyToBillForRQ.getMethod());
            requestBody.add("productsArray", buyToBillForRQ.getProductsArray());
            requestBody.add("personAsString", buyToBillForRQ.getPersonAsString());
            requestBody.add("customData", buyToBillForRQ.getCustomData());

            result = restClient.sendReceive(integrador, requestBody, HttpMethod.POST, "buyToBillFor", BuyToBillForRS.class);
            UtilsWS.verificaErro(integrador, result);

        } catch (ErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
            throw new ErrorException(integrador, NovaxsClient.class, "buyToBillForRQ", WSMensagemErroEnum.SRE, ex.getMessage(), WSIntegracaoStatusEnum.OK, ex);
        }
        return result;
    }

    public BillForRS billForRQ(WSIntegrador integrador, BillForRQ billRQ) throws ErrorException {
        BillForRS result = null;
        integrador.setDsMetodo("billForRQ");
        try {

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("token", billRQ.getToken());
            requestBody.add("login", billRQ.getLogin());
            requestBody.add("password", billRQ.getPassword());
            requestBody.add("method", billRQ.getMethod());
            requestBody.add("bill", billRQ.getBill());


            result = restClient.sendReceive(integrador, requestBody, HttpMethod.POST, "billFor", BillForRS.class);
            UtilsWS.verificaErro(integrador, result);

        } catch (ErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
            throw new ErrorException(integrador, NovaxsClient.class, "billForRQ", WSMensagemErroEnum.SRE, ex.getMessage(), WSIntegracaoStatusEnum.OK, ex);
        }
        return result;
    }

    public CreateBillPaymentLinkRS createBillPaymentLinkRQ(WSIntegrador integrador, CreateBillPaymentLinkRQ createPaymentLinkRQ) throws ErrorException {
        CreateBillPaymentLinkRS result = null;
        integrador.setDsMetodo("createBillPaymentLinkRQ");
        try {

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("token", createPaymentLinkRQ.getToken());
            requestBody.add("login", createPaymentLinkRQ.getLogin());
            requestBody.add("password", createPaymentLinkRQ.getPassword());
            requestBody.add("method", createPaymentLinkRQ.getMethod());
            requestBody.add("bill", createPaymentLinkRQ.getBill());


            result = restClient.sendReceive(integrador, requestBody, HttpMethod.POST, "createBillPaymentLink", CreateBillPaymentLinkRS.class);
            UtilsWS.verificaErro(integrador, result);

        } catch (ErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
            throw new ErrorException(integrador, NovaxsClient.class, "createBillPaymentLinkRQ", WSMensagemErroEnum.SRE, ex.getMessage(), WSIntegracaoStatusEnum.OK, ex);
        }
        return result;
    }

    public List<GetAccessListRS> getAccessListRQ(WSIntegrador integrador, GetAccessListRQ getAccessListRQ) throws ErrorException {
        List<GetAccessListRS> result = null;
        integrador.setDsMetodo("getAccessListRQ");
        try {

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("token", getAccessListRQ.getToken());
            requestBody.add("login", getAccessListRQ.getLogin());
            requestBody.add("password", getAccessListRQ.getPassword());
            requestBody.add("method", getAccessListRQ.getMethod());
            requestBody.add("bill", getAccessListRQ.getBill());


            result = Arrays.asList(restClient.sendReceive(integrador, requestBody, HttpMethod.POST, "getAccessList", GetAccessListRS[].class));
            UtilsWS.verificaErro(integrador, result);

        } catch (ErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
            throw new ErrorException(integrador, NovaxsClient.class, "getAccessListRQ", WSMensagemErroEnum.SRE, ex.getMessage(), WSIntegracaoStatusEnum.OK, ex);
        }
        return result;
    }

    public SetAccessListRS setAccessListRQ(WSIntegrador integrador, SetAccessListRQ setAccessListRQ) throws ErrorException {
        SetAccessListRS result = null;
        integrador.setDsMetodo("setAccessListRQ");
        try {

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("token", setAccessListRQ.getToken());
            requestBody.add("login", setAccessListRQ.getLogin());
            requestBody.add("password", setAccessListRQ.getPassword());
            requestBody.add("method", setAccessListRQ.getMethod());
            requestBody.add("bill", setAccessListRQ.getBill());
            requestBody.add("list", setAccessListRQ.getList());


            result = restClient.sendReceive(integrador, requestBody, HttpMethod.POST, "setAccessList", SetAccessListRS.class);
            UtilsWS.verificaErro(integrador, result);

        } catch (ErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
            throw new ErrorException(integrador, NovaxsClient.class, "setAccessListRQ", WSMensagemErroEnum.SRE, ex.getMessage(), WSIntegracaoStatusEnum.OK, ex);
        }
        return result;
    }

    public VoucherRS voucherRQ(WSIntegrador integrador, VoucherRQ voucherRQ) throws ErrorException {
        VoucherRS result;
        integrador.setDsMetodo("voucherRQ");
        try {

            result = restClient.sendReceive(integrador, voucherRQ, HttpMethod.GET,
                    "voucher?" +
                            "login=" + voucherRQ.getLogin() +
                            "&password=" + voucherRQ.getPassword() +
                            "&voucher=" + voucherRQ.getVoucher() +
                            "&token=" + voucherRQ.getToken() +
                            "&method=" + voucherRQ.getMethod()
                    , VoucherRS.class);
            UtilsWS.verificaErro(integrador, result);

        } catch (ErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
            throw new ErrorException(integrador, NovaxsClient.class, "voucherRQ", WSMensagemErroEnum.SRE, ex.getMessage(), WSIntegracaoStatusEnum.OK, ex);
        }
        return result;

    }

    public CancelBillRS cancelBillRQ(WSIntegrador integrador, CancelBillRQ cancelRQ) throws ErrorException {
        CancelBillRS result;
        integrador.setDsMetodo("cancelBillRQ");
        try {

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("token", cancelRQ.getToken());
            requestBody.add("login", cancelRQ.getLogin());
            requestBody.add("password", cancelRQ.getPassword());
            requestBody.add("method", cancelRQ.getMethod());
            requestBody.add("bill", cancelRQ.getBill());

            result = restClient.sendReceive(integrador, requestBody, HttpMethod.POST, "cancelBill", CancelBillRS.class);
            UtilsWS.verificaErro(integrador, result);

        } catch (ErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
            throw new ErrorException(integrador, NovaxsClient.class, "cancelBillRQ", WSMensagemErroEnum.SRE, ex.getMessage(), WSIntegracaoStatusEnum.OK, ex);
        }
        return result;

    }


}

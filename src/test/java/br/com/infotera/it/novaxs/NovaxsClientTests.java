package br.com.infotera.it.novaxs;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.*;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @Author Lucas
 **/

@SpringBootTest()
public class NovaxsClientTests {

    @Autowired
    DisponibilidadeWS disponibilidadeWS;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Gson gson;
    @Autowired
    private NovaxsClient novaxsClient;

    @Test
    public void teste1NovaxsClientGetProductsByDate() throws ErrorException {

        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);

        GetProductsByDateRQ teste = new GetProductsByDateRQ();
        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setDate("06/12/2022")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843");

        List<GetProductsByDateRS> getProductsByDateRS = assertDoesNotThrow(() -> novaxsClient.getProductsByDateRQ(integrador, teste));

        assertNotNull(getProductsByDateRS);

        try {
            System.out.println("Result teste1NovaxsClientGetProductsByDate --->" + objectMapper.writeValueAsString(getProductsByDateRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void teste1NovaxsClientGetProductsByDateMontaCredencial() throws ErrorException {

        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);


        GetProductsByDateRQ teste = new GetProductsByDateRQ(UtilsWS.montaCredenciaisNovaXS(integrador));

        teste.setDate("06/12/2022");


        List<GetProductsByDateRS> getProductsByDateRS = assertDoesNotThrow(() -> novaxsClient.getProductsByDateRQ(integrador, teste));

        assertNotNull(getProductsByDateRS);

        try {
            System.out.println("Result teste1NovaxsClientGetProductsByDate --->" + objectMapper.writeValueAsString(getProductsByDateRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void teste1NovaxsClientBuytoBillRQ() throws ErrorException {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);
        GetProductsByDateRS[] productsByDateRS = new GetProductsByDateRS[8];
        List<GetProductsByDateRS> testeList;
        Person person = null;
        CustomData customData;
        Product[] products = null;
        ProductsArray productsArray = new ProductsArray();
        try {
            productsByDateRS = objectMapper.readValue(JsonsTeste.testeGetProductsByDateRS(), GetProductsByDateRS[].class);
            person = objectMapper.readValue(JsonsTeste.jsonPerson(), Person.class);
            products = objectMapper.readValue(JsonsTeste.jsonProductArray(), Product[].class);
            productsArray.setProductsArray(products);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        customData = new CustomData()
                .setObs("26029143123")
                .setIdExterno("Joao da Silva");

        BuyToBillForRQ teste = new BuyToBillForRQ();

        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setCustomData("")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843")
                .setProductsArray(productsArray)
                .setPersonAsString(person);


        BuyToBillForRS buyToBillForRS = assertDoesNotThrow(() -> novaxsClient.buyToBillForRQ(integrador, teste));

        assertNotNull(buyToBillForRS);

        try {
            System.out.println("Result teste1NovaxsClientBuytoBillRQ ---> \n" + objectMapper.writeValueAsString(teste));
            System.out.println("Result teste1NovaxsClientBuytoBillRS ---> \n" + objectMapper.writeValueAsString(buyToBillForRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void teste1NovaxsClientBillForRQ() throws ErrorException {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);
        BillForRQ teste = new BillForRQ();

        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843")
                .setBill("1273105");

        BillForRS billForRS = assertDoesNotThrow(() -> novaxsClient.billForRQ(integrador, teste));

        assertNotNull(billForRS);

        try {
            System.out.println("Result teste1NovaxsClientBillRQ ---> \n" + objectMapper.writeValueAsString(teste));
            System.out.println("Result teste1NovaxsClientBillRS ---> \n" + objectMapper.writeValueAsString(billForRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void teste1NovaxscreateBillPaymentLinkRQ() throws ErrorException {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);
        CreateBillPaymentLinkRQ teste = new CreateBillPaymentLinkRQ();

        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843")
                .setBill("1546690");

        ErrorException throwsErrorException = Assertions.assertThrows(ErrorException.class, () -> novaxsClient.createBillPaymentLinkRQ(integrador, teste));
        CreateBillPaymentLinkRS createBillPaymentLinkRS = null;

        createBillPaymentLinkRS = assertDoesNotThrow(() -> novaxsClient.createBillPaymentLinkRQ(integrador, teste));


        assertNotNull(createBillPaymentLinkRS);

        try {
            System.out.println("Result teste1NovaxsClientBillRQ ---> \n" + objectMapper.writeValueAsString(teste));
            System.out.println("Result teste1NovaxsClientBillRS ---> \n" + objectMapper.writeValueAsString(createBillPaymentLinkRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void teste1NovaxsGetAccessListRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);
        GetAccessListRQ teste = new GetAccessListRQ();

        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843")
                .setBill("1546690");

        List<GetAccessListRS> getAccessListRS = null;

        getAccessListRS = assertDoesNotThrow(() -> novaxsClient.getAccessListRQ(integrador, teste));

//        ErrorException throwsErrorException = Assertions.assertThrows(ErrorException.class, () -> novaxsClient.getAccessListRQ(integrador, teste));


        assertNotNull(getAccessListRS);

        try {
            System.out.println("Result teste1NovaxsgetAccessListRQ ---> \n" + objectMapper.writeValueAsString(teste));
            System.out.println("Result teste1NovaxsgetAccessListRQ ---> \n" + objectMapper.writeValueAsString(getAccessListRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void teste1NovaxsSetAccessListRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);

        SetAccessListRQ teste = new SetAccessListRQ();

        GetAccessListRS[] accessPersonArray = null;

        try {
            accessPersonArray = objectMapper.readValue(JsonsTeste.jsonListAcessPerson(), GetAccessListRS[].class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ListSetAccessListRQ listSetAccessListRQ = new ListSetAccessListRQ();

        listSetAccessListRQ.setAccessPersonList(accessPersonArray);

        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843")
                .setBill("1546690")
                .setList(listSetAccessListRQ);


        SetAccessListRS setAccessListRS = null;

        setAccessListRS = assertDoesNotThrow(() -> novaxsClient.setAccessListRQ(integrador, teste));

//        ErrorException throwsErrorException = Assertions.assertThrows(ErrorException.class, () -> novaxsClient.setAccessListRQ(integrador, teste));


        assertNotNull(setAccessListRS);

        try {
            System.out.println("Result teste1NovaxsgetAccessListRQ ---> \n" + objectMapper.writeValueAsString(teste));
            System.out.println("Result teste1NovaxsgetAccessListRQ ---> \n" + objectMapper.writeValueAsString(setAccessListRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void teste2NovaxsSetAccessListRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);
        SetAccessListRQ teste = new SetAccessListRQ();

        GetAccessListRS[] accessPersonArray = null;

        try {
            accessPersonArray = objectMapper.readValue(JsonsTeste.jsonListAcessPerson(), GetAccessListRS[].class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ListSetAccessListRQ listSetAccessListRQ = new ListSetAccessListRQ();

        listSetAccessListRQ.setAccessPersonList(accessPersonArray);

        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843")
                .setBill("1546690")
                .setList(listSetAccessListRQ);


        SetAccessListRS setAccessListRS = null;

        setAccessListRS = assertDoesNotThrow(() -> novaxsClient.setAccessListRQ(integrador, teste));

//        ErrorException throwsErrorException = Assertions.assertThrows(ErrorException.class, () -> novaxsClient.setAccessListRQ(integrador, teste));


        assertNotNull(setAccessListRS);

        try {
            System.out.println("Result teste1NovaxsgetAccessListRQ ---> \n" + objectMapper.writeValueAsString(teste));
            System.out.println("Result teste1NovaxsgetAccessListRQ ---> \n" + objectMapper.writeValueAsString(setAccessListRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void teste1NovaxsVoucherRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);

        VoucherRQ teste = new VoucherRQ();

        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setToken("52F38AEB90C4CC484D8CCB322BB978E718B42D87")
                .setVoucher("827858");


        VoucherRS voucherRS = assertDoesNotThrow(() -> novaxsClient.voucherRQ(integrador, teste));

//        ErrorException throwsErrorException = Assertions.assertThrows(ErrorException.class, () -> novaxsClient.voucherRQ(integrador, teste));


        assertNotNull(voucherRS);

        try {
            System.out.println("Result teste1NovaxsVoucherRQ ---> \n" + objectMapper.writeValueAsString(teste));
            System.out.println("Result teste1NovaxsVoucherRQ ---> \n" + objectMapper.writeValueAsString(voucherRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void teste2NovaxsVoucherRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);

        VoucherRQ teste = new VoucherRQ();

        teste.setLogin("docuser")
                .setPassword("abc1234")
//                .setToken("52F38AEB90C4CC484D8CCB322BB978E718B42D87")
                .setVoucher("827858");


//        VoucherRS voucherRS = assertDoesNotThrow(() -> novaxsClient.voucherRQ(integrador, teste));

        ErrorException throwsErrorException = Assertions.assertThrows(ErrorException.class, () -> novaxsClient.voucherRQ(integrador, teste));


//        assertNotNull(voucherRS);

        try {
            System.out.println("Result teste1NovaxsVoucherRQ ---> \n" + objectMapper.writeValueAsString(teste));
//            System.out.println("Result teste1NovaxsVoucherRQ ---> \n" + objectMapper.writeValueAsString(voucherRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void teste1NovaxsCancelBillRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);

        CancelBillRQ teste = new CancelBillRQ();

        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843")
                .setBill("1546679");


        CancelBillRS cancelBillRS = assertDoesNotThrow(() -> novaxsClient.cancelBillRQ(integrador, teste));


        assertNotNull(cancelBillRS);

        try {
            System.out.println("Result teste1NovaxsCancelBillRQ ---> \n" + objectMapper.writeValueAsString(teste));
            System.out.println("Result teste1NovaxsCancelBillRQ ---> \n" + objectMapper.writeValueAsString(cancelBillRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void teste2NovaxsCancelBillRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);

        CancelBillRQ teste = new CancelBillRQ();

        teste.setLogin("docuser")
                .setPassword("abc1234")
//                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843")
                .setBill("1546679");


//        CancelBillRS cancelBillRS = assertDoesNotThrow(() -> novaxsClient.cancelBillRQ(integrador, teste));

        ErrorException throwsErrorException = Assertions.assertThrows(ErrorException.class, () -> novaxsClient.cancelBillRQ(integrador, teste));


//        assertNotNull(cancelBillRS);

        try {
            System.out.println("Result teste1NovaxsCancelBillRQ ---> \n" + objectMapper.writeValueAsString(teste));
//            System.out.println("Result teste1NovaxsCancelBillRQ ---> \n" + objectMapper.writeValueAsString(cancelBillRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}

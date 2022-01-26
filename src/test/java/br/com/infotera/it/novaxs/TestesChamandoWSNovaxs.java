package br.com.infotera.it.novaxs;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.*;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author Lucas
 **/

@SpringBootTest()
public class TestesChamandoWSNovaxs {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Gson gson;

    @Autowired
    private NovaxsClient novaxsClient;

    @Autowired
    DisponibilidadeWS disponibilidadeWS;



    @Test
    public void teste1NovaxsClientGetProductsByDate() throws ErrorException {

        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);

        GetProductsByDateRQ teste = new GetProductsByDateRQ();
        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setDate("05/02/2022")
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
    public void teste1NovaxsClientBuytoBillRQ() throws ErrorException {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);
        GetProductsByDateRS[] productsByDateRS = new GetProductsByDateRS[8];
        List<GetProductsByDateRS> testeList;
        Person person = null;
        try {
            productsByDateRS = objectMapper.readValue(JsonsTeste.testeGetProductsByDateRS(), GetProductsByDateRS[].class);
            person = objectMapper.readValue(JsonsTeste.jsonPerson(), Person.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        BuyToBillForRQ teste = new BuyToBillForRQ();

        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setCustomData("05/02/2022")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843")
                .setProductsArray("{\"path\":\"products/46700\",\"amount\":\"2\",\"date\":\"04/09/2022\"},{\"path\":\"products/46701\",\"amount\":\"1\",\"date\":\"04/09/2022\",\"schedule\":\"13:00\"},{\"path\":\"productCombos/2111\",\"amount\":\"1\",\"date\":\"04/09/2022\"},{\"path\":\"productCombos/2112\",\"amount\":\"1\",\"date\":\"04/09/2022\",\"children\":[{\"path\":\"products/46700\",\"date\":\"04/09/2022\"},{\"path\":\"products/46700\",\"date\":\"05/09/2022\"},{\"path\":\"products/46700\",\"date\":\"06/09/2022\"}]}")
                .setPersonAsString(person.toString());


        BuyToBillForRS buyToBillForRS = assertDoesNotThrow(() -> novaxsClient.buyToBillForRQ(integrador, teste));

        assertNotNull(buyToBillForRS);

        try {
            System.out.println("Result teste1NovaxsClientBuytoBillRQ ---> \n" + objectMapper.writeValueAsString(teste));
            System.out.println("Result teste1NovaxsClientBuytoBillRS ---> \n" + objectMapper.writeValueAsString(buyToBillForRS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }



}

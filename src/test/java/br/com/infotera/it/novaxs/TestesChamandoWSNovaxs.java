package br.com.infotera.it.novaxs;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.GetProductsByDateRQ;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

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

    @Test
    void contextLoads() {
    }

    @Test
    public void teste1ConversaoGetProductsByDate() throws JsonProcessingException {
        GetProductsByDateRS[] teste = objectMapper.readValue(JsonsTeste.testeGetProductsByDateRS(), GetProductsByDateRS[].class);

        System.out.println(teste[0]);
    }

    @Test
    public void teste2ConversaoGetProductsByDate() throws JsonProcessingException {
        GetProductsByDateRS[] productsByDateRS = objectMapper.readValue(JsonsTeste.testeGetProductsByDateRSVazio(), GetProductsByDateRS[].class);

        List<GetProductsByDateRS> testeList = Arrays.asList(productsByDateRS);
        //Faltar tratar quando vier vazio possivel solução transformar para Lista o Array
        Assertions.assertEquals(testeList.isEmpty(), productsByDateRS);
    }

    @Test
    public void teste1NovaxsClientGetProductsByDate() throws ErrorException {

        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);

        GetProductsByDateRQ teste = new GetProductsByDateRQ();
        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setDate("05/02/2022")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843");

        Assertions.assertDoesNotThrow(() -> novaxsClient.getProductsByDateRQ(integrador, teste));

        List<GetProductsByDateRS> productsByDateRQ = novaxsClient.getProductsByDateRQ(integrador, teste);

        Assertions.assertNotEquals(null, productsByDateRQ);

        try {
            System.out.println("Result teste1NovaxsClientGetProductsByDate --->" + objectMapper.writeValueAsString(productsByDateRQ));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }



}

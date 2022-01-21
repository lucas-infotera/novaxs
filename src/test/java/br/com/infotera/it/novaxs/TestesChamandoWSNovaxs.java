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
    public void teste1ConversaoGetProductsByDate() throws JsonProcessingException {
        GetProductsByDateRS[] teste = objectMapper.readValue(JsonsTeste.testeGetProductsByDateRS(), GetProductsByDateRS[].class);

        System.out.println(teste[0]);
    }

    @Test
    public void teste2ConversaoGetProductsByDate() throws JsonProcessingException {
        GetProductsByDateRS[] productsByDateRS = objectMapper.readValue(JsonsTeste.testeGetProductsByDateRSVazio(), GetProductsByDateRS[].class);

        List<GetProductsByDateRS> testeList = Arrays.asList(productsByDateRS);
        //Faltar tratar quando vier vazio possivel solução transformar para Lista o Array
        assertEquals(testeList.isEmpty(), productsByDateRS);
    }

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
    public void teste1ConversaoBuyToBillForRS() throws JsonProcessingException {
        BuyToBillForRS teste = objectMapper.readValue(JsonsTeste.testeBuytoBillForRS(), BuyToBillForRS.class);

        assertNotNull(teste);

        System.out.println(teste);
    }

    @Test
    public void testeProductsToStringJSON() {
        Product teste = new Product();
        String jsonTeste = null;

        teste.setImage("teste")
                .setAmount("dfad")
                .setCurrency("brl");

        assertNotNull(teste.toString());

        try {
            jsonTeste = objectMapper.writeValueAsString(teste);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        System.out.println("Converter para JSON usando o toString  -> \n" + teste.toString());
        System.out.println("Converter para JSON usando o mapper  -> \n" + jsonTeste);
    }


    @Test
    public void testeBuytoBillForRSJSON() {
        BuyToBillForRS teste = new BuyToBillForRS();
        String jsonTeste = null;

        teste.setAgencia("teste")
                .setAmount(200)
                .setCurrency("brl");

        assertNotNull(teste.toString());

        try {
            jsonTeste = objectMapper.writeValueAsString(teste);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        System.out.println("Converter para JSON usando o toString  -> \n" + teste.toString());
        System.out.println("Converter para JSON usando o mapper  -> \n" + jsonTeste);
    }

    @Test
    public void testeGetProductsByDateToStringJSON() {
        GetProductsByDateRS teste = new GetProductsByDateRS();
        String jsonTeste = null;

        teste.setImage("teste")
                .setAvailable("dafdfadfa")
                .setCurrency("brl");

        assertNotNull(teste.toString());

        try {
            jsonTeste = objectMapper.writeValueAsString(teste);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        System.out.println("Converter para JSON usando o toString  -> \n" + teste.toString());
        System.out.println("Converter para JSON usando o mapper  -> \n" + jsonTeste);
    }

    @Test
    public void teste1NovaxsClientBuytoBillRQ() throws ErrorException {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);

        BuyToBillForRQ teste = new BuyToBillForRQ();
        teste.setLogin("docuser")
                .setPassword("abc1234")
                .setCustomData("05/02/2022")
                .setToken("E1D779DB5D11E4C6EED41B418B53C2AC4205B843");


        BuyToBillForRS buyToBillForRS = assertDoesNotThrow(() -> novaxsClient.buyToBillForRQ(integrador, teste));

        assertNotNull(buyToBillForRS);

        try {
            System.out.println("Result teste1NovaxsClientBuytoBillRQ --->" + objectMapper.writeValueAsString(teste));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testeDsParametroDisponibilidade(){
        WSDisponibilidadeIngressoRQ wsDisponibilidadeIngressoRQ = gson.fromJson(JsonsTeste.jsonDisponibilidade(), WSDisponibilidadeIngressoRQ.class);
        assertNotNull(wsDisponibilidadeIngressoRQ);
        try {
            WSDisponibilidadeIngressoRS disponibilidade = disponibilidadeWS.disponibilidade(wsDisponibilidadeIngressoRQ);
            String dsParametro = disponibilidade.getIngressoPesquisaList().get(0).getIngresso().getDsParametro();
            assertNotNull(dsParametro);
            System.out.println("DsParametro -----> " + dsParametro);
        } catch (ErrorException e) {
            e.printStackTrace();
        }

    }

}

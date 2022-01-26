package br.com.infotera.it.novaxs;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRS;
import br.com.infotera.it.novaxs.model.BuyToBillForRS;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import br.com.infotera.it.novaxs.model.Person;
import br.com.infotera.it.novaxs.model.Product;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @Author Lucas
 **/

@SpringBootTest
public class TestesDeConvercao {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Gson gson;

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

    @Test
    public void testeConverterJsonParaPerson(){
        Person person;
        try {
            person = objectMapper.readValue(JsonsTeste.jsonPerson(), Person.class);
            assertNotNull(person);
            System.out.println("Teste testeConverterJsonParaPerson -> " + person.getName());
            System.out.println("Teste testeConverterJsonParaPerson -> " + person.getCpf());
            System.out.println("Teste testeConverterJsonParaPerson -> " + person.getCellPhone());
            System.out.println("Teste testeConverterJsonParaPerson -> " + person.getHomePhone());


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

}

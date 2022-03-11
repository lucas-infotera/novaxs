package br.com.infotera.it.novaxs;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.WSDocumentoTipoEnum;
import br.com.infotera.common.enumerator.WSPaxTipoEnum;
import br.com.infotera.common.enumerator.WSSexoEnum;
import br.com.infotera.common.enumerator.WSTelefoneTipoEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.model.*;
import br.com.infotera.it.novaxs.services.ConfirmarWS;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @Author Lucas
 **/
@SpringBootTest
public class TestesEmDevNovaxs {

    @Autowired
    private DisponibilidadeWS disponibilidadeWS;

    @Autowired
    private ConfirmarWS confirmarWS;

    @Autowired
    private Gson gson;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void teste1DeAnotacoesNotNullValid() {

        Person personAsString = new Person().setName("Lucas").setCpf(null);

        Assertions.assertThrows(ConstraintViolationException.class, () -> UtilsWS.validator(personAsString));

    }

    @Test
    public void teste2DeAnotacoesNotNullValid_TesteMontaPersonAsString_DadosDoComprador() {

        WSContato contato = new WSContato("lucas", "teste@gmalil", new WSTelefone("11", "tsaedfad", WSTelefoneTipoEnum.CELULAR));
        contato.setDocumento(new WSDocumento(WSDocumentoTipoEnum.CPF));
        WSReserva reserva = new WSReserva();
        reserva.setContato(contato);

        Assertions.assertThrows(ErrorException.class, () -> UtilsWS.montaPersonAsStringDadosDoComprador(reserva));

    }

    @Test
    public void testeMontagemDataParaOInfotravel() {
        Date date = null;
        try {
            date = UtilsWS.montaDataInfotravel("10/02/2022");
        } catch (ErrorException e) {
            e.printStackTrace();
        }

        Assertions.assertNotNull(date);
    }

    @Test
    public void testeMontaRangeDatas() {
        WSDisponibilidadeIngressoRQ wsDisponibilidadeIngressoRQ = new WSDisponibilidadeIngressoRQ();
        wsDisponibilidadeIngressoRQ.setDtInicio(new Date());
        wsDisponibilidadeIngressoRQ.setDtFim(Utils.addDias(new Date(), 4));

        List<Date> dates = disponibilidadeWS.montaRangedatasPesquisa(wsDisponibilidadeIngressoRQ);

        Assertions.assertNotNull(dates);

        for (Date date : dates) {
            System.out.println(date.toString());
        }

    }

    @Test
    public void fluxo1_testaConfirmarRQ() {
        WSReservaRQ wsRQ = gson.fromJson(JsonsTeste.fluxo1_montaConfirmarRQ(), WSReservaRQ.class);

        try {
            WSReservaRS confirmar = confirmarWS.confirmar(wsRQ);
        } catch (ErrorException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void fluxo1_testachamachamaReservarBillForRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);
        BuyToBillForRS buyToBillForRS = null;
        try {
            buyToBillForRS = objectMapper.readValue(JsonsTeste.fluxo1_montaJsonBuyToBillForRSTesteRetorno(), BuyToBillForRS.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        BillForRS billForRS;
        try {
            billForRS = confirmarWS.chamaReservarBillForRQ(integrador, buyToBillForRS);
            assertNotNull(billForRS);
        } catch (ErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fluxo1_testachamaObtencaoDeListaDeAcessoGetAccessListRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);
        BuyToBillForRS buyToBillForRS = null;
        try {
            buyToBillForRS = objectMapper.readValue(JsonsTeste.fluxo1_montaJsonBuyToBillForRSTesteRetorno(), BuyToBillForRS.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        List<GetAccessListRS> getAccessListRS;
        try {
            getAccessListRS = confirmarWS.chamaObtencaoDeListaDeAcessoGetAccessListRQ(integrador, buyToBillForRS);
            System.out.println(getAccessListRS.toString());
            assertNotNull(getAccessListRS);
        } catch (ErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fluxo1_testeSetAcessListRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);
        BuyToBillForRS buyToBillForRS = null;
        List<GetAccessListRS> getAccessListRS = null;
        WSReservaRQ wsRQ = gson.fromJson(JsonsTeste.fluxo1_montaConfirmarRQ(), WSReservaRQ.class);
        try {
            buyToBillForRS = objectMapper.readValue(JsonsTeste.fluxo1_montaJsonBuyToBillForRSTesteRetorno(), BuyToBillForRS.class);
            GetAccessListRS[] getAccessArray = objectMapper.readValue(JsonsTeste.fluxo1_montaGetAcessListRS(), GetAccessListRS[].class);
            getAccessListRS = Arrays.asList(getAccessArray);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            confirmarWS.chamaMontagemListaDeAcessoSetAccessListRQ(integrador, wsRQ.getReserva(), buyToBillForRS, getAccessListRS);
            System.out.println(getAccessListRS.toString());
            assertNotNull(getAccessListRS);
        } catch (ErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fluxo1_testeSGetVoucherRQ() {
        WSIntegrador integrador = gson.fromJson(JsonsTeste.montaIntegrador(), WSIntegrador.class);
        BuyToBillForRS buyToBillForRS = null;
        List<GetAccessListRS> getAccessListRS = null;
        WSReservaRQ wsRQ = gson.fromJson(JsonsTeste.fluxo1_montaConfirmarRQ(), WSReservaRQ.class);
        BillForRS billForRS = null;
        try {
            buyToBillForRS = objectMapper.readValue(JsonsTeste.fluxo1_montaJsonBuyToBillForRSTesteRetorno(), BuyToBillForRS.class);
            GetAccessListRS[] getAccessArray = objectMapper.readValue(JsonsTeste.fluxo1_montaGetAcessListRS(), GetAccessListRS[].class);
            getAccessListRS = Arrays.asList(getAccessArray);
            billForRS = objectMapper.readValue(JsonsTeste.fluxo1_montaJsonReturnBillForTesteConfirmar(), BillForRS.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            VoucherRS voucherRS = confirmarWS.chamaObtencaodeVoucherVoucherRQ(integrador, billForRS);
            System.out.println(getAccessListRS.toString());
            assertNotNull(getAccessListRS);
        } catch (ErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testemontaListSetAccessListRQ() {
        List<GetAccessListRS> getAccessListRS = null;
        try {
            getAccessListRS = Arrays.asList(objectMapper.readValue(JsonsTeste.json_TesteListAcessPersonsRS(), GetAccessListRS[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        List<WSReservaNome> reservaNomeList = new ArrayList<>();

        reservaNomeList.add(new WSReservaNome("Joao", "", WSPaxTipoEnum.ADT, new Date(), 40, WSSexoEnum.MASCULINO));
        reservaNomeList.add(new WSReservaNome("Lucas", "", WSPaxTipoEnum.ADT, new Date(), 40, WSSexoEnum.MASCULINO));

        ListSetAccessListRQ listSetAccessListRQ = confirmarWS.montaListSetAccessListRQ(getAccessListRS, reservaNomeList);

        assertNotNull(listSetAccessListRQ);
    }


    @Test
    public void teste2montaListSetAccessListRQ() {
        List<GetAccessListRS> getAccessListRS = null;
        try {
            getAccessListRS = Arrays.asList(objectMapper.readValue(JsonsTeste.json2_TesteListAcessPersonRS(), GetAccessListRS[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        List<WSReservaNome> reservaNomeList = new ArrayList<>();

        reservaNomeList.add(new WSReservaNome("Joao", "", WSPaxTipoEnum.ADT, new Date(), 40, WSSexoEnum.MASCULINO));
        reservaNomeList.add(new WSReservaNome("Lucas", "", WSPaxTipoEnum.ADT, new Date(), 40, WSSexoEnum.MASCULINO));

        ListSetAccessListRQ listSetAccessListRQ = confirmarWS.montaListSetAccessListRQ(getAccessListRS, reservaNomeList);

        try {
            System.out.println(objectMapper.writeValueAsString(listSetAccessListRQ));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertNotNull(listSetAccessListRQ);
    }
}

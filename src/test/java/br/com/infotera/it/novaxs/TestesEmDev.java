package br.com.infotera.it.novaxs;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.WSDocumentoTipoEnum;
import br.com.infotera.common.enumerator.WSTelefoneTipoEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.model.Person;
import br.com.infotera.it.novaxs.services.ConfirmarWS;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

/**
 * @Author Lucas
 **/
@SpringBootTest
public class TestesEmDev {

    @Autowired
    private DisponibilidadeWS disponibilidadeWS;

    @Autowired
    private ConfirmarWS confirmarWS;

    @Autowired
    private Gson gson;

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
    public void testaConfirmarRQ(){
        WSReservaRQ wsRQ = gson.fromJson(JsonsTeste.montaConfirmarRQ(), WSReservaRQ.class);

        try {
            WSReservaRS confirmar = confirmarWS.confirmar(wsRQ);
        } catch (ErrorException e) {
            e.printStackTrace();
        }


    }


}

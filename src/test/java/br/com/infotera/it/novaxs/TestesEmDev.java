package br.com.infotera.it.novaxs;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSContato;
import br.com.infotera.common.WSDocumento;
import br.com.infotera.common.WSTelefone;
import br.com.infotera.common.enumerator.WSDocumentoTipoEnum;
import br.com.infotera.common.enumerator.WSTelefoneTipoEnum;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.model.Person;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
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

    @Test
    public void teste1DeAnotacoesNotNullValid() {

        Person personAsString = new Person().setName("Lucas").setCpf(null);

        Assertions.assertThrows(ConstraintViolationException.class, () -> UtilsWS.validator(personAsString));

    }

    @Test
    public void teste2DeAnotacoesNotNullValid_TesteMontaPersonAsString_DadosDoComprador() {

        WSContato contato = new WSContato("lucas", "teste@gmalil", new WSTelefone("11", "tsaedfad", WSTelefoneTipoEnum.CELULAR));
        contato.setDocumento(new WSDocumento(WSDocumentoTipoEnum.CPF));


        Assertions.assertThrows(ErrorException.class, () -> UtilsWS.montaPersonAsStringDadosDoComprador(contato));

    }

    @Test
    public void testeMontagemDataParaOInfotravel() {
        Date date = UtilsWS.montaDataInfotravel("10/02/2022");

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


}

package br.com.infotera.it.novaxs;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSContato;
import br.com.infotera.common.WSDocumento;
import br.com.infotera.common.WSTelefone;
import br.com.infotera.common.enumerator.WSDocumentoTipoEnum;
import br.com.infotera.common.enumerator.WSTelefoneTipoEnum;
import br.com.infotera.it.novaxs.model.Person;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.Date;

/**
 * @Author Lucas
 **/
@SpringBootTest
public class TestesEmDev {

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
    public void testeMontagemDataParaOInfotravel(){
        Date date = UtilsWS.montaDataInfotravel("10/02/2022");

        Assertions.assertNotNull(date);
    }


}

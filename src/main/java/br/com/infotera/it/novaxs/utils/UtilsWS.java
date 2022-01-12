package br.com.infotera.it.novaxs.utils;

import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Lucas
 **/

@Component
public class UtilsWS {

    private static ObjectMapper objectMapper;

    @Autowired
    public static void setObjectMapper(ObjectMapper objectMapper) {
        UtilsWS.objectMapper = objectMapper;
    }


    public static void verificaErro(WSIntegrador integrador, Object responseEntity){

    }


    public static List<WSPolitica> montaPoliticaList(GetProductsByDateRS productsByDateRS) {
        return null;
    }
}

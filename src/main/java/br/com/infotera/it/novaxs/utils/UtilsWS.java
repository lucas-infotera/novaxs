package br.com.infotera.it.novaxs.utils;

import br.com.infotera.common.WSIntegrador;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}

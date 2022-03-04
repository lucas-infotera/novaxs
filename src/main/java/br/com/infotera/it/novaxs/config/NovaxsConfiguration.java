package br.com.infotera.it.novaxs.config;

import br.com.infotera.common.circuito.WSCircuitoServico;
import br.com.infotera.common.pagto.WSPagtoForma;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.util.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Author Lucas
 **/

@Configuration
public class NovaxsConfiguration {

    @Bean
    public static Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(WSServico.class, new WSServicoJson());
        gsonBuilder.registerTypeAdapter(WSPolitica.class, new WSPoliticaJson());
        gsonBuilder.registerTypeAdapter(WSPagtoForma.class, new WSPagtoFormaJson());
        gsonBuilder.registerTypeAdapter(WSCircuitoServico.class, new WSCircuitoServicoJson());
        gsonBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                return new JsonPrimitive(sdf.format(src));
            }
        });
        gsonBuilder.disableHtmlEscaping();
        Gson g = gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
        return g;
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(240000);
        return new RestTemplate(factory);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.setTimeZone(TimeZone.getDefault());
        mapper.deactivateDefaultTyping();
        return mapper;
    }


    @Bean
    public Validator validator(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator;
    }


}

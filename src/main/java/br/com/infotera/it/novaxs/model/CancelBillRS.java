package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author Lucas
 **/
public class CancelBillRS {

    Boolean success;

    @JsonProperty("erro")
    String erro;

    public Boolean getSuccess() {
        return success;
    }

    public CancelBillRS setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getErro() {
        return erro;
    }

    public CancelBillRS setErro(String erro) {
        this.erro = erro;
        return this;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .deactivateDefaultTyping()
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

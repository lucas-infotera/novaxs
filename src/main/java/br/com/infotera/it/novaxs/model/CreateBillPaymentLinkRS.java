package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author Lucas
 **/
public class CreateBillPaymentLinkRS extends NovaxsTemplateforModels {

    @JsonProperty("id")
    public String id;
    @JsonProperty("url")
    public String url;
    @JsonProperty("valor")
    public String valor;
    @JsonProperty("erro")
    public String erro;

    public String getErro() {
        return erro;
    }

    public CreateBillPaymentLinkRS setErro(String erro) {
        this.erro = erro;
        return this;
    }

    public String getId() {
        return id;
    }

    public CreateBillPaymentLinkRS setId(String id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CreateBillPaymentLinkRS setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getValor() {
        return valor;
    }

    public CreateBillPaymentLinkRS setValor(String valor) {
        this.valor = valor;
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

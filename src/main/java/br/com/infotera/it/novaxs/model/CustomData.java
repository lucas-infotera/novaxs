package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author Lucas
 **/
public class CustomData extends NovaxsTemplateforModels{

    @JsonProperty("IdExterno")
    public String idExterno;
    @JsonProperty("Obs")
    public String obs;
    @JsonProperty("ProductName")
    public String productName;
    @JsonProperty("productId")
    public int productId;
    @JsonProperty("entryId")
    public String entryId;




    public String getProductName() {
        return productName;
    }

    public CustomData setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public int getProductId() {
        return productId;
    }

    public CustomData setProductId(int productId) {
        this.productId = productId;
        return this;
    }

    public String getEntryId() {
        return entryId;
    }

    public CustomData setEntryId(String entryId) {
        this.entryId = entryId;
        return this;
    }

    public String getIdExterno() {
        return idExterno;
    }

    public CustomData setIdExterno(String idExterno) {
        this.idExterno = idExterno;
        return this;
    }

    public String getObs() {
        return obs;
    }

    public CustomData setObs(String obs) {
        this.obs = obs;
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

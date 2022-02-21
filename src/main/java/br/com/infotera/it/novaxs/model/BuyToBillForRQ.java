package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author Lucas
 **/
public class BuyToBillForRQ {

    @JsonProperty("token")
    String token;

    @JsonProperty("login")
    String login;

    @JsonProperty("password")
    String password;

    @JsonProperty("method")
    String method = "buyToBillFor";

    @JsonProperty("customData")
    String customData;
    @JsonProperty("personAsString")
    String personAsString;
    @JsonProperty("productsArray")
    String productsArray;

    public BuyToBillForRQ() {
    }


    public BuyToBillForRQ(@NotNull CredenciaisNovaxsRQ credenciaisNovaXS) {
        this.setLogin(credenciaisNovaXS.getLogin());
        this.setPassword(credenciaisNovaXS.getPassword());
        this.setToken(credenciaisNovaXS.getToken());
    }

    public String getToken() {
        return token;
    }

    public BuyToBillForRQ setToken(String token) {
        this.token = token;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public BuyToBillForRQ setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public BuyToBillForRQ setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public String getCustomData() {
        return customData;
    }

    public BuyToBillForRQ setCustomData(String customData) {
        this.customData = customData;
        return this;
    }

    public BuyToBillForRQ setCustomData(CustomData customData) {
        this.customData = customData.toString();
        return this;
    }

    public String getPersonAsString() {
        return personAsString;
    }

    public BuyToBillForRQ setPersonAsString(String personAsString) {
        this.personAsString = personAsString;
        return this;
    }

    public BuyToBillForRQ setPersonAsString(Person personAsString) {
        this.personAsString = personAsString.toString();
        return this;
    }

    public String getProductsArray() {
        return productsArray;
    }

    public BuyToBillForRQ setProductsArray(String productsArray) {
        this.productsArray = productsArray;
        return this;
    }

    public BuyToBillForRQ setProductsArray(ProductsArray productsArray) {
        this.productsArray = productsArray.toString();
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

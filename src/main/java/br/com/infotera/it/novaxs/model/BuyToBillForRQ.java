package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public String getPersonAsString() {
        return personAsString;
    }

    public BuyToBillForRQ setPersonAsString(String personAsString) {
        this.personAsString = personAsString;
        return this;
    }

    public String getProductsArray() {
        return productsArray;
    }

    public BuyToBillForRQ setProductsArray(String productsArray) {
        this.productsArray = productsArray;
        return this;
    }

}

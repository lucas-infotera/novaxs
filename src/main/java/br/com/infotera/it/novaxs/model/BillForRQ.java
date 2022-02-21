package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author Lucas
 **/

public class BillForRQ extends NovaxsTemplateforModels {


    @JsonProperty("token")
    String token;

    @JsonProperty("login")
    String login;

    @JsonProperty("password")
    String password;
    @JsonProperty("method")
    String method = "billFor";

    @JsonProperty("bill")
    String bill;

    public BillForRQ() {
    }

    public BillForRQ(CredenciaisNovaxsRQ credenciaisNovaXS) {
        this.setLogin(credenciaisNovaXS.getLogin());
        this.setPassword(credenciaisNovaXS.getPassword());
        this.setToken(credenciaisNovaXS.getToken());
    }

    public String getToken() {
        return token;
    }

    public BillForRQ setToken(String token) {
        this.token = token;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public BillForRQ setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public BillForRQ setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public String getBill() {
        return bill;
    }

    public BillForRQ setBill(String bill) {
        this.bill = bill;
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

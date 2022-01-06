package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Author Lucas
 **/

public class GetProductsByDateRQ {

    @JsonProperty("token")
    String token;
    @JsonProperty("login")
    String login;
    @JsonProperty("password")
    String password;
    @JsonProperty("method")
    String method = "getProductsByDate";
    @JsonProperty("date")
    String date;

    public String getToken() {
        return token;
    }

    public GetProductsByDateRQ setToken(String token) {
        this.token = token;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public GetProductsByDateRQ setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public GetProductsByDateRQ setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public String getDate() {
        return date;
    }

    public GetProductsByDateRQ setDate(String date) {
        this.date = date;
        return this;
    }

}

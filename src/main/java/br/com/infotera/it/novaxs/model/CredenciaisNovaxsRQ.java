package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @Author Lucas
 **/
public class CredenciaisNovaxsRQ {

    @NotNull(message = "token é obrigatorio")
    @JsonProperty("token")
    String token;

    @NotNull(message = "login é obrigatorio")
    @JsonProperty("login")
    String login;

    @NotNull(message = "password é obrigatório")
    @JsonProperty("password")
    String password;

    public CredenciaisNovaxsRQ(String login,  String password) {
        this.login = login;
        this.password = password;
    }

    public CredenciaisNovaxsRQ(String login,  String password, String token) {
        this.token = token;
        this.login = login;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public CredenciaisNovaxsRQ setToken(String token) {
        this.token = token;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public CredenciaisNovaxsRQ setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CredenciaisNovaxsRQ setPassword(String password) {
        this.password = password;
        return this;
    }
}

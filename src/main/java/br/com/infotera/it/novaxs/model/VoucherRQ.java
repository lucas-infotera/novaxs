package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Author Lucas
 **/
public class VoucherRQ {
    @JsonProperty("token")
    String token;
    @JsonProperty("login")
    String login;
    @JsonProperty("password")
    String password;
    @JsonProperty("voucher")
    String voucher;
    @JsonProperty("method")
    String method = "receiptAsByte";

    public String getVoucher() {
        return voucher;
    }

    public VoucherRQ setVoucher(String voucher) {
        this.voucher = voucher;
        return this;
    }

    public String getToken() {
        return token;
    }

    public VoucherRQ setToken(String token) {
        this.token = token;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public VoucherRQ setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public String getPassword() {
        return password;
    }

    public VoucherRQ setPassword(String password) {
        this.password = password;
        return this;
    }
}

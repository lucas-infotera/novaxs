package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author Lucas
 **/
public class BillForRS {

    @JsonProperty("valorFaturado")
    public String valorFaturado;
    @JsonProperty("voucher")
    public String voucher;
    @JsonProperty("localizador")
    public String localizador;
    @JsonProperty("token")
    public String token;
    @JsonProperty("erro")
    public String erro;

    public String getErro() {
        return erro;
    }

    public BillForRS setErro(String erro) {
        this.erro = erro;
        return this;
    }

    public String getValorFaturado() {
        return valorFaturado;
    }

    public BillForRS setValorFaturado(String valorFaturado) {
        this.valorFaturado = valorFaturado;
        return this;
    }

    public String getVoucher() {
        return voucher;
    }

    public BillForRS setVoucher(String voucher) {
        this.voucher = voucher;
        return this;
    }

    public String getLocalizador() {
        return localizador;
    }

    public BillForRS setLocalizador(String localizador) {
        this.localizador = localizador;
        return this;
    }

    public String getToken() {
        return token;
    }

    public BillForRS setToken(String token) {
        this.token = token;
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

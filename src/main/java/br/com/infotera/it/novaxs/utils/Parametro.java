package br.com.infotera.it.novaxs.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author Lucas
 **/
public class Parametro {

    public String cd;

    public String dt;

    public String horario;

    public String nomeIngresso;

    public String cdModalidade;

    public String nomeModalidade;

    public String voucher;

    public String tokenVoucher;

    public String getVoucher() {
        return voucher;
    }

    public Parametro setVoucher(String voucher) {
        this.voucher = voucher;
        return this;
    }

    public String getTokenVoucher() {
        return tokenVoucher;
    }

    public Parametro setTokenVoucher(String tokenVoucher) {
        this.tokenVoucher = tokenVoucher;
        return this;
    }

    public String getCdModalidade() {
        return cdModalidade;
    }

    public Parametro setCdModalidade(String cdModalidade) {
        this.cdModalidade = cdModalidade;
        return this;
    }

    public String getNomeModalidade() {
        return nomeModalidade;
    }

    public Parametro setNomeModalidade(String nomeModalidade) {
        this.nomeModalidade = nomeModalidade;
        return this;
    }

    public String getNomeIngresso() {
        return nomeIngresso;
    }

    public Parametro setNomeIngresso(String nomeIngresso) {
        this.nomeIngresso = nomeIngresso;
        return this;
    }

    public String getHorario() {
        return horario;
    }

    public Parametro setHorario(String horario) {
        this.horario = horario;
        return this;
    }

    public String getCd() {
        return cd;
    }

    public Parametro setCd(String cd) {
        this.cd = cd;
        return this;
    }

    public String getDt() {
        return dt;
    }

    public Parametro setDt(String dt) {
        this.dt = dt;
        return this;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                    .deactivateDefaultTyping()
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

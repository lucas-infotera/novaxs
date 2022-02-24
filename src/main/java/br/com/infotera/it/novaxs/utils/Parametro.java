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

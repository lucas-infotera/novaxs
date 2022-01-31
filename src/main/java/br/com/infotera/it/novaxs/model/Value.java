package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Value extends NovaxsTemplateforModels{

    @JsonProperty("first")
    public String first;
    @JsonProperty("second")
    public String second;
    @JsonProperty("hashCode")
    public Integer hashCode;
    @JsonProperty("show")
    public String show;
    @JsonProperty("value")
    public String value;

    public String getValue() {
        return value;
    }

    public Value setValue(String value) {
        this.value = value;
        return this;
    }

    public String getShow() {
        return show;
    }

    public Value setShow(String show) {
        this.show = show;
        return this;
    }

    public String getFirst() {
        return first;
    }

    public Value setFirst(String first) {
        this.first = first;
        return this;
    }

    public String getSecond() {
        return second;
    }

    public Value setSecond(String second) {
        this.second = second;
        return this;
    }

    public Integer getHashCode() {
        return hashCode;
    }

    public Value setHashCode(Integer hashCode) {
        this.hashCode = hashCode;
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

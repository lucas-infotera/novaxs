package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Value {

    @JsonProperty("first")
    public String first;
    @JsonProperty("second")
    public String second;
    @JsonProperty("hashCode")
    public Integer hashCode;

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

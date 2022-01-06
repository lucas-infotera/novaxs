package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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

}

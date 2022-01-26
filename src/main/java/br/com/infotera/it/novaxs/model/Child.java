package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Child {

    @JsonProperty("path")
    public String path;
    @JsonProperty("date")
    public String date;

    public String getDate() {
        return date;
    }

    public Child setDate(String date) {
        this.date = date;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Child setPath(String path) {
        this.path = path;
        return this;
    }

}

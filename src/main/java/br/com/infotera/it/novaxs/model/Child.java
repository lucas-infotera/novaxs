package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Child extends NovaxsTemplateforModels {

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

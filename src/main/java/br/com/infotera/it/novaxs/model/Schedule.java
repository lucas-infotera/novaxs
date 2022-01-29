package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Schedule {

    @JsonProperty("path")
    public String path;
    @JsonProperty("schedule")
    public String schedule;
    @JsonProperty("available")
    public String available;

    public String getPath() {
        return path;
    }

    public Schedule setPath(String path) {
        this.path = path;
        return this;
    }

    public String getSchedule() {
        return schedule;
    }

    public Schedule setSchedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    public String getAvailable() {
        return available;
    }

    public Schedule setAvailable(String available) {
        this.available = available;
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

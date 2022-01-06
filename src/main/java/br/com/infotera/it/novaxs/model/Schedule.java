package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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


}

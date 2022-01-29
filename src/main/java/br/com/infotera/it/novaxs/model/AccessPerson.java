package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AccessPerson {

    @JsonProperty("internalId")
    public Integer internalId;
    @JsonProperty("name")
    public String name;
    @JsonProperty("document")
    public String document;
    @JsonProperty("birth")
    public String birth;
    @JsonProperty("itemIdentificator")
    public String itemIdentificator;

    public Integer getInternalId() {
        return internalId;
    }

    public AccessPerson setInternalId(Integer internalId) {
        this.internalId = internalId;
        return this;
    }

    public String getName() {
        return name;
    }

    public AccessPerson setName(String name) {
        this.name = name;
        return this;
    }

    public String getDocument() {
        return document;
    }

    public AccessPerson setDocument(String document) {
        this.document = document;
        return this;
    }

    public String getBirth() {
        return birth;
    }

    public AccessPerson setBirth(String birth) {
        this.birth = birth;
        return this;
    }

    public String getItemIdentificator() {
        return itemIdentificator;
    }

    public AccessPerson setItemIdentificator(String itemIdentificator) {
        this.itemIdentificator = itemIdentificator;
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

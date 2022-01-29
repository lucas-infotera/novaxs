package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class GetAccessListRS {

    @JsonProperty("accessPersons")
    public ArrayList<AccessPerson> accessPersons;
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("internalId")
    public Integer internalId;
    @JsonProperty("trash")
    public Boolean trash;
    @JsonProperty("frozen")
    public Boolean frozen;
    @JsonProperty("inHistory")
    public Boolean inHistory;
    @JsonProperty("lastVersion")
    public Boolean lastVersion;
    @JsonProperty("customData")
    public CustomData customData;
    @JsonProperty("lazy")
    public Boolean lazy;

    @JsonProperty("erro")
    public String erro;

    public String getErro() {
        return erro;
    }

    public GetAccessListRS setErro(String erro) {
        this.erro = erro;
        return this;
    }

    public ArrayList<AccessPerson> getAccessPersons() {
        return accessPersons;
    }

    public GetAccessListRS setAccessPersons(ArrayList<AccessPerson> accessPersons) {
        this.accessPersons = accessPersons;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public GetAccessListRS setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getInternalId() {
        return internalId;
    }

    public GetAccessListRS setInternalId(Integer internalId) {
        this.internalId = internalId;
        return this;
    }

    public Boolean getTrash() {
        return trash;
    }

    public GetAccessListRS setTrash(Boolean trash) {
        this.trash = trash;
        return this;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public GetAccessListRS setFrozen(Boolean frozen) {
        this.frozen = frozen;
        return this;
    }

    public Boolean getInHistory() {
        return inHistory;
    }

    public GetAccessListRS setInHistory(Boolean inHistory) {
        this.inHistory = inHistory;
        return this;
    }

    public Boolean getLastVersion() {
        return lastVersion;
    }

    public GetAccessListRS setLastVersion(Boolean lastVersion) {
        this.lastVersion = lastVersion;
        return this;
    }

    public CustomData getCustomData() {
        return customData;
    }

    public GetAccessListRS setCustomData(CustomData customData) {
        this.customData = customData;
        return this;
    }

    public Boolean getLazy() {
        return lazy;
    }

    public GetAccessListRS setLazy(Boolean lazy) {
        this.lazy = lazy;
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

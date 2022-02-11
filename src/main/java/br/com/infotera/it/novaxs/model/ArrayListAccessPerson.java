package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lucas
 **/

public class ArrayListAccessPerson extends NovaxsTemplateforModels {

    @JsonProperty("accessPersons")
    public ArrayList<AccessPerson> accessPersonList;
    @JsonProperty("customData")
    public CustomData customData;
    @JsonProperty("id")
    public int id;
    @JsonProperty("internalId")
    public int internalId;
    @JsonProperty("trash")
    public boolean trash;
    @JsonProperty("frozen")
    public boolean frozen;
    @JsonProperty("inHistory")
    public boolean inHistory;
    @JsonProperty("lastVersion")
    public boolean lastVersion;
    @JsonProperty("lazy")
    public boolean lazy;

    public int getId() {
        return id;
    }

    public ArrayListAccessPerson setId(int id) {
        this.id = id;
        return this;
    }

    public int getInternalId() {
        return internalId;
    }

    public ArrayListAccessPerson setInternalId(int internalId) {
        this.internalId = internalId;
        return this;
    }

    public boolean isTrash() {
        return trash;
    }

    public ArrayListAccessPerson setTrash(boolean trash) {
        this.trash = trash;
        return this;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public ArrayListAccessPerson setFrozen(boolean frozen) {
        this.frozen = frozen;
        return this;
    }

    public boolean isInHistory() {
        return inHistory;
    }

    public ArrayListAccessPerson setInHistory(boolean inHistory) {
        this.inHistory = inHistory;
        return this;
    }

    public boolean isLastVersion() {
        return lastVersion;
    }

    public ArrayListAccessPerson setLastVersion(boolean lastVersion) {
        this.lastVersion = lastVersion;
        return this;
    }

    public boolean isLazy() {
        return lazy;
    }

    public ArrayListAccessPerson setLazy(boolean lazy) {
        this.lazy = lazy;
        return this;
    }

    public List<AccessPerson> getAccessPersonList() {
        if (this.accessPersonList == null) {
            this.accessPersonList = new ArrayList<>();
        }
        return accessPersonList;
    }

    public ArrayListAccessPerson setAccessPersonList(ArrayList<AccessPerson> accessPersonList) {
        this.accessPersonList = accessPersonList;
        return this;
    }

    public CustomData getCustomData() {
        return customData;
    }

    public ArrayListAccessPerson setCustomData(CustomData customData) {
        this.customData = customData;
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

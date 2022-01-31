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

public class ListAccessPerson extends NovaxsTemplateforModels{

    @JsonProperty("accessPersons")
    ArrayList<AccessPerson> accessPersonList;

    @JsonProperty("customData")
    CustomData customData;

    public List<AccessPerson> getAccessPersonList() {
        if (this.accessPersonList == null) {
            this.accessPersonList = new ArrayList<>();
        }
        return accessPersonList;
    }

    public ListAccessPerson setAccessPersonList(ArrayList<AccessPerson> accessPersonList) {
        this.accessPersonList = accessPersonList;
        return this;
    }

    public CustomData getCustomData() {
        return customData;
    }

    public ListAccessPerson setCustomData(CustomData customData) {
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

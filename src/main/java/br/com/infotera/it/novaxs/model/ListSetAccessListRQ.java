package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Lucas
 **/

public class ListSetAccessListRQ {

    List<ListAccessPerson> accessPersonList;

    public List<ListAccessPerson> getAccessPersonList() {
        return accessPersonList;
    }

    public ListSetAccessListRQ setAccessPersonList(List<ListAccessPerson> accessPersonList) {
        this.accessPersonList = accessPersonList;
        return this;
    }

    public ListSetAccessListRQ setAccessPersonList(ListAccessPerson[] accessPersonList) {
        this.accessPersonList = Arrays.asList(accessPersonList);
        return this;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .deactivateDefaultTyping()
                    .writeValueAsString(getAccessPersonList());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author Lucas
 **/
public class Person extends NovaxsTemplateforModels {

    @JsonProperty("name")
    public String name;

    @JsonProperty("cpf")
    public String cpf;

    @JsonProperty("homePhone")
    public String homePhone;
    @JsonProperty("cellPhone")
    public String cellPhone;
    @JsonProperty("email")
    public String email;


    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public String getCpf() {
        return cpf;
    }

    public Person setCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public Person setHomePhone(String homePhone) {
        this.homePhone = homePhone;
        return this;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public Person setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Person setEmail(String email) {
        this.email = email;
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

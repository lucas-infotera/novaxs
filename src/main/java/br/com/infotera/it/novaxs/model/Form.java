package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Form extends NovaxsTemplateforModels{

    @JsonProperty("name")
    public String name;
    @JsonProperty("property")
    public String property;
    @JsonProperty("type")
    public String type;
    @JsonProperty("group")
    public String group;
    @JsonProperty("order")
    public Integer order;
    @JsonProperty("per_entry")
    public Boolean per_entry;
    @JsonProperty("product")
    public String product;
    @JsonProperty("product_path")
    public String product_path;
    @JsonProperty("required")
    public Boolean required;
    @JsonProperty("values")
    public List<Value> values;

    public String getName() {
        return name;
    }

    public Form setName(String name) {
        this.name = name;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public Form setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getType() {
        return type;
    }

    public Form setType(String type) {
        this.type = type;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public Form setGroup(String group) {
        this.group = group;
        return this;
    }

    public Integer getOrder() {
        return order;
    }

    public Form setOrder(Integer order) {
        this.order = order;
        return this;
    }

    public Boolean getPer_entry() {
        return per_entry;
    }

    public Form setPer_entry(Boolean per_entry) {
        this.per_entry = per_entry;
        return this;
    }

    public String getProduct() {
        return product;
    }

    public Form setProduct(String product) {
        this.product = product;
        return this;
    }

    public String getProduct_path() {
        return product_path;
    }

    public Form setProduct_path(String product_path) {
        this.product_path = product_path;
        return this;
    }

    public Boolean getRequired() {
        return required;
    }

    public Form setRequired(Boolean required) {
        this.required = required;
        return this;
    }

    public List<Value> getValues() {
        return values;
    }

    public Form setValues(List<Value> values) {
        this.values = values;
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

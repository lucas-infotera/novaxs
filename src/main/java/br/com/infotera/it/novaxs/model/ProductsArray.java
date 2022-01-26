package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;


public class ProductsArray {

    @JsonProperty("path")
    public String path;
    @JsonProperty("amount")
    public String amount;
    @JsonProperty("date")
    public String date;
    @JsonProperty("children")
    public ArrayList<Child> children;

    public String getPath() {
        return path;
    }

    public ProductsArray setPath(String path) {
        this.path = path;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public ProductsArray setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getDate() {
        return date;
    }

    public ProductsArray setDate(String date) {
        this.date = date;
        return this;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public ProductsArray setChildren(ArrayList<Child> children) {
        this.children = children;
        return this;
    }

}

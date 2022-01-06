package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Product {

    @JsonProperty("supplierName")
    public String supplierName;
    @JsonProperty("image")
    public String image;
    @JsonProperty("minAmount")
    public String minAmount;
    @JsonProperty("select_date")
    public Boolean select_date;
    @JsonProperty("amount")
    public String amount;
    @JsonProperty("htmlMemo")
    public String htmlMemo;
    @JsonProperty("available")
    public String available;
    @JsonProperty("memo")
    public String memo;
    @JsonProperty("supplierPath")
    public String supplierPath;
    @JsonProperty("cancellationPolicies")
    public String cancellationPolicies;
    @JsonProperty("type")
    public String type;
    @JsonProperty("additionalLanguagesInfo")
    public List<Object> additionalLanguagesInfo;
    @JsonProperty("path")
    public String path;
    @JsonProperty("definePrecoNaVenda")
    public String definePrecoNaVenda;
    @JsonProperty("schedules")
    public List<Object> schedules;
    @JsonProperty("faixaetaria")
    public String faixaetaria;
    @JsonProperty("name")
    public String name;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("id")
    public String id;
    @JsonProperty("shortName")
    public String shortName;
    @JsonProperty("dates")
    public List<String> dates;

    public String getSupplierName() {
        return supplierName;
    }

    public Product setSupplierName(String supplierName) {
        this.supplierName = supplierName;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Product setImage(String image) {
        this.image = image;
        return this;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public Product setMinAmount(String minAmount) {
        this.minAmount = minAmount;
        return this;
    }

    public Boolean getSelect_date() {
        return select_date;
    }

    public Product setSelect_date(Boolean select_date) {
        this.select_date = select_date;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public Product setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getHtmlMemo() {
        return htmlMemo;
    }

    public Product setHtmlMemo(String htmlMemo) {
        this.htmlMemo = htmlMemo;
        return this;
    }

    public String getAvailable() {
        return available;
    }

    public Product setAvailable(String available) {
        this.available = available;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public Product setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public String getSupplierPath() {
        return supplierPath;
    }

    public Product setSupplierPath(String supplierPath) {
        this.supplierPath = supplierPath;
        return this;
    }

    public String getCancellationPolicies() {
        return cancellationPolicies;
    }

    public Product setCancellationPolicies(String cancellationPolicies) {
        this.cancellationPolicies = cancellationPolicies;
        return this;
    }

    public String getType() {
        return type;
    }

    public Product setType(String type) {
        this.type = type;
        return this;
    }

    public List<Object> getAdditionalLanguagesInfo() {
        return additionalLanguagesInfo;
    }

    public Product setAdditionalLanguagesInfo(List<Object> additionalLanguagesInfo) {
        this.additionalLanguagesInfo = additionalLanguagesInfo;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Product setPath(String path) {
        this.path = path;
        return this;
    }

    public String getDefinePrecoNaVenda() {
        return definePrecoNaVenda;
    }

    public Product setDefinePrecoNaVenda(String definePrecoNaVenda) {
        this.definePrecoNaVenda = definePrecoNaVenda;
        return this;
    }

    public List<Object> getSchedules() {
        return schedules;
    }

    public Product setSchedules(List<Object> schedules) {
        this.schedules = schedules;
        return this;
    }

    public String getFaixaetaria() {
        return faixaetaria;
    }

    public Product setFaixaetaria(String faixaetaria) {
        this.faixaetaria = faixaetaria;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public Product setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getId() {
        return id;
    }

    public Product setId(String id) {
        this.id = id;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public Product setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public List<String> getDates() {
        return dates;
    }

    public Product setDates(List<String> dates) {
        this.dates = dates;
        return this;
    }

}

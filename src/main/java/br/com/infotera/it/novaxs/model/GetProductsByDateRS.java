package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @Author Lucas
 **/
public class GetProductsByDateRS {
    @JsonProperty("supplierName")
    public String supplierName;
    @JsonProperty("image")
    public String image;
    @JsonProperty("minAmount")
    public String minAmount;
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
    @JsonProperty("form")
    public List<Form> form;
    @JsonProperty("definePrecoNaVenda")
    public String definePrecoNaVenda;
    @JsonProperty("custo")
    public Integer custo;
    @JsonProperty("schedules")
    public List<Schedule> schedules;
    @JsonProperty("name")
    public String name;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("id")
    public String id;
    @JsonProperty("shortName")
    public String shortName;
    @JsonProperty("value")
    public String value;
    @JsonProperty("token")
    public String token;
    @JsonProperty("faixaetaria")
    public String faixaetaria;
    @JsonProperty("schedule_type")
    public String schedule_type;
    @JsonProperty("needExtraData")
    public String needExtraData;
    @JsonProperty("products")
    public List<Product> products;
    @JsonProperty("umaPessoaPorCombo")
    public Boolean umaPessoaPorCombo;

    public String getSupplierName() {
        return supplierName;
    }

    public GetProductsByDateRS setSupplierName(String supplierName) {
        this.supplierName = supplierName;
        return this;
    }

    public String getImage() {
        return image;
    }

    public GetProductsByDateRS setImage(String image) {
        this.image = image;
        return this;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public GetProductsByDateRS setMinAmount(String minAmount) {
        this.minAmount = minAmount;
        return this;
    }

    public String getHtmlMemo() {
        return htmlMemo;
    }

    public GetProductsByDateRS setHtmlMemo(String htmlMemo) {
        this.htmlMemo = htmlMemo;
        return this;
    }

    public String getAvailable() {
        return available;
    }

    public GetProductsByDateRS setAvailable(String available) {
        this.available = available;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public GetProductsByDateRS setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public String getSupplierPath() {
        return supplierPath;
    }

    public GetProductsByDateRS setSupplierPath(String supplierPath) {
        this.supplierPath = supplierPath;
        return this;
    }

    public String getCancellationPolicies() {
        return cancellationPolicies;
    }

    public GetProductsByDateRS setCancellationPolicies(String cancellationPolicies) {
        this.cancellationPolicies = cancellationPolicies;
        return this;
    }

    public String getType() {
        return type;
    }

    public GetProductsByDateRS setType(String type) {
        this.type = type;
        return this;
    }

    public List<Object> getAdditionalLanguagesInfo() {
        return additionalLanguagesInfo;
    }

    public GetProductsByDateRS setAdditionalLanguagesInfo(List<Object> additionalLanguagesInfo) {
        this.additionalLanguagesInfo = additionalLanguagesInfo;
        return this;
    }

    public String getPath() {
        return path;
    }

    public GetProductsByDateRS setPath(String path) {
        this.path = path;
        return this;
    }

    public List<Form> getForm() {
        return form;
    }

    public GetProductsByDateRS setForm(List<Form> form) {
        this.form = form;
        return this;
    }

    public String getDefinePrecoNaVenda() {
        return definePrecoNaVenda;
    }

    public GetProductsByDateRS setDefinePrecoNaVenda(String definePrecoNaVenda) {
        this.definePrecoNaVenda = definePrecoNaVenda;
        return this;
    }

    public Integer getCusto() {
        return custo;
    }

    public GetProductsByDateRS setCusto(Integer custo) {
        this.custo = custo;
        return this;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public GetProductsByDateRS setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
        return this;
    }

    public String getName() {
        return name;
    }

    public GetProductsByDateRS setName(String name) {
        this.name = name;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public GetProductsByDateRS setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getId() {
        return id;
    }

    public GetProductsByDateRS setId(String id) {
        this.id = id;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public GetProductsByDateRS setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public String getValue() {
        return value;
    }

    public GetProductsByDateRS setValue(String value) {
        this.value = value;
        return this;
    }

    public String getToken() {
        return token;
    }

    public GetProductsByDateRS setToken(String token) {
        this.token = token;
        return this;
    }

    public String getFaixaetaria() {
        return faixaetaria;
    }

    public GetProductsByDateRS setFaixaetaria(String faixaetaria) {
        this.faixaetaria = faixaetaria;
        return this;
    }

    public String getSchedule_type() {
        return schedule_type;
    }

    public GetProductsByDateRS setSchedule_type(String schedule_type) {
        this.schedule_type = schedule_type;
        return this;
    }

    public String getNeedExtraData() {
        return needExtraData;
    }

    public GetProductsByDateRS setNeedExtraData(String needExtraData) {
        this.needExtraData = needExtraData;
        return this;
    }

    public List<Product> getProducts() {
        return products;
    }

    public GetProductsByDateRS setProducts(List<Product> products) {
        this.products = products;
        return this;
    }

    public Boolean getUmaPessoaPorCombo() {
        return umaPessoaPorCombo;
    }

    public GetProductsByDateRS setUmaPessoaPorCombo(Boolean umaPessoaPorCombo) {
        this.umaPessoaPorCombo = umaPessoaPorCombo;
        return this;
    }
}

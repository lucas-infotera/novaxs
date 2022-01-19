package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Author Lucas
 **/
public class BuyToBillForRS {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("path")
    public String path;
    @JsonProperty("periodo")
    public String periodo;
    @JsonProperty("created")
    public Long created;
    @JsonProperty("dataVenda")
    public String dataVenda;
    @JsonProperty("descritivo")
    public String descritivo;
    @JsonProperty("agencia")
    public String agencia;
    @JsonProperty("amount")
    public Integer amount;
    @JsonProperty("usuario")
    public String usuario;
    @JsonProperty("status")
    public String status;
    @JsonProperty("clientId")
    public String clientId;
    @JsonProperty("clientName")
    public String clientName;
    @JsonProperty("email")
    public String email;
    @JsonProperty("cancelar")
    public String cancelar;
    @JsonProperty("pagamento")
    public String pagamento;
    @JsonProperty("valorPago")
    public Integer valorPago;
    @JsonProperty("valor")
    public Integer valor;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("token")
    public String token;
    @JsonProperty("localizador")
    public String localizador;

    public Integer getId() {
        return id;
    }

    public BuyToBillForRS setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getPath() {
        return path;
    }

    public BuyToBillForRS setPath(String path) {
        this.path = path;
        return this;
    }

    public String getPeriodo() {
        return periodo;
    }

    public BuyToBillForRS setPeriodo(String periodo) {
        this.periodo = periodo;
        return this;
    }

    public Long getCreated() {
        return created;
    }

    public BuyToBillForRS setCreated(Long created) {
        this.created = created;
        return this;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public BuyToBillForRS setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
        return this;
    }

    public String getDescritivo() {
        return descritivo;
    }

    public BuyToBillForRS setDescritivo(String descritivo) {
        this.descritivo = descritivo;
        return this;
    }

    public String getAgencia() {
        return agencia;
    }

    public BuyToBillForRS setAgencia(String agencia) {
        this.agencia = agencia;
        return this;
    }

    public Integer getAmount() {
        return amount;
    }

    public BuyToBillForRS setAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public String getUsuario() {
        return usuario;
    }

    public BuyToBillForRS setUsuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public BuyToBillForRS setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public BuyToBillForRS setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public BuyToBillForRS setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public BuyToBillForRS setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getCancelar() {
        return cancelar;
    }

    public BuyToBillForRS setCancelar(String cancelar) {
        this.cancelar = cancelar;
        return this;
    }

    public String getPagamento() {
        return pagamento;
    }

    public BuyToBillForRS setPagamento(String pagamento) {
        this.pagamento = pagamento;
        return this;
    }

    public Integer getValorPago() {
        return valorPago;
    }

    public BuyToBillForRS setValorPago(Integer valorPago) {
        this.valorPago = valorPago;
        return this;
    }

    public Integer getValor() {
        return valor;
    }

    public BuyToBillForRS setValor(Integer valor) {
        this.valor = valor;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public BuyToBillForRS setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getToken() {
        return token;
    }

    public BuyToBillForRS setToken(String token) {
        this.token = token;
        return this;
    }

    public String getLocalizador() {
        return localizador;
    }

    public BuyToBillForRS setLocalizador(String localizador) {
        this.localizador = localizador;
        return this;
    }
}
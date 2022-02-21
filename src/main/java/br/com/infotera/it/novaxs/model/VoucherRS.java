package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author Lucas
 **/


public class VoucherRS {

    public String endpointVoucher;

    public byte[] voucher;
    @JsonProperty("erro")
    String erro;

    public String getEndpointVoucher() {
        return endpointVoucher;
    }

    public VoucherRS setEndpointVoucher(String endpointVoucher) {
        this.endpointVoucher = endpointVoucher;
        return this;
    }

    public String getErro() {
        return erro;
    }

    public VoucherRS setErro(String erro) {
        this.erro = erro;
        return this;
    }

    public byte[] getVoucher() {
        return voucher;
    }

    public VoucherRS setVoucher(byte[] voucher) {
        this.voucher = voucher;
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

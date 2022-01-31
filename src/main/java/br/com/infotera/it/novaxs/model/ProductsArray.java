package br.com.infotera.it.novaxs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProductsArray extends NovaxsTemplateforModels{

    List<Product> productsArray;

    public List<Product> getProductsArray() {
        if (this.productsArray == null){
            this.productsArray = new ArrayList<>();
        }
        return productsArray;
    }

    public ProductsArray setProductsArray(List<Product> productsArray) {
        this.productsArray = productsArray;
        return this;
    }

    public ProductsArray setProductsArray(Product[] productsArray) {
        setProductsArray(Arrays.asList(productsArray));
        return this;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .deactivateDefaultTyping()
                    .writeValueAsString(getProductsArray());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}

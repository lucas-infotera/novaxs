package br.com.infotera.it.novaxs.client;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.it.novaxs.model.GetProductsByDateRQ;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Lucas
 **/

@Component
public class NovaxsClient {

    @Autowired
    private RESTClient restClient;

    public List<GetProductsByDateRS> getProductsByDateRQ(WSIntegrador integrador, GetProductsByDateRQ getProductsByDateRQ) throws ErrorException {

        List<GetProductsByDateRS> result = null;
        integrador.setDsMetodo("getProductsByDate");
        try {

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("token", getProductsByDateRQ.getToken());
            requestBody.add("login", getProductsByDateRQ.getLogin());
            requestBody.add("password",getProductsByDateRQ.getPassword());
            requestBody.add("method", getProductsByDateRQ.getMethod());
            requestBody.add("date", getProductsByDateRQ.getDate());

            result = Arrays.asList(restClient.sendReceive(integrador, requestBody, HttpMethod.POST, "getProductsByDate", GetProductsByDateRS[].class));
            UtilsWS.verificaErro(integrador, result);

        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.OK);
            throw new ErrorException(integrador, NovaxsClient.class, "getProductsByDate", WSMensagemErroEnum.SDI, ex.getMessage(), WSIntegracaoStatusEnum.OK, ex);
        }
        return result;
    }

}

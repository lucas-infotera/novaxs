package br.com.infotera.it.novaxs.utils;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.BuyToBillForRS;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @Author Lucas
 **/

@Component
public class UtilsWS {

    private static ObjectMapper objectMapper;

    public static String variavelTemporaria = "VariavelDeTeste";

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        UtilsWS.objectMapper = objectMapper;
    }

    public static void verificaErro(WSIntegrador integrador, Object errorResponse) throws ErrorException {

        String dsErro = "";

        if (errorResponse != null) {
            dsErro = montaMensagemDeErro(errorResponse);
            if (errorResponse instanceof ResponseEntity) {
                ResponseEntity<String> response = (ResponseEntity<String>) errorResponse;
                if (response.getStatusCodeValue() != 200) {
                    switch (response.getStatusCodeValue()) {
                        case 400:
                            throw new ErrorException(integrador, NovaxsClient.class, "verificaErro", WSMensagemErroEnum.GENCONEC, response.getStatusCode() + " - " + response.getStatusCode().getReasonPhrase(), WSIntegracaoStatusEnum.INCONSISTENTE, null);
                        case 403:
                            throw new ErrorException(integrador, NovaxsClient.class, "verificaErro", WSMensagemErroEnum.GENCONEC, response.getStatusCode() + " - " + response.getStatusCode().getReasonPhrase(), WSIntegracaoStatusEnum.INCONSISTENTE, null);
                        case 404:
                            throw new ErrorException(integrador, NovaxsClient.class, "verificaErro", WSMensagemErroEnum.GENCONEC, response.getStatusCode() + " - " + response.getStatusCode().getReasonPhrase(), WSIntegracaoStatusEnum.INCONSISTENTE, null);
                        case 500:
                            throw new ErrorException(integrador, NovaxsClient.class, "verificaErro", WSMensagemErroEnum.GENCONEC, response.getStatusCode() + " - " + response.getStatusCode().getReasonPhrase(), WSIntegracaoStatusEnum.NEGADO, null);
                        case 504:
                            String dsMensagem = response.getStatusCode().getReasonPhrase();
                            if (dsMensagem.contains("Timeout")) {
                                throw new ErrorException(integrador, NovaxsClient.class, "verificaErro", WSMensagemErroEnum.GENCONEC, response.getStatusCode() + " - " + dsMensagem + " Favor consultar o fornecedor", WSIntegracaoStatusEnum.INCONSISTENTE, null);
                            } else {
                                throw new ErrorException(integrador, NovaxsClient.class, "verificaErro", WSMensagemErroEnum.GENCONEC, response.getStatusCode() + " - " + dsMensagem, WSIntegracaoStatusEnum.NEGADO, null);
                            }
                        default:
                            throw new ErrorException(integrador, NovaxsClient.class, "verificaErro", WSMensagemErroEnum.GENCONEC, response.getStatusCode() + " - " + response.getStatusCode().getReasonPhrase(), WSIntegracaoStatusEnum.INCONSISTENTE, null);
                    }
                }
            }
        }

        if (!dsErro.equals("")) {
            throw new ErrorException(integrador, NovaxsClient.class, "VerificaErro", WSMensagemErroEnum.GENCONEC, dsErro, WSIntegracaoStatusEnum.NEGADO, null);
        }
    }

    private static String montaMensagemDeErro(Object errorResponse) throws ErrorException {
        String errorsType = "";

        try {
            if (errorResponse instanceof GetProductsByDateRS) {
                GetProductsByDateRS getProductsByDateRS = (GetProductsByDateRS) errorResponse;
                if (getProductsByDateRS.getErro() != null) {
                    errorsType = getProductsByDateRS.getErro();
                }
            }
            if (errorResponse instanceof BuyToBillForRS) {
                BuyToBillForRS buyToBillForRS = (BuyToBillForRS) errorResponse;
                if (buyToBillForRS.getErro() != null) {
                    errorsType = buyToBillForRS.getErro();
                }
            }
        } catch (Exception e) {
            throw new ErrorException("Erro a o montar ErrorResponse");
        }

        return errorsType;
    }


    public static List<WSPolitica> montaPoliticaList(GetProductsByDateRS productsByDateRS) throws ErrorException {
        return null;
    }
}

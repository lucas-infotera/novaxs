package br.com.infotera.it.novaxs.client;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.enumerator.WSAmbienteEnum;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSIntegradorLogTipoEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @Author Lucas
 **/

@Service
public class RESTClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T sendReceive(WSIntegrador integrador, Object request, HttpMethod httpMethod, String method, Class<T> retorno) throws ErrorException {
        String auxEndpoint = null;
        ResponseEntity<String> responseEntity = null;
        Long tempoInicio = System.currentTimeMillis();
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout((integrador.getTimeoutSegundos() * 1000));

        try {

            auxEndpoint = montaEnvironmentUri(integrador) + "/" + method;
            HttpEntity<String> entity = new HttpEntity(request, montaHeader());
            responseEntity = restTemplate.exchange(auxEndpoint, httpMethod, entity, String.class);


        } catch (RestClientException ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, RESTClient.class, "Erro no envio da Requisição HTTP", WSMensagemErroEnum.GENENDPOINT, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        } catch (ErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, RESTClient.class, "Exception no envio da Requisição ", WSMensagemErroEnum.GENENDPOINT, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        } finally {
            UtilsWS.verificaErro(integrador, responseEntity);
            if (responseEntity != null) {
                montaLog(integrador, method, request, responseEntity.getBody(), tempoInicio);
            } else {
                montaLog(integrador, method, request, null, tempoInicio);
            }
        }
        return montaRetorno(integrador, responseEntity, retorno);
    }

    private HttpHeaders montaHeader() {
        HttpHeaders result = new HttpHeaders();
        result.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        result.add("Content-Type", "application/x-www-form-urlencoded");
        return result;
    }

    private <T> T montaRetorno(WSIntegrador integrador, ResponseEntity<String> responseEntity, Class<T> retorno) throws ErrorException {
        Object objResponse = null;
        try {
            objResponse = objectMapper.readValue(responseEntity.getBody(), retorno);
        } catch (JsonProcessingException ex) {
            throw new ErrorException(integrador, RESTClient.class, "montaRetorno", WSMensagemErroEnum.GENCONVERT, "Erro ao converter responseEntity para objeto", WSIntegracaoStatusEnum.INCONSISTENTE, ex);
        }
        return retorno.cast(objResponse);
    }

    private String montaEnvironmentUri(WSIntegrador integrador) throws ErrorException {
        String result = null;
        if (integrador != null) {
            try {
                if (integrador.getDsCredencialList().get(2) != null) {
                    if (WSAmbienteEnum.PRODUCAO.equals(integrador.getAmbiente())) {
                        result = "https://travel3.novaxs.com.br/api/v1/2059/";
                    } else {
                        result = "https://travel3.novaxs.com.br/api/v1/2059/";
                    }
                }
            } catch (Exception ex) {
                throw new ErrorException(integrador, RESTClient.class, "montaEnvironmentUri", WSMensagemErroEnum.GENENDPOINT, "Erro a o montar Ambiente", WSIntegracaoStatusEnum.NEGADO, ex);
            }
        }
        return result;
    }

    private void montaLog(WSIntegrador integrador, String metodo, Object request, Object response, Long tempoInicio) throws ErrorException {
        String logRequest = "";
        String logResponse = "";

        if (integrador != null) {
            try {
                if (!"Hotel.svc/Search".equals(metodo) || integrador.isStGerarLog()) {
                    logRequest = objectMapper.writeValueAsString(request);
                    logResponse = objectMapper.writeValueAsString(response);
                }
                if (Utils.tpAmbiente.equals("H")) {
                    System.out.println(metodo + " --> " + logRequest);
                    System.out.println(metodo + " Response --> " + logResponse);
                }
                Utils.adicionaIntegradorLog(integrador,
                        WSIntegradorLogTipoEnum.JSON,
                        metodo,
                        logRequest,
                        logResponse,
                        Utils.tempoExecucaoSeg(tempoInicio));
            } catch (Exception ex) {
                throw new ErrorException(integrador, RESTClient.class, "montaLog", WSMensagemErroEnum.GENNULO, "Erro a o montar Log", WSIntegracaoStatusEnum.INCONSISTENTE, ex);
            }
        }
    }
}

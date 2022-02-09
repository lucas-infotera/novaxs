package br.com.infotera.it.novaxs.client;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSIntegradorLog;
import br.com.infotera.common.enumerator.WSAmbienteEnum;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSIntegradorLogTipoEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.util.LogWS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.model.CancelBillRS;
import br.com.infotera.it.novaxs.model.VoucherRS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
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
        Object result = null;
        String auxEndpoint = null;
        ResponseEntity<String> responseEntity = null;
        WSIntegradorLog log = new WSIntegradorLog(integrador.getDsAction(), WSIntegradorLogTipoEnum.JSON);
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout((integrador.getTimeoutSegundos() * 1000));
        HttpEntity<String> entity;

        try {
            LogWS.convertRequest(integrador, log, objectMapper, request);
            if (httpMethod.equals(HttpMethod.GET) && integrador.getDsMetodo().equals("voucherRQ")) {
                ResponseEntity<byte[]> voucherNovaXSResponse = null;
                auxEndpoint = montaEnvironmentUri(integrador) + "/" + method;
                entity = new HttpEntity(montaHeaderGET());
                voucherNovaXSResponse = restTemplate.exchange(auxEndpoint, httpMethod, entity, byte[].class);

                VoucherRS voucherRS = new VoucherRS()
                        .setVoucher(voucherNovaXSResponse.getBody());

                responseEntity = new ResponseEntity<String>(voucherRS.toString(), HttpStatus.OK);
            } else {
                auxEndpoint = montaEnvironmentUri(integrador) + "/" + method;
                entity = new HttpEntity(request, montaHeader());
                responseEntity = restTemplate.exchange(auxEndpoint, httpMethod, entity, String.class);
            }

            if (responseEntity.getBody().equals("true") && integrador.getDsMetodo().equals("cancelBillRQ")) {
                CancelBillRS cancelBillRS = new CancelBillRS()
                        .setSuccess(Boolean.TRUE);
                responseEntity = new ResponseEntity<String>(cancelBillRS.toString(), HttpStatus.OK);
            }

            result = LogWS.convertResponse(integrador, log, objectMapper, responseEntity, retorno);
            UtilsWS.verificaErro(integrador, result);

        } catch (RestClientException ex) {
            throw LogWS.convertResponseException(integrador, log, ex);
        } catch (ErrorException ex) {
            LogWS.convertResponse(integrador, log, objectMapper, responseEntity, retorno);
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem("Erro " + responseEntity.getStatusCode().toString());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, RESTClient.class, "Erro no envio da Requisição", WSMensagemErroEnum.GENENDPOINT, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        } finally {
            LogWS.montaLog(integrador, log);
        }
        return (T) result;
    }

    private MultiValueMap<String, String> montaHeaderGET() {
        HttpHeaders result = new HttpHeaders();
        result.setAccept(Arrays.asList(MediaType.APPLICATION_PDF));
        return result;
    }

    private HttpHeaders montaHeader() {
        HttpHeaders result = new HttpHeaders();
        result.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        result.add("Content-Type", "application/x-www-form-urlencoded");
        return result;
    }

    private String montaEnvironmentUri(WSIntegrador integrador) throws ErrorException {
        String result = null;
        if (integrador != null) {
            try {
                if (integrador.getDsCredencialList().get(2) != null) {
                    if (WSAmbienteEnum.PRODUCAO.equals(integrador.getAmbiente())) {
                        if (integrador.getDsAction().equals("voucherRQ")) {
                            result = "https://travel3.novaxs.com.br/api";
                        } else {
                            result = "https://travel3.novaxs.com.br/api/v1/2059";
                        }
                    } else {
                        result = "https://travel3.novaxs.com.br/api/v1/2059";
                    }
                }
            } catch (Exception ex) {
                throw new ErrorException(integrador, RESTClient.class, "montaEnvironmentUri", WSMensagemErroEnum.GENENDPOINT, "Erro a o montar Ambiente", WSIntegracaoStatusEnum.NEGADO, ex);
            }
        }
        return result;
    }

}

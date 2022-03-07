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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
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

    public static <T> T convertResponse(WSIntegrador integrador, WSIntegradorLog log, ObjectMapper objectMapper, ResponseEntity<String> responseEntity, Class<T> retorno) throws ErrorException {
        Object result = null;

        try {
            if (log.getDsResponse() == null) {
                log.setDsResponse(((String) responseEntity.getBody()).getBytes());
            }

            result = objectMapper.readValue((String) responseEntity.getBody(), retorno);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        if (responseEntity != null && responseEntity.getStatusCodeValue() != 200) {
            integrador.setDsMensagem("Erro " + responseEntity.getStatusCode().toString());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, Utils.class, "verificaErro", WSMensagemErroEnum.GENCONEC, responseEntity.getStatusCode() + " - " + responseEntity.getStatusCode().getReasonPhrase(), WSIntegracaoStatusEnum.NEGADO, (Throwable) null, false);
        } else {
            return (T) result;
        }
    }

    public <T> T sendReceive(WSIntegrador integrador, Object request, HttpMethod httpMethod, String method, Class<T> retorno) throws ErrorException {
        Object result = null;
        String auxEndpoint = null;
        ResponseEntity<String> responseEntity = null;
        WSIntegradorLog log = new WSIntegradorLog(integrador.getDsAction(), WSIntegradorLogTipoEnum.JSON);
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout((integrador.getTimeoutSegundos() * 1000));
        HttpEntity<String> entity;

        try {
            LogWS.convertRequest(integrador, log, objectMapper, request);
            if (httpMethod.equals(HttpMethod.GET) && integrador.getDsAction().equals("voucherRQ")) {
                ResponseEntity<byte[]> voucherNovaXSResponse = null;
                auxEndpoint = montaEnvironmentUri(integrador) + "/" + method;
                entity = new HttpEntity(montaHeaderGET_PDF());
                voucherNovaXSResponse = restTemplate.exchange(auxEndpoint, httpMethod, entity, byte[].class);

                String s = new String(voucherNovaXSResponse.getBody(), StandardCharsets.UTF_8);

                VoucherRS voucherRS = new VoucherRS()
                        .setVoucher(voucherNovaXSResponse.getBody())
                        .setEndpointVoucher(auxEndpoint);

                if (s.contains("cancelado")) {
                    voucherRS.setStatus("cancelado");
                }

                responseEntity = new ResponseEntity<String>(voucherRS.toString(), HttpStatus.OK);

            } else {
                auxEndpoint = montaEnvironmentUri(integrador) + "/" + method;
                entity = new HttpEntity(request, montaHeader());
                responseEntity = restTemplate.exchange(auxEndpoint, httpMethod, entity, String.class);

            }

            if (responseEntity.getBody().contains("true") && integrador.getDsAction().equals("cancelBillRQ")) {
                CancelBillRS cancelBillRS = new CancelBillRS()
                        .setSuccess(Boolean.TRUE);
                responseEntity = new ResponseEntity<String>(cancelBillRS.toString(), HttpStatus.OK);
            }

            result = convertResponse(integrador, log, objectMapper, responseEntity, retorno);
            UtilsWS.verificaErro(integrador, result);

        } catch (RestClientException ex) {
            throw LogWS.convertResponseException(integrador, log, ex);
        } catch (ErrorException ex) {
            LogWS.convertResponse(integrador, log, objectMapper, responseEntity, retorno);
            throw ex;
        } catch (Exception ex) {
            if (responseEntity != null) {
                integrador.setDsMensagem("Erro " + responseEntity.getStatusCode());
            }
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, RESTClient.class, "Erro no envio da Requisição", WSMensagemErroEnum.GENENDPOINT, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        } finally {
            LogWS.montaLog(integrador, log);
        }
        return (T) result;
    }

    private MultiValueMap<String, String> montaHeaderGET_PDF() {
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
        String clientIdentification;
        if (integrador != null) {
            try {
                if (integrador.getDsCredencialList().get(3) != null) {
                    clientIdentification = integrador.getDsCredencialList().get(3).replace(" ", "");
                    if (WSAmbienteEnum.PRODUCAO.equals(integrador.getAmbiente())) {
                        if (integrador.getDsAction().equals("voucherRQ")) {
                            result = clientIdentification + "/api";
                            result = "https://travel3.novaxs.com.br/api";
                        } else {
                            result = clientIdentification + "/api/v1/2059";
                            result = "https://travel3.novaxs.com.br/api/v1/2059";
                        }
                    } else {
                        result = clientIdentification + "/api/v1/2059";
                    }
                }
            } catch (Exception ex) {
                throw new ErrorException(integrador, RESTClient.class, "montaEnvironmentUri", WSMensagemErroEnum.GENENDPOINT, "Erro a o montar Ambiente", WSIntegracaoStatusEnum.NEGADO, ex);
            }
        }
        return result;
    }

}

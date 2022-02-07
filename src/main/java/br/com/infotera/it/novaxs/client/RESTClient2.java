package br.com.infotera.it.novaxs.client;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSIntegradorLog;
import br.com.infotera.common.enumerator.WSAmbienteEnum;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSIntegradorLogTipoEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.cangooroo.utils.UtilsWS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @author Lucas
 */

@Service
public class RESTClient2 {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T sendReceive(WSIntegrador integrador, Object request, HttpMethod httpMethod, Class<T> retorno) throws ErrorException {
        Object result = null;
        String auxEndpoint = null;
        ResponseEntity<String> responseEntity = null;
        Long tempoInicio = System.currentTimeMillis();
        WSIntegradorLog log = new WSIntegradorLog(integrador.getDsAction(), WSIntegradorLogTipoEnum.JSON);
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout((integrador.getTimeoutSegundos() * 1000));

        try {
            Utils.convertRequest(integrador, log, objectMapper, request);
            if (httpMethod.equals(HttpMethod.GET)) {
                HttpEntity<String> entity = new HttpEntity(montaHeaderCustomizado());
                responseEntity = restTemplate.exchange(integrador.getDsAction(), httpMethod, entity, String.class);
            } else {
                auxEndpoint = montaEnvironmentUri(integrador) + "/" + integrador.getDsAction();
                HttpEntity<String> entity = new HttpEntity(request, montaHeader());
                responseEntity = restTemplate.exchange(auxEndpoint, httpMethod, entity, String.class);
            }
            result = Utils.convertResponse(integrador, log, objectMapper, responseEntity, retorno);
            UtilsWS.verificaErro(integrador, result);
        } catch (RestClientException ex) {
            if (!integrador.getDsAction().equals("Hotel.svc/Search")) {
                throw Utils.convertResponseException(integrador, log, ex);
            } else {
                return null;
            }
        } catch (ErrorException ex) {
            Utils.convertResponse(integrador, log, objectMapper, responseEntity, retorno);
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem("Erro " + responseEntity.getStatusCode().toString());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, RESTClient.class, "Erro no envio da Requisição", WSMensagemErroEnum.GENENDPOINT, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        } finally {
            Utils.montaLog(integrador, log);
        }
        return (T) result;
    }

    private HttpHeaders montaHeader() {
        HttpHeaders result = new HttpHeaders();
        result.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        result.setContentType(MediaType.APPLICATION_JSON);
        return result;
    }

    private MultiValueMap<String, String> montaHeaderCustomizado() {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        result.set("Host", "commons.t4w.com.br");
        result.set("Connection", "keep-alive");
        result.set("Cache-Control", "max-age=0");
        result.set("Upgrade-Insecure-Requests", "1");
        result.set("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
        result.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        result.set("Accept-Encoding", "x-gzip, deflate");
        result.set("Accept-Language", "en-US,en;q=0.9");
        return result;
    }

    private String montaEnvironmentUri(WSIntegrador integrador) throws ErrorException {
        String clientIdentification = null;
        String result = null;

        if (integrador != null) {
            try {
                if (integrador.getDsCredencialList().get(2) != null) {
                    clientIdentification = integrador.getDsCredencialList().get(2).replace(" ", "");

                    if (WSAmbienteEnum.PRODUCAO.equals(integrador.getAmbiente())) {
                        result = clientIdentification + "/API/REST";
                    } else {
                        result = clientIdentification + "/API/REST";
                    }
                }
            } catch (Exception ex) {
                throw new ErrorException(integrador, RESTClient.class, "montaEnvironmentUri", WSMensagemErroEnum.GENENDPOINT, "Erro a o montar Ambiente", WSIntegracaoStatusEnum.NEGADO, ex);
            }
        }
        return result;
    }

}

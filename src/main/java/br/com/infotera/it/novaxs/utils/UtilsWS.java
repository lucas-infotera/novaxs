package br.com.infotera.it.novaxs.utils;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMediaCategoriaEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSPagtoFornecedorTipoEnum;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.BuyToBillForRS;
import br.com.infotera.it.novaxs.model.GetProductsByDateRQ;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import br.com.infotera.it.novaxs.model.Product;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author Lucas
 **/

@Component
public class UtilsWS {

    public static String variavelTemporaria = "VariavelDeTeste";
    private static ObjectMapper objectMapper;

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

    private static double montaVlNeto(GetProductsByDateRS productsByDateRS) throws ErrorException {
        String strValue = Optional.ofNullable(productsByDateRS.getValue()).orElseThrow(() ->
                new ErrorException("Erro ao calcular valores por pax"));
        double vlNeto = Double.parseDouble(strValue) / 100;
        return vlNeto;
    }


    private static WSTarifa montaWSTarifa(WSIntegrador integrador, List<WSReservaNome>  reservaNomeList, GetProductsByDateRS productsByDateRS) throws ErrorException {
        WSTarifa tarifa = null;
        double vlNeto = montaVlNeto(productsByDateRS);
        Integer qtPax = reservaNomeList.size();
        try {
            tarifa = new WSTarifa(productsByDateRS.getCurrency(),
                    vlNeto,
                    vlNeto / (qtPax.doubleValue()),
                    null,
                    null,
                    WSPagtoFornecedorTipoEnum.FATURADO,
                    UtilsWS.montaPoliticaList(productsByDateRS));

//            tarifa.setTarifaNomeList(UtilsWS.montaTarifaNome(integrador, m.getAmountsFrom(), ac.getCurrency()));

            return tarifa;

        } catch (Exception ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade", WSIntegracaoStatusEnum.NEGADO, ex);
        }
    }
    private static WSIngressoModalidade montaIngressoModalidade(WSTarifa tarifa, Product product) {
        return new WSIngressoModalidade(product.getId(),
                product.getName(),
                tarifa);
    }

    public static List<WSIngressoModalidade> montaIngressoModalidadeList(WSIntegrador integrador, List<WSReservaNome>  reservaNomeList, GetProductsByDateRS productsByDateRS) throws ErrorException {
        List<WSIngressoModalidade> ingressoModalidadeList = null;
        try {
            if (productsByDateRS.getProducts() != null) {
                ingressoModalidadeList = montaIngressoModalidade(montaWSTarifa(integrador, reservaNomeList, productsByDateRS), productsByDateRS.getProducts());
            }
        } catch (NullPointerException ex){
            throw new ErrorException(integrador, DisponibilidadeWS.class, "montaIngressoModalidadeList", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade" + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        } catch (ErrorException ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "montaIngressoModalidadeList", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return ingressoModalidadeList;
    }

    private static List<WSIngressoModalidade> montaIngressoModalidade(WSTarifa tarifa, List<Product> productList) {
        List<WSIngressoModalidade> wsIngressoModalidade = new ArrayList<>();
        if (!Utils.isListNothing(productList)) {
            for (Product product : productList) {
                wsIngressoModalidade.add(montaIngressoModalidade(tarifa, product));
            }
        }
        return wsIngressoModalidade;
    }

    public static WSIngresso montaIngresso(WSIntegrador integrador, List<WSReservaNome> reservaNomeList, GetProductsByDateRS productsByDateRS) {
        String dsParamTarifar = productsByDateRS.toString();
        List<WSMedia> mediaList = null;
        if (!productsByDateRS.getType().equals("Combo")) {
            mediaList = montaMediaList(productsByDateRS);
        } else {
            mediaList = montaMediaList(productsByDateRS.getProducts());
        }
        WSIngresso result = new WSIngresso(productsByDateRS.getPath(),
                productsByDateRS.getName(),
                UtilsWS.variavelTemporaria,
                null,
                null,
                null,
                reservaNomeList,
                null,
                mediaList,
                dsParamTarifar,
                null);

        result.setDsParametro(productsByDateRS.toString());

        return result;
    }

    private static List<WSMedia> montaMediaList(GetProductsByDateRS productsByDateRS) {
        List<WSMedia> result = new ArrayList<>();
        WSMedia media = new WSMedia(WSMediaCategoriaEnum.SERVICO, productsByDateRS.getImage());
        result.add(media);
        return result;
    }

    private static List<WSMedia> montaMediaList(List<Product> products) {
        List<WSMedia> result = new ArrayList<>();
        WSMedia media = null;
        for (Product product : products) {
            media = new WSMedia(WSMediaCategoriaEnum.SERVICO, product.getImage());
            result.add(media);
        }
        return result;
    }

    public static List<WSPolitica> montaPoliticaList(GetProductsByDateRS productsByDateRS) throws ErrorException {
        return null;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        UtilsWS.objectMapper = objectMapper;
    }
}

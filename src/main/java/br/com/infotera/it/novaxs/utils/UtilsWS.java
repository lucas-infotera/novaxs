package br.com.infotera.it.novaxs.utils;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.common.politica.WSPoliticaCancelamento;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoPesquisa;
import br.com.infotera.common.servico.WSIngressoUtilizacaoData;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.*;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Lucas
 **/

@Component
public class UtilsWS {

    private static ObjectMapper objectMapper;
    private static Validator validator;

    public static void verificaErro(WSIntegrador integrador, Object errorResponse) throws ErrorException {
        String dsErro = "";
        if (errorResponse != null) {
            dsErro = montaMensagemDeErro(errorResponse);
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
            if (errorResponse instanceof BillForRS) {
                BillForRS billForRS = (BillForRS) errorResponse;
                if (billForRS.getErro() != null) {
                    errorsType = billForRS.getErro();
                }
            }
            if (errorResponse instanceof CreateBillPaymentLinkRS) {
                CreateBillPaymentLinkRS billPaymentLinkRS = (CreateBillPaymentLinkRS) errorResponse;
                if (billPaymentLinkRS.getErro() != null) {
                    errorsType = billPaymentLinkRS.getErro();
                }
            }

            if (errorResponse instanceof GetAccessListRS) {
                GetAccessListRS getAccessListRS = (GetAccessListRS) errorResponse;
                if (getAccessListRS.getErro() != null) {
                    errorsType = getAccessListRS.getErro();
                }
            }
            if (errorResponse instanceof SetAccessListRS) {
                SetAccessListRS setAccessListRS = (SetAccessListRS) errorResponse;
                if (setAccessListRS.getErro() != null) {
                    errorsType = setAccessListRS.getErro();
                }
            }
            if (errorResponse instanceof CancelBillRS) {
                CancelBillRS cancelBillRS = (CancelBillRS) errorResponse;
                if (cancelBillRS.getErro() != null) {
                    errorsType = cancelBillRS.getErro();
                }
            }
            if (errorResponse instanceof VoucherRS) {
                VoucherRS voucherRS = (VoucherRS) errorResponse;
                if (voucherRS.getVoucher() != null) {
                    String s = new String(voucherRS.getVoucher(), StandardCharsets.UTF_8);
                    if (s.contains("erro")) {
                        if (!s.contains("cancelado")) {
                            errorsType = montaRetorno(s, VoucherRS.class).getErro();
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new ErrorException("Erro a o montar ErrorResponse");
        }

        return errorsType;
    }

    private static double montaVlNeto(GetProductsByDateRS productsByDateRS) throws ErrorException {
        String strValue = Optional.ofNullable(productsByDateRS.getValue()).orElseThrow(() -> new ErrorException("Erro ao calcular valores por pax"));
        double vlNeto = Double.parseDouble(strValue) / 100;
        return vlNeto;
    }

    private static WSTarifa montaWSTarifa(WSDisponibilidadeIngressoRQ dispRQ, WSIntegrador integrador, List<WSReservaNome> reservaNomeList, GetProductsByDateRS productsByDateRS) throws ErrorException {
        WSTarifa tarifa = null;

        Integer qtPax = 0;
        if (productsByDateRS.getName().toUpperCase().contains("INFOTERA")) {
            qtPax = 1;
        } else {
            if (!productsByDateRS.getName().toUpperCase().contains("COMBO")) {
                qtPax = montaQtAdultos_QtCriancas(reservaNomeList).get(WSPaxTipoEnum.ADT);
            } else {
                qtPax = 1;
            }
        }
        double vlNeto = montaVlNeto(productsByDateRS) * qtPax;

        try {
            tarifa = new WSTarifa(productsByDateRS.getCurrency(), vlNeto, vlNeto / (qtPax.doubleValue()), null, null, WSPagtoFornecedorTipoEnum.FATURADO, UtilsWS.montaPoliticaList(dispRQ, productsByDateRS));

            tarifa.setTarifaNomeList(montaTarifaNomeList(productsByDateRS, tarifa));

            return tarifa;

        } catch (Exception ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade", WSIntegracaoStatusEnum.NEGADO, ex);
        }
    }

    public static Map<WSPaxTipoEnum, Integer> montaQtAdultos_QtCriancas(List<WSReservaNome> reservaNomeList) {
        Map<WSPaxTipoEnum, Integer> qtAdultos_qtCriancas = new HashMap<>();
        int qtCrianca = 0;
        int qtAdulto = 0;
        for (WSReservaNome reservaNome : reservaNomeList) {
            if (reservaNome.getPaxTipo().equals(WSPaxTipoEnum.CHD)) {
                qtCrianca++;
            }
            if (reservaNome.getPaxTipo().equals(WSPaxTipoEnum.ADT)) {
                qtAdulto++;
            }
        }

        qtAdultos_qtCriancas.put(WSPaxTipoEnum.ADT, qtAdulto);
        qtAdultos_qtCriancas.put(WSPaxTipoEnum.CHD, qtCrianca);

        return qtAdultos_qtCriancas;
    }

    private static List<WSTarifaNome> montaTarifaNomeList(GetProductsByDateRS productsByDateRS, WSTarifa tarifa) {
        List<WSTarifaNome> wsTarifaNomes = new ArrayList<>();
        WSTarifaNome tarifaNome = new WSTarifaNome();
        tarifaNome.setPaxTipo(WSPaxTipoEnum.ADT);
        tarifaNome.setQtIdade(30);
        tarifaNome.setTarifa(new WSTarifa(productsByDateRS.getCurrency(), tarifa.getVlNeto(), null));
        wsTarifaNomes.add(tarifaNome);
        return wsTarifaNomes;
    }

    private static WSIngressoModalidade montaIngressoModalidade(WSDisponibilidadeIngressoRQ rq, WSTarifa tarifa, GetProductsByDateRS productsByDateRS) {
        List<WSIngressoUtilizacaoData> utilizacaoDatasList = new ArrayList<>();
        WSIngressoModalidade wsIngressoModalidade = new WSIngressoModalidade(productsByDateRS.getPath(), productsByDateRS.getName(), tarifa);

        utilizacaoDatasList.add(montaWSIngressoUtilizacao(rq, productsByDateRS));
        wsIngressoModalidade.setUtilizacaoDatasList(utilizacaoDatasList);
        return wsIngressoModalidade;

    }

    public static WSIngressoUtilizacaoData montaWSIngressoUtilizacao(WSDisponibilidadeIngressoRQ rq, GetProductsByDateRS productsByDateRS) {
        WSIngressoUtilizacaoData wsIngressoUtilizacaoData = new WSIngressoUtilizacaoData();
        wsIngressoUtilizacaoData.setDtInicio(rq.getDtInicio());
        wsIngressoUtilizacaoData.setDtFim(rq.getDtInicio());
        Parametro parametro = new Parametro()
                .setNomeIngresso(productsByDateRS.getName())
                .setDt(rq.getDtInicio().toString())
                .setCd(productsByDateRS.getPath())
                .setNomeModalidade(productsByDateRS.getName())
                .setCdModalidade(productsByDateRS.getPath());

        if (productsByDateRS.getValue() != null) {
            double vlTotal = 0;
            if (productsByDateRS.getName().toUpperCase().contains("INFOTERA")) {
                vlTotal = (Double.parseDouble(productsByDateRS.getValue()) / 100);
            } else {
                if (!productsByDateRS.getName().toUpperCase().contains("COMBO")) {
                    vlTotal = (Double.parseDouble(productsByDateRS.getValue()) / 100) * montaQtAdultos_QtCriancas(rq.getReservaNomeList()).get(WSPaxTipoEnum.ADT);
                } else {
                    vlTotal = (Double.parseDouble(productsByDateRS.getValue()) / 100);
                }
            }
            wsIngressoUtilizacaoData.setVlTotal(vlTotal);
        }
        wsIngressoUtilizacaoData.setDsTarifa("null~" + wsIngressoUtilizacaoData.getVlTotal().toString() + "#" + parametro.toString());
        return wsIngressoUtilizacaoData;
    }

    public static WSIngressoUtilizacaoData montaWSIngressoUtilizacao(WSDisponibilidadeIngressoRQ rq, GetProductsByDateRS productsByDateRS, Schedule schedule) {
        WSIngressoUtilizacaoData wsIngressoUtilizacaoData = new WSIngressoUtilizacaoData();
        wsIngressoUtilizacaoData.setDtInicio(rq.getDtInicio());
        wsIngressoUtilizacaoData.setDtFim(rq.getDtInicio());
        Parametro parametro = new Parametro()
                .setNomeIngresso(productsByDateRS.getName())
                .setDt(rq.getDtInicio().toString())
                .setCd(productsByDateRS.getPath())
                .setHorario(schedule.getSchedule())
                .setCdModalidade(schedule.getPath())
                .setNomeModalidade(schedule.getSchedule());

        if (productsByDateRS.getValue() != null) {
            double vlTotal = 0;
            if (productsByDateRS.getName().toUpperCase().contains("INFOTERA")) {
                vlTotal = (Double.parseDouble(productsByDateRS.getValue()) / 100);
            } else {
                vlTotal = (Double.parseDouble(productsByDateRS.getValue()) / 100) * montaQtAdultos_QtCriancas(rq.getReservaNomeList()).get(WSPaxTipoEnum.ADT);
            }
            wsIngressoUtilizacaoData.setVlTotal(vlTotal);
        }
        wsIngressoUtilizacaoData.setDsTarifa("null~" + wsIngressoUtilizacaoData.getVlTotal().toString() + "#" + parametro.toString());
        return wsIngressoUtilizacaoData;
    }

    private static List<WSIngressoModalidade> montaIngressoModalidadeCombo(WSIngressoPesquisa ingressoPesquisa, WSDisponibilidadeIngressoRQ dispRQ, WSTarifa tarifa, GetProductsByDateRS productsByDateRS) {
        List<WSIngressoModalidade> wsIngressoModalidade = new ArrayList<>();
        int i = 0;
        if (ingressoPesquisa != null) {
            ingressoPesquisa.getIngressoModalidadeList().add(montaIngressoModalidadeCombo(ingressoPesquisa, dispRQ, tarifa, productsByDateRS.getProducts().get(0), productsByDateRS));
        }
        return wsIngressoModalidade;
    }

    private static List<WSIngressoModalidade> montaIngressoModalidadeComHorario(WSIngressoPesquisa ingressoPesquisa, WSDisponibilidadeIngressoRQ dispRQ, WSTarifa tarifa, List<Schedule> schedules, GetProductsByDateRS productsByDateRS) {
        List<WSIngressoModalidade> wsIngressoModalidade = new ArrayList<>();

        if (ingressoPesquisa != null) {
            for (WSIngressoModalidade modalidade : ingressoPesquisa.getIngressoModalidadeList()) {
                for (Schedule schedule : schedules) {
                    String cdReferenciaSchedule = schedule.getPath() + "-" + schedule.getSchedule();
                    if (modalidade.getCdModalidade().equals(cdReferenciaSchedule)) {
                        modalidade.getUtilizacaoDatasList().add(montaWSIngressoUtilizacao(dispRQ, productsByDateRS, schedule));
                    }
                }
            }
            return ingressoPesquisa.getIngressoModalidadeList();
        } else {
            for (Schedule schedule : schedules) {
                wsIngressoModalidade.add(montaIngressoModalidadeComHorario(dispRQ, tarifa, schedule, productsByDateRS));
            }
        }


        return wsIngressoModalidade;
    }

    public static WSIngressoModalidade montaIngressoModalidadeCombo(WSIngressoPesquisa ingressoPesquisa, WSDisponibilidadeIngressoRQ ingressoRQ, WSTarifa tarifa, Product product, GetProductsByDateRS productsByDateRS) {
        WSIngressoModalidade modalidade = null;
        if (ingressoPesquisa != null) {
            if (ingressoPesquisa.getIngressoModalidadeList() != null) {
                for (WSIngressoModalidade m : ingressoPesquisa.getIngressoModalidadeList()) {
                    if (!m.getCdModalidade().equals(productsByDateRS.getPath())) {
                        modalidade = new WSIngressoModalidade(productsByDateRS.getPath(), productsByDateRS.getName(), tarifa);
                        modalidade.setDsModalidade("Combo Ingresso" + product.getName());
                        WSIngressoUtilizacaoData wsIngressoUtilizacaoData = montaWSIngressoUtilizacao(ingressoRQ, productsByDateRS);
                        List<WSIngressoUtilizacaoData> ingressoUtilizacaoDataList = new ArrayList<>();
                        ingressoUtilizacaoDataList.add(wsIngressoUtilizacaoData);
                        modalidade.setUtilizacaoDatasList(ingressoUtilizacaoDataList);
                    } else {
                        m.getUtilizacaoDatasList().add(montaWSIngressoUtilizacao(ingressoRQ, productsByDateRS));
                    }
                }

            }
        }
        return modalidade;
    }

    public static List<WSIngressoModalidade> montaIngressoModalidadeComboDiasDeAcesso(WSIngressoPesquisa ingressoPesquisa, WSDisponibilidadeIngressoRQ ingressoRQ, WSTarifa tarifa, Product product, GetProductsByDateRS productsByDateRS) {
        List<WSIngressoModalidade> modalidadeList = new ArrayList<>();
        WSIngressoModalidade modalidade = null;
        if (ingressoPesquisa != null) {
            if (ingressoPesquisa.getIngressoModalidadeList() != null) {
                for (WSIngressoModalidade m : ingressoPesquisa.getIngressoModalidadeList()) {
                    WSIngressoUtilizacaoData wsIngressoUtilizacaoData = montaWSIngressoUtilizacao(ingressoRQ, productsByDateRS);
                    List<WSIngressoUtilizacaoData> ingressoUtilizacaoDataList = new ArrayList<>();
                    ingressoUtilizacaoDataList.add(wsIngressoUtilizacaoData);
                    m.setUtilizacaoDatasList(ingressoUtilizacaoDataList);
                    m.getUtilizacaoDatasList().add(montaWSIngressoUtilizacao(ingressoRQ, productsByDateRS));
                    return ingressoPesquisa.getIngressoModalidadeList();
                }
            }
        } else {
            modalidade = new WSIngressoModalidade(productsByDateRS.getPath(), productsByDateRS.getName(), tarifa);
            modalidade.setDsModalidade("Combo Ingresso" + product.getName());
            WSIngressoUtilizacaoData wsIngressoUtilizacaoData = montaWSIngressoUtilizacao(ingressoRQ, productsByDateRS);
            List<WSIngressoUtilizacaoData> ingressoUtilizacaoDataList = new ArrayList<>();
            ingressoUtilizacaoDataList.add(wsIngressoUtilizacaoData);
            modalidade.setUtilizacaoDatasList(ingressoUtilizacaoDataList);
            modalidadeList.add(modalidade);
        }
        return modalidadeList;
    }

    private static WSIngressoModalidade montaIngressoModalidadeComHorario(WSDisponibilidadeIngressoRQ ingressoRQ, WSTarifa tarifa, Schedule schedule, GetProductsByDateRS productsByDateRS) {
        WSIngressoModalidade modalidade = new WSIngressoModalidade(schedule.getPath() + "-" + schedule.getSchedule(), productsByDateRS.getName() + " " + schedule.getSchedule(), tarifa);
        modalidade.setDsModalidade("Ingresso com agendamento" + schedule.getSchedule());
        WSIngressoUtilizacaoData wsIngressoUtilizacaoData = montaWSIngressoUtilizacao(ingressoRQ, productsByDateRS, schedule);
        List<WSIngressoUtilizacaoData> ingressoUtilizacaoDataList = new ArrayList<>();
        ingressoUtilizacaoDataList.add(wsIngressoUtilizacaoData);
        modalidade.setUtilizacaoDatasList(ingressoUtilizacaoDataList);
        return modalidade;
    }

    public static List<WSIngressoModalidade> montaIngressoModalidadeList(WSIngressoPesquisa ingressoPesquisa, WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {
        List<WSIngressoModalidade> ingressoModalidadeList = new ArrayList<>();
        try {
            WSTarifa tarifa = montaWSTarifa(dispRQ, dispRQ.getIntegrador(), dispRQ.getReservaNomeList(), productsByDateRS);
            ingressoModalidadeList.addAll(montaIngressoModalidadeComTarifa(ingressoPesquisa, dispRQ, tarifa, productsByDateRS));

        } catch (NullPointerException ex) {
            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaIngressoModalidadeList", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade" + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex);
        } catch (ErrorException ex) {
            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaIngressoModalidadeList", WSMensagemErroEnum.SDI, "Erro ao armazenar tarifa/modalidade", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return ingressoModalidadeList;
    }

    private static List<WSIngressoModalidade> montaIngressoModalidadeComTarifaGetProductsByDateRS(WSIngressoPesquisa ingressoPesquisa, WSDisponibilidadeIngressoRQ dispRQ, WSTarifa tarifa, GetProductsByDateRS productsByDateRS) {
        List<WSIngressoModalidade> wsIngressoModalidade = new ArrayList<>();

        if (ingressoPesquisa != null) {
            for (WSIngressoModalidade modalidade : ingressoPesquisa.getIngressoModalidadeList()) {
                if (modalidade.getCdModalidade().equals(productsByDateRS.getPath())) {
                    modalidade.getUtilizacaoDatasList().add(montaWSIngressoUtilizacao(dispRQ, productsByDateRS));
                    return ingressoPesquisa.getIngressoModalidadeList();
                }
            }
        } else {
            wsIngressoModalidade.add(montaIngressoModalidade(dispRQ, tarifa, productsByDateRS));
        }

        return wsIngressoModalidade;
    }


    public static List<WSIngressoModalidade> montaIngressoModalidadeComTarifa(WSIngressoPesquisa ingressoPesquisa, WSDisponibilidadeIngressoRQ dispRQ, WSTarifa tarifa, GetProductsByDateRS productsByDateRS) throws ErrorException {
        List<WSIngressoModalidade> wsIngressoModalidade = new ArrayList<>();
        if (!productsByDateRS.getName().toUpperCase().contains("INFOTERA")) {
            if (productsByDateRS.getName().contains("Horário")) {
                if (!Utils.isListNothing(productsByDateRS.getSchedules())) {
                    return montaIngressoModalidadeComHorario(ingressoPesquisa, dispRQ, tarifa, productsByDateRS.getSchedules(), productsByDateRS);
                }
            } else if (productsByDateRS.getName().toUpperCase().contains("INDIVIDUAL")
                    || productsByDateRS.getName().toUpperCase().contains("ingresso".toUpperCase())
                    &&  productsByDateRS.getName().toUpperCase().contains("Adulto".toUpperCase())
            ) {
                wsIngressoModalidade.addAll(montaIngressoModalidadeComTarifaGetProductsByDateRS(ingressoPesquisa, dispRQ, tarifa, productsByDateRS));
            } else if (productsByDateRS.getName().toUpperCase().contains("COMBO")
                    && productsByDateRS.getName().toUpperCase().contains("INGRESSO")) {
                return montaIngressoModalidadeCombo(ingressoPesquisa, dispRQ, tarifa, productsByDateRS);
            } else if (productsByDateRS.getName().toUpperCase().contains("COMBO")
                    && productsByDateRS.getName().toUpperCase().contains("DIAS")
                    && productsByDateRS.getName().toUpperCase().contains("ACESSO")
            ) {
                wsIngressoModalidade.addAll(montaIngressoModalidadeComboDiasDeAcesso(ingressoPesquisa, dispRQ, tarifa, productsByDateRS.getProducts().get(0), productsByDateRS));
            }
        } else {
            if (productsByDateRS.getName().contains("Horário")) {
                if (!Utils.isListNothing(productsByDateRS.getSchedules())) {
                    return montaIngressoModalidadeComHorario(ingressoPesquisa, dispRQ, tarifa, productsByDateRS.getSchedules(), productsByDateRS);
                }
            }
            wsIngressoModalidade.addAll(montaIngressoModalidadeComTarifaGetProductsByDateRS(ingressoPesquisa, dispRQ, tarifa, productsByDateRS));
        }

        return wsIngressoModalidade;
    }

    public static WSIngresso montaIngresso(WSIntegrador integrador, WSDisponibilidadeIngressoRQ ingressoRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {
        List<WSMedia> mediaList = new ArrayList<>();
        if (productsByDateRS != null) {
            mediaList.addAll(montaMediaList(productsByDateRS));
            if (productsByDateRS.getProducts() != null) {
                mediaList.addAll(montaMediaList(productsByDateRS.getProducts()));
            }
            if (mediaList.isEmpty()) {
                mediaList = null;
            }
        }

        WSIngresso result = new WSIngresso(productsByDateRS.getPath(),
                productsByDateRS.getName(),
                productsByDateRS.getName(),
                null,
                null,
                null,
                ingressoRQ.getReservaNomeList(),
                montaWSTarifa(ingressoRQ, integrador, ingressoRQ.getReservaNomeList(), productsByDateRS),
                mediaList,
                null,
                null);

        if (productsByDateRS.getHtmlMemo() != null) {
            result.setDsServico(productsByDateRS.getHtmlMemo());
        }


        result.setDsParametro(
                new Parametro()
                        .setNomeIngresso(productsByDateRS.getName())
                        .setCd(productsByDateRS.getPath())
                        .setDt(ingressoRQ.getDtInicio().toString())
                        .toString()
        );
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

    public static List<WSPolitica> montaPoliticaList(WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {
        List<WSPolitica> result = new ArrayList<>();
        List<WSPoliticaCancelamento> politicaCancelamentoList = new ArrayList();
        if (productsByDateRS != null) {
            if (productsByDateRS.getCancellationPolicies() != null) {

                WSPoliticaCancelamento politicaCancelamento = new WSPoliticaCancelamento("Cancelamento",
                        productsByDateRS.getCancellationPolicies(),
                        null,
                        null,
                        null,
                        null,
                        false);

                politicaCancelamentoList.add(politicaCancelamento);
            }
        }
        result.addAll(politicaCancelamentoList);
        return result;
    }

    public static Parametro converterDSParametro(String dsParametro) throws ErrorException {
        Parametro result = null;
        String parametroTratado;
        try {
            parametroTratado = dsParametro.replaceAll("[a-z][a-z][a-z][a-z][~][0-9][0-9][0-9][.][0-9][#]", "")
                    .replaceAll("[a-z][a-z][a-z][a-z][~][0-9][0-9][0-9][0-9][.][0-9][#]", "")
                    .replaceAll("[a-z][a-z][a-z][a-z][~][0-9][0-9][.][0-9][#]", "")
                    .replaceAll("[a-z][a-z][a-z][a-z][~][0-9][0-9][.][0-9][0-9][#]", "")
                    .replaceAll("[a-z][a-z][a-z][a-z][~][0-9][0-9][0-9][.][0-9][0-9][#]", "");

            result = montaRetorno(parametroTratado, Parametro.class);
        } catch (ErrorException ex) {
            throw ex;
        }
        return result;
    }

    private static <T> T montaRetorno(String dsParametro, Class<T> retorno) throws ErrorException {
        Object objResponse = null;
        try {
            objResponse = objectMapper.readValue(dsParametro, retorno);
        } catch (JsonProcessingException ex) {
            throw new ErrorException("Erro a o converter dsParametro");
        }
        return retorno.cast(objResponse);
    }

    public static CredenciaisNovaxsRQ montaCredenciaisNovaXS(WSIntegrador integrador) throws ErrorException {
        CredenciaisNovaxsRQ credenciaisNovaxsRQ = null;
        try {
            credenciaisNovaxsRQ = new CredenciaisNovaxsRQ(integrador.getDsCredencialList().get(0), integrador.getDsCredencialList().get(1), integrador.getDsCredencialList().get(2));
            validator(credenciaisNovaxsRQ);
        } catch (ConstraintViolationException ex) {
            throw new ErrorException(ex.getMessage());
        }
        return credenciaisNovaxsRQ;
    }

    public static CredenciaisNovaxsRQ montaCredenciaisNovaXS(WSIntegrador integrador, String token) throws ErrorException {
        CredenciaisNovaxsRQ credenciaisNovaxsRQ = null;
        try {
            credenciaisNovaxsRQ = new CredenciaisNovaxsRQ(integrador.getDsCredencialList().get(0), integrador.getDsCredencialList().get(1), token);
            validator(credenciaisNovaxsRQ);
        } catch (ConstraintViolationException ex) {
            throw new ErrorException(ex.getMessage());
        }
        return credenciaisNovaxsRQ;
    }


    public static Person montaPersonAsStringDadosDoComprador(WSReserva reserva) throws ErrorException {
        Person result;
        WSContato contato = reserva.getContato();
        try {
            result = new Person()
                    .setCellPhone(contato.getTelefone().getTransNrTelefone())
                    .setHomePhone(contato.getTelefone().getNrTelefone())
                    .setEmail(contato.getEmail());

            reserva.getReservaServicoList().get(0)
                    .getServico().getReservaNomeList().forEach(reservaNome -> {
                        if (reservaNome.isStPrincipal()) {
                            if (reservaNome.getDocumento().getDocumentoTipo().equals(WSDocumentoTipoEnum.CPF.getId())) {
                                result.setCpf(reservaNome.getDocumento().getNrDocumento());
                            }
                            result.setName(reservaNome.getNmNomeCompleto());
                        }
                    });

            validator(result);
        } catch (ConstraintViolationException ex) {
            throw new ErrorException(ex.getMessage());
        }
        return result;
    }

    public static <T> void validator(Object objeto) {

        Set<ConstraintViolation<Object>> violations = validator.validate(objeto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static ProductsArray montaProductsArray(WSReserva reserva) throws ErrorException {
        ProductsArray result = null;
        List<Product> productList = new ArrayList<>();
        Product product = null;
        String[] pathArray;

        if (reserva != null) {
            result = new ProductsArray();
            if (reserva.getReservaServicoList() != null) {
                if (!Utils.isListNothing(reserva.getReservaServicoList())) {
                    for (WSReservaServico reservaServico : reserva.getReservaServicoList()) {
                        Parametro dsParametro = Optional.ofNullable(UtilsWS.converterDSParametro(reservaServico.getDsParametro()))
                                .orElseThrow(() -> new ErrorException("Erro a o converter DsParametro para montar ProductsArray"));

                        try {
                            /*Quando o mesmo conter Infotera sabemos que ser refere a Combo de ingressos com adultos e crianças */
                            /*Primeiro produto sempre é o do Adulto  */
                            if (reservaServico.getServico().getNmServico().contains("infotera".toUpperCase())) {
                                pathArray = dsParametro.getCd().split("-");
                                int i = 0;
                                for (String path : pathArray) {
                                    if (i == 0) {
                                        product = montaProduct(reserva, dsParametro, path, WSPaxTipoEnum.ADT);
                                    } else {
                                        product = montaProduct(reserva, dsParametro, path, WSPaxTipoEnum.CHD);
                                    }
                                    i++;
                                    productList.add(product);
                                }
                            } else {
                                pathArray = dsParametro.getCdModalidade().split("-");
                                if (dsParametro.getNomeModalidade().contains("Combo")) {
                                    if (pathArray.length > 1) {
                                        product = montaProduct(reserva, dsParametro, pathArray[1], WSPaxTipoEnum.ADT);
                                    }
                                } else {
                                    product = montaProduct(reserva, dsParametro, pathArray[0], WSPaxTipoEnum.ADT);
                                }
                                productList.add(product);
                            }
                            result.setProductsArray(productList);
                        } catch (NullPointerException ex) {
                            throw new ErrorException("Erro de nullpointer no metodo montaProductsArray ");
                        } catch (ParseException ex) {
                            throw new ErrorException("Erro de parse na montagem da data para a novaxs no metodo montaProductsArray");
                        }
                    }
                }
            }

        }
        return result;
    }

    private static Product montaProduct(WSReserva reserva, Parametro dsParametro, String path, WSPaxTipoEnum paxTipoEnum) throws ParseException {
        Product product = new Product()
                .setAmount(UtilsWS.montaQtAdultos_QtCriancas(reserva.getReservaServicoList().get(0)
                        .getServico().getReservaNomeList()).get(paxTipoEnum).toString())
                .setDate(UtilsWS.montaDataNovaxs(montaStringToDate(dsParametro.getDt())))
                .setPath(path);
        if (dsParametro.getHorario() != null
                && !dsParametro.getHorario().equals("")
                && !dsParametro.getHorario().equals(" ")) {
            product.setSchedule(dsParametro.getHorario());
        } else if (dsParametro.getNomeModalidade().toUpperCase().contains("Horário".toUpperCase())){
            String[] cdModalidade = dsParametro.getCdModalidade().split("-");
            if (path.equals(cdModalidade[0])) {
                product.setSchedule(cdModalidade[1]);
            }
        }
        return product;
    }

    private static Date montaStringToDate(String data) throws ParseException {
        return new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault()).parse(data);
    }

    public static String montaDataNovaxs(Date date) {
        return Utils.formatData(date, "dd/MM/yyyy");
    }

    public static Date montaDataInfotravel(String date) throws ErrorException {
        Date result = null;
        try {
            result = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            throw new ErrorException("Erro a o converter data para o infotravel");
        }

        return result;
    }

    public static CustomData montaCustomData() {
        return new CustomData()
                .setIdExterno("1")
                .setObs("Integração");
    }

    @Autowired
    public void setValidator(Validator validator) {
        UtilsWS.validator = validator;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        UtilsWS.objectMapper = objectMapper;
    }
}

package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoPesquisa;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.GetProductsByDateRQ;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @Author Lucas
 **/

@Service
public class DisponibilidadeWS {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private NovaxsClient novaxsClient;

    public static String montaIntegrador() {
        return "{\n" +
                "        \"integradorEnum\": \"NOVAXS\",\n" +
                "        \"id\": 6231,\n" +
                "        \"nmIntegrador\": \"Beach Park\",\n" +
                "        \"ambiente\": \"PRODUCAO\",\n" +
                "        \"timeoutSegundos\": 40,\n" +
                "        \"idParceiro\": \"113\",\n" +
                "        \"ativo\": true,\n" +
                "        \"qtMaximaSessao\": 2,\n" +
                "        \"dsUrl\": \"http://10.0.0.28:8070/novaxs\",\n" +
                "        \"stLocal\": false,\n" +
                "        \"dsCredencialList\": [\n" +
                "            \"docuser\",\n" +
                "            \"abc1234\",\n" +
                "            \"E1D779DB5D11E4C6EED41B418B53C2AC4205B843\",\n" +
                "            null\n" +
                "        ],\n" +
                "        \"stSelecionado\": false,\n" +
                "        \"idFornecedor\": 22635,\n" +
                "        \"idEmpresa\": 1,\n" +
                "        \"sgEmpresa\": \"ORITEST\",\n" +
                "        \"nmUsuarioEmpresa\": \"Infotera\",\n" +
                "        \"sgNacionalidade\": \"BR\",\n" +
                "        \"stErro\": false,\n" +
                "        \"stGerarLog\": false,\n" +
                "        \"transStMarkupTaxa\": false,\n" +
                "        \"transStComissaoTaxa\": false,\n" +
                "        \"dsMetodo\":  \"disponibilidadeIngresso\",\n" +
                "        \"idiomaEnum\": \"PT_BR\"\n" +
                "    }";
    }

    public WSDisponibilidadeIngressoRS disponibilidade(WSDisponibilidadeIngressoRQ dispRQ) throws ErrorException {
        WSDisponibilidadeIngressoRS result = null;
        WSIntegrador integrador = dispRQ.getIntegrador();
        try {
            List<WSIngressoPesquisa> ingressoPesquisaList = pesquisarIngresso(dispRQ);
            result = new WSDisponibilidadeIngressoRS(ingressoPesquisaList, integrador, WSIntegracaoStatusEnum.OK);
        } catch (ErrorException ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            if (ex.getIntegrador() == null) {
                ex.setIntegrador(integrador);
            }
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "disponibilidade", WSMensagemErroEnum.SDI, "Erro ao pesquisar atividades", WSIntegracaoStatusEnum.NEGADO, ex);
        }
        return result;
    }

    public List<WSIngressoPesquisa> pesquisarIngresso(WSDisponibilidadeIngressoRQ dispRQ) throws ErrorException {
        List<WSIngressoPesquisa> result = new ArrayList<>();
        WSIntegrador integrador = dispRQ.getIntegrador();
        List<WSReservaNome> reservaNomeList = dispRQ.getReservaNomeList();
        List<Date> rangedatasPesquisa = montaRangedatasPesquisa(dispRQ);

        if (rangedatasPesquisa != null) {
            for (Date dtPesquisa : rangedatasPesquisa) {
                WSDisponibilidadeIngressoRQ ingressoRQ = new WSDisponibilidadeIngressoRQ(integrador, reservaNomeList, dispRQ.getCdDestino(), dtPesquisa, dtPesquisa);
                try {
                    List<GetProductsByDateRS> getProductsByDateRSList = novaxsClient.getProductsByDateRQ(ingressoRQ.getIntegrador(),
                            montaRequestGetProductsByDateRQ(ingressoRQ.getIntegrador(), ingressoRQ.getDtInicio()));
//                    if (UtilsWS.variavelTemporaria != null) {
//                        GetProductsByDateRS[] e = null;
//                        try {
//                            e = objectMapper.readValue(testeAgendamentoCOmHorario(), GetProductsByDateRS[].class);
//                        } catch (JsonProcessingException ex) {
//                            ex.printStackTrace();
//                        }
//                        result = montaPesquisarIngressoResult(result, ingressoRQ, Arrays.asList(e));
////                                result = montaPesquisarIngressoResult(result, ingressoRQ, getProductsByDateRSList);
//
//                    }
                    if (getProductsByDateRSList != null) {
                        if (!getProductsByDateRSList.isEmpty()) {
                            result = montaPesquisarIngressoResult(result, ingressoRQ, getProductsByDateRSList);
                        }
                    }
                } catch (NullPointerException ex) {
                    throw new ErrorException(ingressoRQ.getIntegrador(), DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, true);
                } catch (ErrorException ex) {
                    throw new ErrorException(ingressoRQ.getIntegrador(), DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            }
        }

        return result;
    }

    /*public String testeAgendamentoCOmHorario() {
        return "[\n" +
                "    {\n" +
                "        \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "        \"image\": \"\",\n" +
                "        \"minAmount\": \"1\",\n" +
                "        \"travelertype\": \"ADT\",\n" +
                "        \"htmlMemo\": \"\",\n" +
                "        \"available\": \"2147483647.00\",\n" +
                "        \"memo\": \"\",\n" +
                "        \"supplierPath\": \"agencies/954\",\n" +
                "        \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "        \"type\": \"Produto\",\n" +
                "        \"additionalLanguagesInfo\": [],\n" +
                "        \"path\": \"products/47841\",\n" +
                "        \"form\": [\n" +
                "            {\n" +
                "                \"name\": \"Nome do Cliente\",\n" +
                "                \"property\": \"client_name\",\n" +
                "                \"type\": \"text\",\n" +
                "                \"group\": \"\",\n" +
                "                \"order\": 0,\n" +
                "                \"per_entry\": true,\n" +
                "                \"product\": \"Ingresso Individual com Formulário\",\n" +
                "                \"product_path\": \"products/47841\",\n" +
                "                \"required\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"Data de Nascimento\",\n" +
                "                \"property\": \"client_birthday\",\n" +
                "                \"type\": \"date\",\n" +
                "                \"group\": \"\",\n" +
                "                \"order\": 2,\n" +
                "                \"per_entry\": true,\n" +
                "                \"product\": \"Ingresso Individual com Formulário\",\n" +
                "                \"product_path\": \"products/47841\",\n" +
                "                \"required\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"Documento do Cliente\",\n" +
                "                \"property\": \"client_document\",\n" +
                "                \"type\": \"text\",\n" +
                "                \"group\": \"\",\n" +
                "                \"order\": 1,\n" +
                "                \"per_entry\": true,\n" +
                "                \"product\": \"Ingresso Individual com Formulário\",\n" +
                "                \"product_path\": \"products/47841\",\n" +
                "                \"required\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"Tipo\",\n" +
                "                \"property\": \"type\",\n" +
                "                \"type\": \"list\",\n" +
                "                \"group\": \"\",\n" +
                "                \"order\": 3,\n" +
                "                \"per_entry\": false,\n" +
                "                \"product\": \"Ingresso Individual com Formulário\",\n" +
                "                \"product_path\": \"products/47841\",\n" +
                "                \"required\": false,\n" +
                "                \"values\": [\n" +
                "                    {\n" +
                "                        \"show\": \"Grupo\",\n" +
                "                        \"value\": \"Grupo\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"show\": \"Individual\",\n" +
                "                        \"value\": \"Individual\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ],\n" +
                "        \"definePrecoNaVenda\": \"false\",\n" +
                "        \"custo\": 13950,\n" +
                "        \"schedules\": [],\n" +
                "        \"faixaetaria\": \"ADT\",\n" +
                "        \"name\": \"Ingresso Individual com Formulário\",\n" +
                "        \"currency\": \"BRL\",\n" +
                "        \"id\": \"47841\",\n" +
                "        \"shortName\": \"Ingresso Individual com Formulário\",\n" +
                "        \"value\": \"13950\",\n" +
                "        \"token\": \"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqf6aBhunFggi98sSfQv7z9F3VITStB8liQFDCu4o+W9VYEVYvfureCj1tMndGVGDp4y+FFI5eNtj2YHOHndpmEHQL66OC/ysRvHlt6nCYEkeb\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "        \"image\": \"\",\n" +
                "        \"minAmount\": \"1\",\n" +
                "        \"travelertype\": \"ADT\",\n" +
                "        \"htmlMemo\": \"\",\n" +
                "        \"available\": \"99.00\",\n" +
                "        \"memo\": \"\",\n" +
                "        \"supplierPath\": \"agencies/954\",\n" +
                "        \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "        \"type\": \"Produto\",\n" +
                "        \"additionalLanguagesInfo\": [],\n" +
                "        \"path\": \"products/46700\",\n" +
                "        \"definePrecoNaVenda\": \"false\",\n" +
                "        \"custo\": 9000,\n" +
                "        \"schedules\": [],\n" +
                "        \"faixaetaria\": \"ADT\",\n" +
                "        \"name\": \"Ingresso Individual\",\n" +
                "        \"currency\": \"BRL\",\n" +
                "        \"id\": \"46700\",\n" +
                "        \"shortName\": \"Ingresso Individual\",\n" +
                "        \"value\": \"9000\",\n" +
                "        \"token\": \"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqf3gbEMdyBrsdiNnP0tarQyzVITStB8liQFDCu4o+W9VYEVYvfureCj1tMndGVGDp4y+FFI5eNtj2YHOHndpmEHSk9yvv8v6MlMvgb2OTz2+5\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "        \"image\": \"\",\n" +
                "        \"minAmount\": \"1\",\n" +
                "        \"travelertype\": \"ADT\",\n" +
                "        \"schedule_type\": \"code\",\n" +
                "        \"htmlMemo\": \"\",\n" +
                "        \"available\": \"2147483647.00\",\n" +
                "        \"memo\": \"\",\n" +
                "        \"supplierPath\": \"agencies/954\",\n" +
                "        \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "        \"type\": \"Produto\",\n" +
                "        \"additionalLanguagesInfo\": [],\n" +
                "        \"path\": \"products/46701\",\n" +
                "        \"definePrecoNaVenda\": \"false\",\n" +
                "        \"custo\": 8091,\n" +
                "        \"schedules\": [\n" +
                "            {\n" +
                "                \"path\": \"products/46701\",\n" +
                "                \"schedule\": \"11:00\",\n" +
                "                \"available\": \"2147483647.00\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"path\": \"products/46701\",\n" +
                "                \"schedule\": \"13:00\",\n" +
                "                \"available\": \"2147483647.00\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"path\": \"products/46701\",\n" +
                "                \"schedule\": \"15:00\",\n" +
                "                \"available\": \"2147483647.00\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"faixaetaria\": \"ADT\",\n" +
                "        \"name\": \"Ingresso com Horário\",\n" +
                "        \"currency\": \"BRL\",\n" +
                "        \"id\": \"46701\",\n" +
                "        \"needExtraData\": \"hora\",\n" +
                "        \"shortName\": \"Ingresso com Horário\",\n" +
                "        \"value\": \"8091\",\n" +
                "        \"token\": \"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqf2IGAviRhbdHsuVdt10w8i7VITStB8liQFDCu4o+W9VYEVYvfureCj1tMndGVGDp4y+FFI5eNtj2YHOHndpmEHSNVAd1fIfkdbumOnoPCkOm\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "        \"image\": \"\",\n" +
                "        \"minAmount\": \"1\",\n" +
                "        \"travelertype\": \"CHD\",\n" +
                "        \"htmlMemo\": \"\",\n" +
                "        \"available\": \"2147483647.00\",\n" +
                "        \"memo\": \"\",\n" +
                "        \"supplierPath\": \"agencies/954\",\n" +
                "        \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "        \"type\": \"Produto\",\n" +
                "        \"additionalLanguagesInfo\": [],\n" +
                "        \"path\": \"products/47245\",\n" +
                "        \"definePrecoNaVenda\": \"false\",\n" +
                "        \"custo\": 5400,\n" +
                "        \"schedules\": [],\n" +
                "        \"faixaetaria\": \"CHD\",\n" +
                "        \"name\": \"Ingresso CHD Individual\",\n" +
                "        \"currency\": \"BRL\",\n" +
                "        \"id\": \"47245\",\n" +
                "        \"shortName\": \"Ingresso CHD Individual\",\n" +
                "        \"value\": \"5400\",\n" +
                "        \"token\": \"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqf/ddvjpNsatLjsGep8IV2TnVITStB8liQFDCu4o+W9VYEVYvfureCj1tMndGVGDp4y+FFI5eNtj2YHOHndpmEHQep/Z184YETscKZ5VOH0Hk\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "        \"image\": \"\",\n" +
                "        \"minAmount\": \"1\",\n" +
                "        \"travelertype\": \"UNICO\",\n" +
                "        \"htmlMemo\": \"\",\n" +
                "        \"available\": \"99.00\",\n" +
                "        \"memo\": \"\",\n" +
                "        \"supplierPath\": \"agencies/954\",\n" +
                "        \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "        \"type\": \"Combo\",\n" +
                "        \"additionalLanguagesInfo\": [],\n" +
                "        \"products\": [\n" +
                "            {\n" +
                "                \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "                \"image\": \"\",\n" +
                "                \"minAmount\": \"1\",\n" +
                "                \"select_date\": false,\n" +
                "                \"travelertype\": \"ADT\",\n" +
                "                \"amount\": \"1\",\n" +
                "                \"htmlMemo\": \"\",\n" +
                "                \"available\": \"99.00\",\n" +
                "                \"memo\": \"\",\n" +
                "                \"supplierPath\": \"agencies/954\",\n" +
                "                \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "                \"type\": \"Produto\",\n" +
                "                \"additionalLanguagesInfo\": [],\n" +
                "                \"path\": \"products/46700\",\n" +
                "                \"definePrecoNaVenda\": \"false\",\n" +
                "                \"schedules\": [],\n" +
                "                \"faixaetaria\": \"ADT\",\n" +
                "                \"name\": \"Ingresso Individual\",\n" +
                "                \"currency\": \"BRL\",\n" +
                "                \"id\": \"46700\",\n" +
                "                \"shortName\": \"Ingresso Individual\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "                \"image\": \"\",\n" +
                "                \"minAmount\": \"1\",\n" +
                "                \"select_date\": false,\n" +
                "                \"travelertype\": \"ADT\",\n" +
                "                \"amount\": \"1\",\n" +
                "                \"htmlMemo\": \"\",\n" +
                "                \"available\": \"99.00\",\n" +
                "                \"memo\": \"\",\n" +
                "                \"supplierPath\": \"agencies/954\",\n" +
                "                \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "                \"type\": \"Produto\",\n" +
                "                \"additionalLanguagesInfo\": [],\n" +
                "                \"path\": \"products/46700\",\n" +
                "                \"definePrecoNaVenda\": \"false\",\n" +
                "                \"schedules\": [],\n" +
                "                \"faixaetaria\": \"ADT\",\n" +
                "                \"name\": \"Ingresso Individual\",\n" +
                "                \"currency\": \"BRL\",\n" +
                "                \"id\": \"46700\",\n" +
                "                \"shortName\": \"Ingresso Individual\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "                \"image\": \"\",\n" +
                "                \"minAmount\": \"1\",\n" +
                "                \"select_date\": false,\n" +
                "                \"travelertype\": \"ADT\",\n" +
                "                \"amount\": \"1\",\n" +
                "                \"htmlMemo\": \"\",\n" +
                "                \"available\": \"99.00\",\n" +
                "                \"memo\": \"\",\n" +
                "                \"supplierPath\": \"agencies/954\",\n" +
                "                \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "                \"type\": \"Produto\",\n" +
                "                \"additionalLanguagesInfo\": [],\n" +
                "                \"path\": \"products/46700\",\n" +
                "                \"definePrecoNaVenda\": \"false\",\n" +
                "                \"schedules\": [],\n" +
                "                \"faixaetaria\": \"ADT\",\n" +
                "                \"name\": \"Ingresso Individual\",\n" +
                "                \"currency\": \"BRL\",\n" +
                "                \"id\": \"46700\",\n" +
                "                \"shortName\": \"Ingresso Individual\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"path\": \"productCombos/2111\",\n" +
                "        \"definePrecoNaVenda\": \"false\",\n" +
                "        \"custo\": 24300,\n" +
                "        \"schedules\": [],\n" +
                "        \"faixaetaria\": \"UNICO\",\n" +
                "        \"name\": \"Combo 3 Ingressos\",\n" +
                "        \"currency\": \"BRL\",\n" +
                "        \"id\": \"2111\",\n" +
                "        \"value\": \"24300\",\n" +
                "        \"token\": \"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqf8l4J1O+SNNXPGLQcjtmJmlKB50QAg7OZxf5c9UzBWRQdCdb1dITk8MN3wAzToY/Ax9hiU6BLe8K9YLYkKEpvogwv6bITyE6VX3/F5GxflLBLF9WGlQE3k3+gurZxqvNAw\\u003d\\u003d\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "        \"image\": \"\",\n" +
                "        \"minAmount\": \"1\",\n" +
                "        \"travelertype\": \"UNICO\",\n" +
                "        \"umaPessoaPorCombo\": true,\n" +
                "        \"htmlMemo\": \"\",\n" +
                "        \"available\": \"99.00\",\n" +
                "        \"memo\": \"\",\n" +
                "        \"supplierPath\": \"agencies/954\",\n" +
                "        \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "        \"type\": \"Combo\",\n" +
                "        \"additionalLanguagesInfo\": [],\n" +
                "        \"products\": [\n" +
                "            {\n" +
                "                \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "                \"image\": \"\",\n" +
                "                \"minAmount\": \"1\",\n" +
                "                \"select_date\": true,\n" +
                "                \"travelertype\": \"ADT\",\n" +
                "                \"amount\": \"1\",\n" +
                "                \"htmlMemo\": \"\",\n" +
                "                \"available\": \"99.00\",\n" +
                "                \"memo\": \"\",\n" +
                "                \"supplierPath\": \"agencies/954\",\n" +
                "                \"dates\": [\n" +
                "                    \"01/06/2022\",\n" +
                "                    \"02/06/2022\",\n" +
                "                    \"03/06/2022\",\n" +
                "                    \"04/06/2022\",\n" +
                "                    \"05/06/2022\",\n" +
                "                    \"06/06/2022\",\n" +
                "                    \"07/06/2022\",\n" +
                "                    \"08/06/2022\",\n" +
                "                    \"09/06/2022\",\n" +
                "                    \"10/06/2022\",\n" +
                "                    \"11/06/2022\",\n" +
                "                    \"12/06/2022\",\n" +
                "                    \"13/06/2022\",\n" +
                "                    \"14/06/2022\",\n" +
                "                    \"15/06/2022\"\n" +
                "                ],\n" +
                "                \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "                \"type\": \"Produto\",\n" +
                "                \"additionalLanguagesInfo\": [],\n" +
                "                \"path\": \"products/46700\",\n" +
                "                \"definePrecoNaVenda\": \"false\",\n" +
                "                \"schedules\": [],\n" +
                "                \"faixaetaria\": \"ADT\",\n" +
                "                \"name\": \"Ingresso Individual\",\n" +
                "                \"currency\": \"BRL\",\n" +
                "                \"id\": \"46700\",\n" +
                "                \"shortName\": \"Ingresso Individual\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "                \"image\": \"\",\n" +
                "                \"minAmount\": \"1\",\n" +
                "                \"select_date\": true,\n" +
                "                \"travelertype\": \"ADT\",\n" +
                "                \"amount\": \"1\",\n" +
                "                \"htmlMemo\": \"\",\n" +
                "                \"available\": \"99.00\",\n" +
                "                \"memo\": \"\",\n" +
                "                \"supplierPath\": \"agencies/954\",\n" +
                "                \"dates\": [\n" +
                "                    \"01/06/2022\",\n" +
                "                    \"02/06/2022\",\n" +
                "                    \"03/06/2022\",\n" +
                "                    \"04/06/2022\",\n" +
                "                    \"05/06/2022\",\n" +
                "                    \"06/06/2022\",\n" +
                "                    \"07/06/2022\",\n" +
                "                    \"08/06/2022\",\n" +
                "                    \"09/06/2022\",\n" +
                "                    \"10/06/2022\",\n" +
                "                    \"11/06/2022\",\n" +
                "                    \"12/06/2022\",\n" +
                "                    \"13/06/2022\",\n" +
                "                    \"14/06/2022\",\n" +
                "                    \"15/06/2022\"\n" +
                "                ],\n" +
                "                \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "                \"type\": \"Produto\",\n" +
                "                \"additionalLanguagesInfo\": [],\n" +
                "                \"path\": \"products/46700\",\n" +
                "                \"definePrecoNaVenda\": \"false\",\n" +
                "                \"schedules\": [],\n" +
                "                \"faixaetaria\": \"ADT\",\n" +
                "                \"name\": \"Ingresso Individual\",\n" +
                "                \"currency\": \"BRL\",\n" +
                "                \"id\": \"46700\",\n" +
                "                \"shortName\": \"Ingresso Individual\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"supplierName\": \"NXS - Operadora Demo\",\n" +
                "                \"image\": \"\",\n" +
                "                \"minAmount\": \"1\",\n" +
                "                \"select_date\": true,\n" +
                "                \"travelertype\": \"ADT\",\n" +
                "                \"amount\": \"1\",\n" +
                "                \"htmlMemo\": \"\",\n" +
                "                \"available\": \"99.00\",\n" +
                "                \"memo\": \"\",\n" +
                "                \"supplierPath\": \"agencies/954\",\n" +
                "                \"dates\": [\n" +
                "                    \"01/06/2022\",\n" +
                "                    \"02/06/2022\",\n" +
                "                    \"03/06/2022\",\n" +
                "                    \"04/06/2022\",\n" +
                "                    \"05/06/2022\",\n" +
                "                    \"06/06/2022\",\n" +
                "                    \"07/06/2022\",\n" +
                "                    \"08/06/2022\",\n" +
                "                    \"09/06/2022\",\n" +
                "                    \"10/06/2022\",\n" +
                "                    \"11/06/2022\",\n" +
                "                    \"12/06/2022\",\n" +
                "                    \"13/06/2022\",\n" +
                "                    \"14/06/2022\",\n" +
                "                    \"15/06/2022\"\n" +
                "                ],\n" +
                "                \"cancellationPolicies\": \"Cancelamento gratuito até 24 horas antes da data de uso.\\nCancelamento no dia do agendamento multa de 50%.\\nApós a data de agendamento multa de 100%.\",\n" +
                "                \"type\": \"Produto\",\n" +
                "                \"additionalLanguagesInfo\": [],\n" +
                "                \"path\": \"products/46700\",\n" +
                "                \"definePrecoNaVenda\": \"false\",\n" +
                "                \"schedules\": [],\n" +
                "                \"faixaetaria\": \"ADT\",\n" +
                "                \"name\": \"Ingresso Individual\",\n" +
                "                \"currency\": \"BRL\",\n" +
                "                \"id\": \"46700\",\n" +
                "                \"shortName\": \"Ingresso Individual\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"path\": \"productCombos/2112\",\n" +
                "        \"definePrecoNaVenda\": \"false\",\n" +
                "        \"custo\": 14647,\n" +
                "        \"schedules\": [],\n" +
                "        \"faixaetaria\": \"UNICO\",\n" +
                "        \"name\": \"Combo 3 Dias de Acesso\",\n" +
                "        \"currency\": \"BRL\",\n" +
                "        \"id\": \"2112\",\n" +
                "        \"value\": \"14648\",\n" +
                "        \"token\": \"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqfydx5TXW9G42IIYCQL8QeupKB50QAg7OZxf5c9UzBWRQdCdb1dITk8MN3wAzToY/Ax9hiU6BLe8K9YLYkKEpvoitaNhbmpDUkTqZU5DaexGpYVWbawXsMNh+yrVpYUukvw\\u003d\\u003d\"\n" +
                "    }\n" +
                "]";
    }*/

    public List<Date> montaRangedatasPesquisa(WSDisponibilidadeIngressoRQ dispRQ) {
        List<Date> result = new ArrayList<>();

        int qtDias = Utils.diferencaEmDias(dispRQ.getDtInicio(), dispRQ.getDtFim());
        for (int i = 0; i <= qtDias; i++) {
            result.add(Utils.addDias(dispRQ.getDtInicio(), i));
        }

        return result;
    }

    private List<WSIngressoPesquisa> montaPesquisarIngressoResult(List<WSIngressoPesquisa> pesquisaList, WSDisponibilidadeIngressoRQ dispRQ, List<GetProductsByDateRS> getProductsByDateRSList) throws ErrorException {
        List<WSIngressoPesquisa> result = new ArrayList<>();
        int sqPesquisa = 0;
        WSIngressoPesquisa ingressoPesquisa = null;

        for (GetProductsByDateRS productsByDateRS : getProductsByDateRSList) {
            ingressoPesquisa = null;
            sqPesquisa++;
            if (!(productsByDateRS.getName().toUpperCase().contains("COMBO") && productsByDateRS.getName().toUpperCase().contains("INGRESSOS"))) {
                ingressoPesquisa = pesquisaList.stream()
                        .filter(wsi -> wsi.getIngresso().getCdServico().equals(productsByDateRS.getPath()))
                        .findFirst()
                        .orElse(null);
                if (ingressoPesquisa == null) {
                    ingressoPesquisa = montaIngressoPesquisa(sqPesquisa, dispRQ, productsByDateRS);
                    result.add(ingressoPesquisa);
                } else {
                    ingressoPesquisa.setIngressoModalidadeList(ingressoPesquisaIngressoModalidadeList(ingressoPesquisa, dispRQ, productsByDateRS));
                    result.add(ingressoPesquisa);
                }
                if (ingressoPesquisa.getIngresso().getNmServico().toUpperCase().contains("INGRESSO")
                        && ingressoPesquisa.getIngresso().getNmServico().toUpperCase().contains("INDIVIDUAL")
                        && !ingressoPesquisa.getIngresso().getNmServico().toUpperCase().contains("FORMULÁRIO")
                        && !ingressoPesquisa.getIngresso().getNmServico().toUpperCase().contains("CHD")) {
                    for (GetProductsByDateRS searchCombo : getProductsByDateRSList) {
                        if (searchCombo.getName().toUpperCase().contains("COMBO") && searchCombo.getName().toUpperCase().contains("INGRESSOS")) {
                            if (searchCombo.getProducts() != null && !searchCombo.getProducts().isEmpty()) {
                                if (searchCombo.getProducts().get(0).getName().toUpperCase().contains("INGRESSO")
                                        && searchCombo.getProducts().get(0).getName().toUpperCase().contains("INDIVIDUAL")) {
                                    WSIngressoModalidade modalidade = ingressoPesquisa.getIngressoModalidadeList().stream()
                                            .filter(wsm -> {
                                                return wsm.getCdModalidade().equals(searchCombo.getPath());
                                            })
                                            .findFirst()
                                            .orElse(null);

                                    if (modalidade != null) {
                                        modalidade.getUtilizacaoDatasList().add(UtilsWS.montaWSIngressoUtilizacao(dispRQ, searchCombo));
                                    } else {
                                        ingressoPesquisa.getIngressoModalidadeList().addAll(UtilsWS.montaIngressoModalidadeList(ingressoPesquisa, dispRQ, searchCombo));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }


        return result;
    }

    private WSIngressoPesquisa montaIngressoPesquisa(int sqPesquisa, WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {

        WSIngressoPesquisa result = new WSIngressoPesquisa(sqPesquisa,
                UtilsWS.montaIngresso(dispRQ.getIntegrador(), dispRQ.getReservaNomeList(), productsByDateRS),
                ingressoPesquisaIngressoModalidadeList(dispRQ, productsByDateRS));

        return result;
    }

    private List<WSIngressoModalidade> ingressoPesquisaIngressoModalidadeList(WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {
        return UtilsWS.montaIngressoModalidadeList(null, dispRQ, productsByDateRS);
    }

    private List<WSIngressoModalidade> ingressoPesquisaIngressoModalidadeList(WSIngressoPesquisa ingressoPesquisa, WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {
        return UtilsWS.montaIngressoModalidadeList(ingressoPesquisa, dispRQ, productsByDateRS);
    }

    public GetProductsByDateRQ montaRequestGetProductsByDateRQ(WSIntegrador integrador, Date dtInicialIngresso) throws ErrorException {
        Date dtInicio = Optional.ofNullable(dtInicialIngresso)
                .orElseThrow(() -> new ErrorException("Data Inicio não informada"));

        try {
            return new GetProductsByDateRQ(UtilsWS.montaCredenciaisNovaXS(integrador))
                    .setDate(UtilsWS.montaDataNovaxs(dtInicio));

        } catch (NullPointerException ex) {
            throw new ErrorException(integrador, DisponibilidadeWS.class, "disponibilidade", WSMensagemErroEnum.SDI, "Erro ao pesquisar atividades", WSIntegracaoStatusEnum.NEGADO, ex);
        }
    }


}

package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSPaxTipoEnum;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoPesquisa;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.model.GetProductsByDateRQ;
import br.com.infotera.it.novaxs.model.GetProductsByDateRS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * @Author Lucas
 **/

@Service
public class DisponibilidadeWS {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private NovaxsClient novaxsClient;


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
            if (rangedatasPesquisa.size() > 14) {
                throw new ErrorException("Range de datas na pesquisa invalido: Numero máximo permitido é 14 dias");
            }
            for (Date dtPesquisa : rangedatasPesquisa) {
                WSDisponibilidadeIngressoRQ ingressoRQ = new WSDisponibilidadeIngressoRQ();
                ingressoRQ.setIntegrador(integrador);
                ingressoRQ.setReservaNomeList(reservaNomeList);
                ingressoRQ.setDtInicio(dtPesquisa);
                ingressoRQ.setDtFim(dtPesquisa);

                try {
                    List<GetProductsByDateRS> getProductsByDateRSList = novaxsClient.getProductsByDateRQ(ingressoRQ.getIntegrador(),
                            montaRequestGetProductsByDateRQ(ingressoRQ.getIntegrador(), ingressoRQ.getDtInicio()));
                    if (getProductsByDateRSList != null) {
                        if (!getProductsByDateRSList.isEmpty()) {
                            if (isContemCriancaNaPesquisa(dispRQ)) {
                                List<GetProductsByDateRS> combosAdultoCrianca = montaCombosAdultoCrianca(dispRQ, getProductsByDateRSList);
                                result = montaPesquisarIngressoResult(result, ingressoRQ, combosAdultoCrianca, true);
                            } else {
                                result = montaPesquisarIngressoResult(result, ingressoRQ, getProductsByDateRSList, false);
                            }

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

    public List<Date> montaRangedatasPesquisa(WSDisponibilidadeIngressoRQ dispRQ) {
        List<Date> result = new ArrayList<>();

        int qtDias = Utils.diferencaEmDias(dispRQ.getDtInicio(), dispRQ.getDtFim());
        for (int i = 0; i <= qtDias; i++) {
            result.add(Utils.addDias(dispRQ.getDtInicio(), i));
        }

        return result;
    }

    private List<WSIngressoPesquisa> montaPesquisarIngressoResult(List<WSIngressoPesquisa> pesquisaList, WSDisponibilidadeIngressoRQ dispRQ, List<GetProductsByDateRS> getProductsByDateRSList, boolean contemCriancaNaPesquisa) throws ErrorException {
        List<WSIngressoPesquisa> result = new ArrayList<>();
        int sqPesquisa = 0;
        WSIngressoPesquisa ingressoPesquisa = null;

        for (GetProductsByDateRS productsByDateRS : getProductsByDateRSList) {
            ingressoPesquisa = null;
            sqPesquisa++;
            if (!contemCriancaNaPesquisa && productsByDateRS.getName().contains("CHD")) {
                continue;
            } else if (productsByDateRS.getName().toUpperCase().contains("Dias".toUpperCase())
                    && productsByDateRS.getName().toUpperCase().contains("acesso".toUpperCase())) {
                continue;
            } else {
                if (productsByDateRS.getName().toUpperCase().contains("INFOTERA")) {
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
                } else {
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
                            if (UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.ADT) >= 3) {
                                for (GetProductsByDateRS searchCombo : getProductsByDateRSList) {
                                    if (searchCombo.getName().toUpperCase().contains("COMBO") && searchCombo.getName().toUpperCase().contains("INGRESSOS")) {
                                        if (searchCombo.getProducts() != null && !searchCombo.getProducts().isEmpty()) {
                                            if (searchCombo.getProducts().get(0).getName().toUpperCase().contains("INGRESSO")
                                                    && searchCombo.getProducts().get(0).getName().toUpperCase().contains("INDIVIDUAL")) {
                                                WSIngressoModalidade modalidade = ingressoPesquisa.getIngressoModalidadeList().stream()
                                                        .filter(wsm -> {
                                                            return wsm.getCdModalidade().contains(searchCombo.getPath());
                                                        })
                                                        .findFirst()
                                                        .orElse(null);

                                                if (modalidade != null) {
                                                    String path = productsByDateRS.getPath() + "-" + searchCombo.getPath();
                                                    searchCombo.setPath(path);
                                                    modalidade.getUtilizacaoDatasList().add(UtilsWS.montaWSIngressoUtilizacao(dispRQ, searchCombo));
                                                } else {
                                                    String path = productsByDateRS.getPath() + "-" + searchCombo.getPath();
                                                    searchCombo.setPath(path);
                                                    ingressoPesquisa.getIngressoModalidadeList().addAll(UtilsWS.montaIngressoModalidadeList(ingressoPesquisa, dispRQ, searchCombo));
                                                }
                                            }
                                        }
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

    private boolean isContemCriancaNaPesquisa(WSDisponibilidadeIngressoRQ dispRQ) {
        boolean contemCriancaNaPesquisa = dispRQ.getReservaNomeList().stream()
                .anyMatch(reservaNome -> {
                    if (reservaNome.getQtIdade() <= 12 || reservaNome.getPaxTipo().isChd()) {
                        return true;
                    }
                    return false;
                });
        return contemCriancaNaPesquisa;
    }

    private List<GetProductsByDateRS> montaCombosAdultoCrianca(WSDisponibilidadeIngressoRQ dispRQ, List<GetProductsByDateRS> resultadoPesquisaNovaXS) {
        List<GetProductsByDateRS> result = new ArrayList<>();
        GetProductsByDateRS crianca = null;
        if (resultadoPesquisaNovaXS != null) {
            for (GetProductsByDateRS rs : resultadoPesquisaNovaXS) {
                if (rs.getName().toUpperCase().contains("CHD")) {
                    crianca = rs;
                }
            }
            if (crianca != null) {
                for (GetProductsByDateRS rs : resultadoPesquisaNovaXS) {
                    if (rs.getName().toUpperCase().contains("COMBO")
                            && rs.getName().toUpperCase().contains("3")
                            && rs.getName().toUpperCase().contains("INGRESSOS")
                            && UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.ADT) != 3) {
                        continue;
                    } else if (rs.getName().toUpperCase().contains("INGRESSO")
                            && rs.getName().toUpperCase().contains("INDIVIDUAL")
                            && !rs.getName().toUpperCase().contains("FORMULÁRIO")
                            && UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.ADT) == 3) {
                        boolean pularIngressoIndividual = false;
                        for (GetProductsByDateRS productsByDateRS : resultadoPesquisaNovaXS) {
                            if (productsByDateRS.getName().toUpperCase().contains("COMBO")
                                    && productsByDateRS.getName().toUpperCase().contains("3")
                                    && productsByDateRS.getName().toUpperCase().contains("INGRESSOS")) {
                                pularIngressoIndividual = true;
                            }
                        }
                        if (pularIngressoIndividual) {
                            continue;
                        }
                    }
                    if (!rs.getName().toUpperCase().contains("CHD")
                            && !rs.getName().toUpperCase().contains("DIAS")
                            && !rs.getName().toUpperCase().contains("ACESSO")) {
                        result.add(montaComboAdultoCrianca(dispRQ, crianca, rs));
                    }
                }
            }
        }
        return result;
    }

    /*path do objeto origem + path do objeto da criança*/
    private GetProductsByDateRS montaComboAdultoCrianca(WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS crianca, GetProductsByDateRS rs) {
        GetProductsByDateRS comboAdultoCrianca = new GetProductsByDateRS()
                .setPath(rs.getPath() + "-" + crianca.getPath())
                .setCancellationPolicies(rs.getCancellationPolicies() + "\n Politicas de cancelamento Ingresso criança: " + crianca.getCancellationPolicies())
                .setImage(rs.getImage())
                .setValue(montaValueCombo(dispRQ, crianca, rs))
                .setId(rs.getId() + "-" + crianca.getId())
                .setType(rs.getType())
                .setCurrency(rs.getCurrency());
        if (rs.getSchedules() != null) {
            comboAdultoCrianca.setSchedules(rs.getSchedules());
        }
        if (rs.getName().toUpperCase().contains("COMBO")
                && rs.getName().toUpperCase().contains("3")
                && rs.getName().toUpperCase().contains("INGRESSOS")) {

            comboAdultoCrianca
                    .setShortName("COMBO INFOTERA " + rs.getName() + " Adulto "
                            + UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.CHD) + "x Crianca")
                    .setName("COMBO INFOTERA " + rs.getName() + " Adulto "
                            + UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.CHD) + "x Crianca");
        } else {
            comboAdultoCrianca
                    .setShortName("COMBO INFOTERA " + rs.getName() + " " + UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.ADT) + "x Adulto "
                            + UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.CHD) + "x Crianca")
                    .setName("COMBO INFOTERA " + rs.getName() + " " + UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.ADT) + "x Adulto "
                            + UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.CHD) + "x Crianca");
        }
        return comboAdultoCrianca;
    }

    private String montaValueCombo(WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS crianca, GetProductsByDateRS rs) {
        double vlAdt = 0;
        if (!rs.getName().toUpperCase().contains("COMBO")) {
            vlAdt = Double.parseDouble(rs.getValue()) * UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.ADT);
        } else {
            vlAdt = Double.parseDouble(rs.getValue());
        }
        double vlChd = Double.parseDouble(crianca.getValue()) * UtilsWS.montaQtAdultos_QtCriancas(dispRQ.getReservaNomeList()).get(WSPaxTipoEnum.CHD);
        return String.valueOf(vlAdt + vlChd);
    }

    private WSIngressoPesquisa montaIngressoPesquisa(int sqPesquisa, WSDisponibilidadeIngressoRQ dispRQ, GetProductsByDateRS productsByDateRS) throws ErrorException {

        WSIngressoPesquisa result = new WSIngressoPesquisa(sqPesquisa,
                UtilsWS.montaIngresso(dispRQ.getIntegrador(), dispRQ, productsByDateRS),
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

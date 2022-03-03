package br.com.infotera.it.novaxs.services;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoPesquisa;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.it.novaxs.client.NovaxsClient;
import br.com.infotera.it.novaxs.utils.Parametro;
import br.com.infotera.it.novaxs.utils.UtilsWS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author Lucas
 **/
@Service
public class TarifarWS {

    @Autowired
    private NovaxsClient novaxsClient;
    @Autowired
    private DisponibilidadeWS disponibilidadeWS;
    @Autowired
    private ObjectMapper objectMapper;

    public WSTarifarServicoRS tarifar(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        WSReservaServico reservaServico = null;
        try {
            reservaServico = montaProdutoTarifado(tarifarServicoRQ, montaProdutoReferencia(tarifarServicoRQ));
        } catch (ErrorException ex) {
            tarifarServicoRQ.getIntegrador().setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            tarifarServicoRQ.getIntegrador().setDsMensagem(ex.getMessage());
            ex.setIntegrador(tarifarServicoRQ.getIntegrador());
            throw ex;
        } catch (NullPointerException ex) {
            throw new ErrorException(tarifarServicoRQ.getIntegrador(), TarifarWS.class, "tarifar", WSMensagemErroEnum.SPR, "Erro no processo de tarifar : NullpointerException", WSIntegracaoStatusEnum.NEGADO, ex);
        }

        return new WSTarifarServicoRS(reservaServico, tarifarServicoRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }

    private WSReservaServico montaProdutoTarifado(WSTarifarServicoRQ tarifarServicoRQ, Parametro produtoReferencia) throws ErrorException {
        WSReservaServico result = null;
        WSIngressoPesquisa ingressoDisp;
        WSIngresso ingressoTarifado;
        if (tarifarServicoRQ.getReservaServico().getServico().getDtServico().toString().equals(produtoReferencia.getDt())) {
            WSDisponibilidadeIngressoRQ ingressoRQ = new WSDisponibilidadeIngressoRQ();
            ingressoRQ.setIntegrador(tarifarServicoRQ.getIntegrador());
            ingressoRQ.setReservaNomeList(tarifarServicoRQ.getReservaServico().getServico().getReservaNomeList());
            ingressoRQ.setDtInicio(tarifarServicoRQ.getReservaServico().getServico().getDtServico());
            ingressoRQ.setDtFim(tarifarServicoRQ.getReservaServico().getServico().getDtServico());

            String[] cdReferencia = produtoReferencia.getCd().split("-");
            List<WSIngressoPesquisa> listIngressosDisp = disponibilidadeWS.pesquisarIngresso(ingressoRQ);
            ingressoDisp = listIngressosDisp.stream()
                    .filter(ingressoPesquisa -> {
                        if (cdReferencia.length > 1) {
                            if (ingressoPesquisa.getIngresso().getCdServico().contains(cdReferencia[0])) {
                                for (WSIngressoModalidade m :ingressoPesquisa.getIngressoModalidadeList()){
                                    if (m.getCdModalidade().contains(cdReferencia[1])){
                                        return true;
                                    }
                                }
                            }
                            return false;
                        } else {
                            return ingressoPesquisa.getIngresso().getCdServico().contains(cdReferencia[0]);
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> new ErrorException("Nenhum Ingresso encontrado para realizar o tarifar "));

            ingressoTarifado = ingressoDisp.getIngresso();

            if (ingressoTarifado != null) {
                String[] cdReferenciaModalidade = produtoReferencia.getCdModalidade().split("-");
                ingressoTarifado.setIngressoModalidade(ingressoDisp.getIngressoModalidadeList().stream()
                        .filter(wsIngressoModalidade -> {
                            if (wsIngressoModalidade.getNmModalidade().contains(produtoReferencia.getNomeModalidade())) {
                                if (cdReferenciaModalidade.length > 1){
                                     return wsIngressoModalidade.getCdModalidade().contains(cdReferenciaModalidade[1]);
                                } else {
                                    return wsIngressoModalidade.getCdModalidade().contains(cdReferenciaModalidade[0]);
                                }
                            }
                            return false;
                        })
                        .findFirst()
                        .orElseThrow(() -> new ErrorException("Modalidade do Ingresso não encontrada para realizar a tarifa ")));

            }

            ingressoTarifado.setDsParametro(montaDsParametroTarifado(ingressoTarifado, ingressoRQ));
            ingressoTarifado.setNmServico(ingressoTarifado.getIngressoModalidade().getNmModalidade());
            ingressoTarifado.setTarifa(ingressoTarifado.getIngressoModalidade().getTarifa());

            result = new WSReservaServico(tarifarServicoRQ.getIntegrador(),
                    WSServicoTipoEnum.INGRESSO,
                    ingressoTarifado,
                    null,
                    null,
                    montaDsParametroTarifado(ingressoTarifado, ingressoRQ));

        }
        return result;
    }

    private String montaDsParametroTarifado(WSIngresso ingressoTarifado, WSDisponibilidadeIngressoRQ ingressoRQ) {
        return new Parametro()
                .setCd(ingressoTarifado.getCdServico())
                .setCdModalidade(ingressoTarifado.getIngressoModalidade().getCdModalidade())
                .setNomeModalidade(ingressoTarifado.getIngressoModalidade().getNmModalidade())
                .setDt(ingressoRQ.getDtInicio().toString())
                .toString();

    }

    private Parametro montaProdutoReferencia(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        Parametro result;
        String parametro;
        try {
            parametro = (tarifarServicoRQ.getReservaServico().getDsParametro() != null ?
                    tarifarServicoRQ.getReservaServico().getDsParametro() :
                    tarifarServicoRQ.getReservaServico().getServico().getDsParametro());

            result = Optional.ofNullable(UtilsWS.converterDSParametro(parametro))
                    .orElseThrow(() -> new ErrorException("Conversão do parametro para tarifar esta null"));

        } catch (NullPointerException ex) {
            throw ex;
        } catch (ErrorException ex) {
            throw new ErrorException("Erro a o converter o dsParametro para o produto Referência");
        }
        return result;
    }


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.it.novaxs.controller;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.hotel.WSAlteraReservaRQ;
import br.com.infotera.common.hotel.WSAlteraReservaRS;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.reserva.rqrs.WSReservarRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRS;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRS;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.common.util.LogWS;
import br.com.infotera.it.novaxs.NovaxsApplication;
import br.com.infotera.it.novaxs.services.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lucas
 */


@RestController
public class ApiController {

    @Autowired
    private PreReservarWS preReservarWS;
    @Autowired
    private ReservarWS reservaWS;
    @Autowired
    private ConsultarWS consultarWS;
    @Autowired
    private PreCancelarWS preCancelarWS;
    @Autowired
    private CancelarWS cancelarWS;
    @Autowired
    private RelatorioWS relatorioWS;
    @Autowired
    private PreAlterarReservaWS preAlterarReservaWS;
    @Autowired
    private AlterarReservaWS alterarReservaWS;
    @Autowired
    private TarifarWS tarifaWS;
    @Autowired
    private ConfirmarWS confirmarWS;
    @Autowired
    private DetalheIngressoWS detalheIngressoWS;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/ola", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String ola() {
        return "Ola NovaXS " + (NovaxsApplication.nrVersao != null ? NovaxsApplication.nrVersao : "");
    }


    @RequestMapping(value = "/detalheIngresso", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String detalheIngresso(@RequestBody String jsonRQ) {
        WSDetalheIngressoRQ wsRQ = gson.fromJson(jsonRQ, WSDetalheIngressoRQ.class);
        WSDetalheIngressoRS result = null;
        wsRQ.getIntegrador().setDsMetodo("detalheIngresso");
        try {
            result = detalheIngressoWS.detalheIngresso(wsRQ);
        } catch (ErrorException ex) {
            result = new WSDetalheIngressoRS(ex.getIntegrador(), null);
        } catch (Exception ex) {
            result = new WSDetalheIngressoRS(new ErrorException(wsRQ.getIntegrador(), ApiController.class, "preReservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador(), null);
        } finally {
            LogWS.gerarLog(result.getIntegrador(), jsonRQ, result);
        }
        return (gson.toJson(result));
    }

    @RequestMapping(value = "/preReservar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String preReservar(@RequestBody String jsonRQ) {
        WSPreReservarRQ wsRQ = gson.fromJson(jsonRQ, WSPreReservarRQ.class);
        WSPreReservarRS result = null;
        wsRQ.getIntegrador().setDsMetodo("preReservar");
        try {
            result = preReservarWS.preReservar(wsRQ);
        } catch (ErrorException ex) {
            result = new WSPreReservarRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            result = new WSPreReservarRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "preReservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(result.getIntegrador(), jsonRQ, result);
        }
        return (gson.toJson(result));
    }

    @RequestMapping(value = "/tarifar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String tarifar(@RequestBody String jsonRQ) {
        WSTarifarServicoRS result = null;
        WSTarifarServicoRQ wsRQ = gson.fromJson(jsonRQ, WSTarifarServicoRQ.class);
        wsRQ.getIntegrador().setDsMetodo("tarifar");
        try {
            result = tarifaWS.tarifar(wsRQ);
        } catch (ErrorException ex) {
            result = new WSTarifarServicoRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            result = new WSTarifarServicoRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "tarifar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(result.getIntegrador(), jsonRQ, result);
        }
        return (gson.toJson(result));
    }

    @RequestMapping(value = "/reservar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String reservar(@RequestBody String jsonRQ) {
        WSReservarRS result = null;
        WSReservarRQ wsRQ = gson.fromJson(jsonRQ, WSReservarRQ.class);
        wsRQ.getIntegrador().setDsMetodo("reservar");
        try {
            result = reservaWS.reservar(wsRQ);
        } catch (ErrorException ex) {
            result = new WSReservarRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            result = new WSReservarRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "reservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(result.getIntegrador(), jsonRQ, result);
        }

        return (gson.toJson(result));
    }

    @RequestMapping(value = "/confirmar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String confirmar(@RequestBody String jsonRQ) {
        WSReservaRS result = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        wsRQ.getIntegrador().setDsMetodo("confirmar");
        try {
            result = confirmarWS.confirmar(wsRQ);
        } catch (ErrorException ex) {
            result = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            result = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "confirmar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(result.getIntegrador(), jsonRQ, result);
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "/consultar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String consultar(@RequestBody String jsonRQ) {
        WSReservaRS result = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        wsRQ.getIntegrador().setDsMetodo("consultar");
        try {
            result = consultarWS.consultar(wsRQ, false);
        } catch (ErrorException ex) {

            result = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {

            result = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "consultar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(result.getIntegrador(), jsonRQ, result);
        }

        return (gson.toJson(result));
    }

    @RequestMapping(value = "/preCancelar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String preCancelar(@RequestBody String jsonRQ) {
        WSReservaRS result = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        wsRQ.getIntegrador().setDsMetodo("preCancelar");
        try {
            result = preCancelarWS.preCancelar(wsRQ);
        } catch (ErrorException ex) {
            result = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            result = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "preCancelar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(result.getIntegrador(), jsonRQ, result);
        }
        return (gson.toJson(result));
    }

    @RequestMapping(value = "/cancelar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String cancelar(@RequestBody String jsonRQ) {
        WSReservaRS result = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        wsRQ.getIntegrador().setDsMetodo("cancelar");
        try {
            result = cancelarWS.cancelar(wsRQ);
        } catch (ErrorException ex) {

            result = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {

            result = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "cancelar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(result.getIntegrador(), jsonRQ, result);
        }

        return (gson.toJson(result));
    }

    @RequestMapping(value = "/relatorio", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String relatorio(@RequestBody String jsonRQ) {
        WSReservaRelatorioRS result = null;
        WSReservaRelatorioRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRelatorioRQ.class);
        wsRQ.getIntegrador().setDsMetodo("relatorio");
        try {
            result = relatorioWS.relatorio(wsRQ);
        } catch (ErrorException ex) {

            result = new WSReservaRelatorioRS(null, ex.getIntegrador());
        } catch (Exception ex) {

            result = new WSReservaRelatorioRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "relatorio", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(result.getIntegrador(), jsonRQ, result);
        }
        return (gson.toJson(result));
    }


}


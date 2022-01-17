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
import br.com.infotera.common.hotel.rqrs.WSTarifarHotelRQ;
import br.com.infotera.common.hotel.rqrs.WSTarifarHotelRS;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.reserva.rqrs.WSReservarRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRS;
import br.com.infotera.common.util.Utils;
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
    private Gson gson;

    @RequestMapping(value = "/ola", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String ola() {
        return "Ola NovaXS" + (NovaxsApplication.nrVersao != null ? NovaxsApplication.nrVersao : "");
    }

    @RequestMapping(value = "/preReservar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String preReservar(@RequestBody String jsonRQ) {

        WSPreReservarRQ wsRQ = gson.fromJson(jsonRQ, WSPreReservarRQ.class);
        WSPreReservarRS result = null;
        boolean stGerarErro = false;
        try {
            result = preReservarWS.preReservar(wsRQ, false);
        } catch (ErrorException ex) {
            stGerarErro = true;
            result = new WSPreReservarRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = true;
            result = new WSPreReservarRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "preReservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
        }
        try {
            Utils.gerarLog(result.getIntegrador(), "preReservar", true, jsonRQ, stGerarErro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (gson.toJson(result));
    }

    @RequestMapping(value = "/tarifar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String tarifar(@RequestBody String jsonRQ) {
        WSTarifarHotelRS result = null;
        WSTarifarHotelRQ wsRQ = gson.fromJson(jsonRQ, WSTarifarHotelRQ.class);
        boolean stGerarErro = false;
        try {
            result = tarifaWS.tarifar(wsRQ);
        } catch (ErrorException ex) {
            stGerarErro = true;
            result = new WSTarifarHotelRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = true;
            result = new WSTarifarHotelRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "tarifar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        }
        try {
            Utils.gerarLog(result.getIntegrador(), "tarifar", true, jsonRQ, stGerarErro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (gson.toJson(result));
    }

    @RequestMapping(value = "/reservar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String reservar(@RequestBody String jsonRQ) {
        WSReservarRS result = null;
        WSReservarRQ wsRQ = gson.fromJson(jsonRQ, WSReservarRQ.class);
        boolean stGerarErro = false;
        try {
            result = reservaWS.reservar(wsRQ);
        } catch (ErrorException ex) {
            stGerarErro = true;
            result = new WSReservarRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = true;
            result = new WSReservarRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "reservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        }
        try {
            Utils.gerarLog(result.getIntegrador(), "reservar", true, jsonRQ, stGerarErro);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (gson.toJson(result));
    }

    @RequestMapping(value = "/confirmar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String confirmar(@RequestBody String jsonRQ) {
        WSReservaRS result = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        boolean stGerarErro = false;
        try {
            result = confirmarWS.confirmar(wsRQ);
        } catch (ErrorException ex) {
            stGerarErro = true;
            result = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = true;
            result = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "confirmar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        }
        try {
            Utils.gerarLog(result.getIntegrador(), "confirmar", true, jsonRQ, stGerarErro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "/consultar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String consultar(@RequestBody String jsonRQ) {
        WSReservaRS result = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        boolean stGerarErro = false;
        try {
            result = consultarWS.consultar(wsRQ, false);
        } catch (ErrorException ex) {
            stGerarErro = true;
            result = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = true;
            result = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "consultar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        }
        try {
            Utils.gerarLog(result.getIntegrador(), "consultar", true, jsonRQ, stGerarErro);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (gson.toJson(result));
    }

    @RequestMapping(value = "/preCancelar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String preCancelar(@RequestBody String jsonRQ) {
        WSReservaRS result = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        boolean stGerarErro = false;
        try {
            result = preCancelarWS.preCancelar(wsRQ);
        } catch (ErrorException ex) {
            stGerarErro = true;
            result = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = true;
            result = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "preCancelar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        }
        try {
            Utils.gerarLog(result.getIntegrador(), "preCancelar", true, jsonRQ, stGerarErro);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (gson.toJson(result));
    }

    @RequestMapping(value = "/cancelar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String cancelar(@RequestBody String jsonRQ) {
        WSReservaRS result = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        boolean stGerarErro = false;
        try {
            result = cancelarWS.cancelar(wsRQ);
        } catch (ErrorException ex) {
            stGerarErro = true;
            result = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = true;
            result = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "cancelar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        }
        try {
            Utils.gerarLog(result.getIntegrador(), "cancelar", true, jsonRQ, stGerarErro);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (gson.toJson(result));
    }

    @RequestMapping(value = "/relatorio", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String relatorio(@RequestBody String jsonRQ) {
        WSReservaRelatorioRS result = null;
        WSReservaRelatorioRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRelatorioRQ.class);
        boolean stGerarErro = false;
        try {
            result = relatorioWS.relatorio(wsRQ);
        } catch (ErrorException ex) {
            stGerarErro = true;
            result = new WSReservaRelatorioRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = true;
            result = new WSReservaRelatorioRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "relatorio", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        }
        try {
            Utils.gerarLog(result.getIntegrador(), "relatorio", true, jsonRQ, stGerarErro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (gson.toJson(result));
    }

    @RequestMapping(value = "/preAlterarReserva", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String preAlterarReserva(@RequestBody String jsonRQ) {
        WSPreAlterarRS result = null;
        WSPreAlterarRQ wsRQ = gson.fromJson(jsonRQ, WSPreAlterarRQ.class);
        boolean stGerarErro = false;
        try {
            result = preAlterarReservaWS.preAlterarReserva(wsRQ);
        } catch (ErrorException ex) {
            stGerarErro = true;
            result = new WSPreAlterarRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = true;
            result = new WSPreAlterarRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "preAlterarReserva", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        }
        try {
            Utils.gerarLog(result.getIntegrador(), "preAlterarReserva", true, jsonRQ, stGerarErro);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (gson.toJson(result));
    }

    @RequestMapping(value = "/alterarReserva", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String alterarReserva(@RequestBody String jsonRQ) {
        WSAlteraReservaRS result = null;
        WSAlteraReservaRQ wsRQ = gson.fromJson(jsonRQ, WSAlteraReservaRQ.class);
        boolean stGerarErro = false;
        try {
            result = alterarReservaWS.alterarReserva(wsRQ);
        } catch (ErrorException ex) {
            stGerarErro = true;
            result = new WSAlteraReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = true;
            result = new WSAlteraReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiController.class, "alterarReserva", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        }
        try {
            Utils.gerarLog(result.getIntegrador(), "alterarReserva", true, jsonRQ, stGerarErro);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (gson.toJson(result));
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.it.novaxs.controller;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.hotel.rqrs.WSDisponibilidadeHotelRQ;
import br.com.infotera.common.hotel.rqrs.WSDisponibilidadeHotelRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.*;

/**
 * @author Lucas
 */
@RestController
public class DisponibilidadeController {

    @Autowired
    private DisponibilidadeWS disponibilidadeWS;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/disponibilidade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String disponibilidade(@RequestBody String jsonRQ) {
        WSDisponibilidadeHotelRQ wsRQ = gson.fromJson(jsonRQ, WSDisponibilidadeHotelRQ.class);
        WSDisponibilidadeHotelRS wsRS = null;
        Callable<WSDisponibilidadeHotelRS> task = () -> {
            boolean stGerarErro = false;
            WSDisponibilidadeHotelRS result = null;
            try {
                result = disponibilidadeWS.disponibilidade(wsRQ, false, false);
            } catch (ErrorException ex) {
                stGerarErro = true;
                result = new WSDisponibilidadeHotelRS(null, ex.getIntegrador());
            } catch (Exception ex) {
                stGerarErro = true;
                result = new WSDisponibilidadeHotelRS(null, new ErrorException(wsRQ.getIntegrador(), DisponibilidadeController.class, "disponibilidade", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
            }
            try {
                Utils.gerarLog(result.getIntegrador(), "disponibilidade", true, jsonRQ, stGerarErro);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<WSDisponibilidadeHotelRS> future = executor.submit(task);

        try {
            wsRS = future.get(wsRQ.getIntegrador().getTimeoutSegundos(), TimeUnit.SECONDS);
        } catch (Exception e) {
            wsRS = new WSDisponibilidadeHotelRS(null, new ErrorException(wsRQ.getIntegrador(), DisponibilidadeController.class, "disponibilidade", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, e).getIntegrador());
        }
        return gson.toJson(wsRS);
    }

    @RequestMapping(value = "/teste", method = RequestMethod.GET)
    @ResponseBody
    public String teste2() {
        return null;
    }
}

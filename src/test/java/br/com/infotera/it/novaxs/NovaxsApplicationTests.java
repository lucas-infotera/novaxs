package br.com.infotera.it.novaxs;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRS;
import br.com.infotera.it.novaxs.controller.DisponibilidadeController;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
/*

@WebMvcTest(DisponibilidadeController.class)
@AutoConfigureMockMvc
class NovaxsApplicationTestsWithMock{


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DisponibilidadeWS disponibilidadeWS;

    @Test
    public void testeDisponibilidadeApiController() throws Exception {
        when(disponibilidadeWS.disponibilidade(new WSDisponibilidadeIngressoRQ())).thenReturn(new WSDisponibilidadeIngressoRS());
        this.mockMvc.perform(post("/disponibilidadeIngresso"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, Mock")));
    }


*/
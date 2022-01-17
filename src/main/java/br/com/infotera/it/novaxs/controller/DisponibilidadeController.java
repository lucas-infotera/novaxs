/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.it.novaxs.controller;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRS;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lucas
 */
@RestController
public class DisponibilidadeController {

    @Autowired
    private DisponibilidadeWS disponibilidadeWS;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/disponibilidadeIngresso", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String disponibilidade(@RequestBody String jsonRQ) {
        WSDisponibilidadeIngressoRQ wsRQ = gson.fromJson(jsonRQ, WSDisponibilidadeIngressoRQ.class);
        WSDisponibilidadeIngressoRS result = null;
        boolean stGerarErro = false;
        try {
            result = disponibilidadeWS.disponibilidade(wsRQ);
        } catch (ErrorException ex) {
            stGerarErro = false;
            result = new WSDisponibilidadeIngressoRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            stGerarErro = false;
            result = new WSDisponibilidadeIngressoRS(null, new ErrorException(wsRQ.getIntegrador(), DisponibilidadeController.class, "disponibilidade", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
        }
        try {
//            Utils.gerarLog(result.getIntegrador(), "disponibilidade", true, jsonRQ, stGerarErro);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return testeStringModelo();
    }

    @RequestMapping(value = "/teste", method = RequestMethod.GET)
    @ResponseBody
    public String teste2() {
        return null;
    }

    public String testeStringModelo() {
        return "{\n" +
                "    \"ingressoPesquisaList\": [\n" +
                "        {\n" +
                "            \"sqPesquisa\": 1,\n" +
                "            \"servicoTipo\": \"INGRESSO\",\n" +
                "            \"ingresso\": {\n" +
                "                \"servicoTipo\": \"INGRESSO\",\n" +
                "                \"cdServico\": \"E-MX1-A9CKNO0031\",\n" +
                "                \"nmServico\": \"Tour Premium na Ilha das Mulheres\",\n" +
                "                \"dsServico\": \"Venha desfrutar de uma experiência maravilhosa navegando a bordo de nossos catamarãs de Cancun a Isla Mujeres!\\u003cbr /\\u003e\\n\\u003cbr /\\u003e\\nDurante o nosso passeio de 7 horas, desfrute do mar azul caribenha, do serviço de bar aberto, da beleza dos recifes de coral e a observação de estátuas Subaquática, uma experiência única e inesquecível. Depois, chegue à famosa ilha das mulheres e dirija-se a um clube de praia privado para desfrutar de um delicioso bufê composto por pratos regionais típicos e um serviçod e bar aberto incluído. Após o almoço, desfrute de um momento de relax nas nossas espreguiçadeiras do clube de praia, e mais tarde, navegue novamente até ao centro da Ilha das Mulheres para caminhar e explorar esta ilha fantástica, repleta de história e cultura.\\u003cbr /\\u003e\\n\\u003cbr /\\u003e\\nNo final da visita à ilha navegaremos até Cancun com muita animação e uma última atividade por realizar (se o tempo permitir) a famosa atividade de \\\"voo do spinnaker\\\", uma atividade extrema mas sem dúvia muito divertida.\\u003cbr /\\u003e\\n \\u003cbr\\u003e\\u003cbr\\u003e\\u003cstrong\\u003eCaracterísticas\\u003c/strong\\u003e\\u003cbr\\u003e\\nIncluso: Livro guia\\u003cbr\\u003eIncluso: Bar aberto\\u003cbr\\u003eIncluso: Passeio de Barco\\u003cbr\\u003e\\u003cbr\\u003e\\u003cstrong\\u003eSegmentos\\u003c/strong\\u003e\\n\\u003cbr\\u003eDuração:   Dia completo\\u003cbr\\u003eCategorias:   Cruzeiros e Atividades Aquáticas Atividades ao ar livre e Aventura Excursões\\u003cbr\\u003eServiços:   Safe2Stay\",\n" +
                "                \"reservaNomeList\": [\n" +
                "                    {\n" +
                "                        \"documento\": {\n" +
                "                            \"stObrigatorio\": false\n" +
                "                        },\n" +
                "                        \"paxTipo\": \"ADT\",\n" +
                "                        \"qtIdade\": 30,\n" +
                "                        \"stPrincipal\": false,\n" +
                "                        \"stSelecionado\": false,\n" +
                "                        \"stAplicado\": false\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"documento\": {\n" +
                "                            \"stObrigatorio\": false\n" +
                "                        },\n" +
                "                        \"paxTipo\": \"ADT\",\n" +
                "                        \"qtIdade\": 30,\n" +
                "                        \"stPrincipal\": false,\n" +
                "                        \"stSelecionado\": false,\n" +
                "                        \"stAplicado\": false\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"mediaList\": [\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://media.activitiesbank.com/50351/ENG/B/Joluga-2.jpg\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://media.activitiesbank.com/50351/ENG/B/Joluga-5.jpg\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://media.activitiesbank.com/50351/ENG/B/Joluga-3.jpg\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://media.activitiesbank.com/50351/ENG/B/Joluga-4_1%20%281%29.jpg\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stExigeNome\": false,\n" +
                "                \"stUnico\": false,\n" +
                "                \"stDisponivel\": true,\n" +
                "                \"dsParametro\": \"MCO|#|2022-02-20|#|2022-02-25\",\n" +
                "                \"stSelecionado\": false,\n" +
                "                \"stOnRequest\": false,\n" +
                "                \"stTransfer\": false\n" +
                "            },\n" +
                "            \"ingressoModalidadeList\": [\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"1377785903#GENERAL\",\n" +
                "                    \"nmModalidade\": \"Tour português\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"EUR\",\n" +
                "                        \"vlNeto\": 90.48,\n" +
                "                        \"vlPessoaNeto\": 45.24,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 999,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 45.24,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Adulto\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"CHD\",\n" +
                "                                \"qtIdade\": 4,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 0.0,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Criança de 0 a 4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"2096512424#GENERAL\",\n" +
                "                    \"nmModalidade\": \"Tour inglês\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"EUR\",\n" +
                "                        \"vlNeto\": 90.48,\n" +
                "                        \"vlPessoaNeto\": 45.24,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 999,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 45.24,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Adulto\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"CHD\",\n" +
                "                                \"qtIdade\": 4,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 0.0,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Criança de 0 a 4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"291654632#GENERAL\",\n" +
                "                    \"nmModalidade\": \"Tour espanhol\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"EUR\",\n" +
                "                        \"vlNeto\": 90.48,\n" +
                "                        \"vlPessoaNeto\": 45.24,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 999,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 45.24,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Adulto\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"CHD\",\n" +
                "                                \"qtIdade\": 4,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 0.0,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Criança de 0 a 4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false\n" +
                "                    }\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stSelecionado\": false\n" +
                "        },\n" +
                "        {\n" +
                "            \"sqPesquisa\": 2,\n" +
                "            \"servicoTipo\": \"INGRESSO\",\n" +
                "            \"ingresso\": {\n" +
                "                \"servicoTipo\": \"INGRESSO\",\n" +
                "                \"cdServico\": \"E-MX1-CHICHENEC\",\n" +
                "                \"nmServico\": \"Chichen Itzac cenote Hubiku, Almoço e Valladolid\",\n" +
                "                \"dsServico\": \"O nome significa \\u0027\\u0027Na boca do poço de Itza\\u0027\\u0027 e é o sítio arqueológico mais visitado do México, uma magnífica exibição da cultura Maia. O destaque do sítio é a \\u003cstrong\\u003e pirâmide de Kukulkan\\u003c/strong\\u003e, conhecida como El Castillo. Nesta excursão vai poder passear pela pirâmide, observando-a de todos os ângulos e admirando o trabalho que foi feito para construir esta magnífica obra.\\u003cbr /\\u003e\\n\\u003cbr /\\u003e\\nA construção sobrevive desde o tempo dos Maias e é um exemplo fascinante desta cultura. Culturas mesoamericanas ocasionalmente construíram pirâmides maiores em cima de menores, e este é o caso de El Castillo, em que uma escavação descobriu um templo com um trono de jaguar elaborado. Ao nascer e ao pôr do sol, nos equinócios de primavera e outono, o canto da pirâmide cria uma sombra de Kukulkan, um deus serpente com penas, criando a ilusão de uma cobra deslizando para o lado Norte com o movimento do sol.\\u003cbr /\\u003e\\n\\u003cbr /\\u003e\\nTambém existem belezas naturais para ver em Chichen Itza, como o \\u003cstrong\\u003e Cenote Sagrado\\u003c/strong\\u003e, uma impressionante passagem para uma cratera num corpo d\\u0027água subterrâneo que possui 60 metros de diâmetro. O Chichen Itza é \\u003cstrong\\u003e um dos locais mais impressionantes do México\\u003c/strong\\u003e, e como uma das Sete Maravilhas do Mundo, é uma experiência que com certeza não será esquecida.\\u003cbr /\\u003e\\n \\u003cbr\\u003e\\u003cbr\\u003e\\u003cstrong\\u003eCaracterísticas\\u003c/strong\\u003e\\u003cbr\\u003e\\nIncluso: Livro guia\\u003cbr\\u003eIncluso: Serviço de pickup e drop-off disponível apenas para hotéis selecionados.\\u003cbr\\u003eIncluso: Almoço\\u003cbr\\u003e\\u003cbr\\u003e\\u003cstrong\\u003eSegmentos\\u003c/strong\\u003e\\n\\u003cbr\\u003eServiços:   Safe2Stay\\u003cbr\\u003eCategorias:   Arte e Cultura\\u003cbr\\u003eDuração:   Dia completo\",\n" +
                "                \"reservaNomeList\": [\n" +
                "                    {\n" +
                "                        \"documento\": {\n" +
                "                            \"stObrigatorio\": false\n" +
                "                        },\n" +
                "                        \"paxTipo\": \"ADT\",\n" +
                "                        \"qtIdade\": 30,\n" +
                "                        \"stPrincipal\": false,\n" +
                "                        \"stSelecionado\": false,\n" +
                "                        \"stAplicado\": false\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"documento\": {\n" +
                "                            \"stObrigatorio\": false\n" +
                "                        },\n" +
                "                        \"paxTipo\": \"ADT\",\n" +
                "                        \"qtIdade\": 30,\n" +
                "                        \"stPrincipal\": false,\n" +
                "                        \"stSelecionado\": false,\n" +
                "                        \"stAplicado\": false\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"mediaList\": [\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://media.activitiesbank.com/12869/ENG/B/12869_1.jpg\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://media.activitiesbank.com/12869/ENG/B/12869_3.jpg\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://media.activitiesbank.com/12869/ENG/B/12869_2.jpg\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://media.activitiesbank.com/12869/ENG/B/12869_4.jpg\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://media.activitiesbank.com/12869/ENG/B/12869_5.jpg\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stExigeNome\": false,\n" +
                "                \"stUnico\": false,\n" +
                "                \"stDisponivel\": true,\n" +
                "                \"dsParametro\": \"MCO|#|2022-02-20|#|2022-02-25\",\n" +
                "                \"stSelecionado\": false,\n" +
                "                \"stOnRequest\": false,\n" +
                "                \"stTransfer\": false\n" +
                "            },\n" +
                "            \"ingressoModalidadeList\": [\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"291128424#NOTICKOFF\",\n" +
                "                    \"nmModalidade\": \"Tour inglês\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"EUR\",\n" +
                "                        \"vlNeto\": 77.94,\n" +
                "                        \"vlPessoaNeto\": 38.97,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 999,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 38.97,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Adulto\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"CHD\",\n" +
                "                                \"qtIdade\": 4,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 0.0,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Criança de 0 a 4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"1715938806#NOTICKOFF\",\n" +
                "                    \"nmModalidade\": \"Tour português\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"EUR\",\n" +
                "                        \"vlNeto\": 106.78,\n" +
                "                        \"vlPessoaNeto\": 53.39,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 999,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 53.39,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Adulto\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"CHD\",\n" +
                "                                \"qtIdade\": 4,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 0.0,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Criança de 0 a 4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"1885063038#NOTICKOFF\",\n" +
                "                    \"nmModalidade\": \"Tour espanhol\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"EUR\",\n" +
                "                        \"vlNeto\": 77.94,\n" +
                "                        \"vlPessoaNeto\": 38.97,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 999,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 38.97,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Adulto\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"CHD\",\n" +
                "                                \"qtIdade\": 4,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"EUR\",\n" +
                "                                    \"vlNeto\": 0.0,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false,\n" +
                "                                \"dsTarifa\": \"Criança de 0 a 4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false\n" +
                "                    }\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stSelecionado\": false\n" +
                "        }\n" +
                "    ],\n" +
                "    \"integrador\": {\n" +
                "        \"integradorEnum\": \"HOTELBEDS\",\n" +
                "        \"id\": 4701,\n" +
                "        \"nmIntegrador\": \"HOTELBEDS - INGRESSO\",\n" +
                "        \"ambiente\": \"PRODUCAO\",\n" +
                "        \"timeoutSegundos\": 4,\n" +
                "        \"idParceiro\": \"5\",\n" +
                "        \"ativo\": true,\n" +
                "        \"qtMaximaSessao\": 2,\n" +
                "        \"dsUrl\": \"http://192.168.15.114:8090/hotelbedssrvc/servico\",\n" +
                "        \"stLocal\": false,\n" +
                "        \"dsCredencialList\": [\n" +
                "            \"921be471023ddba6a7886586bc1badfd\",\n" +
                "            \"ba6b3a40a2\",\n" +
                "            \"\",\n" +
                "            \"\",\n" +
                "            null\n" +
                "        ],\n" +
                "        \"stSelecionado\": false,\n" +
                "        \"idFornecedor\": 14258,\n" +
                "        \"idEmpresa\": 1,\n" +
                "        \"cdIntegra\": \"B0168CE82C1B0DE0C1F8B53497E23353\",\n" +
                "        \"sgEmpresa\": \"ORITEST\",\n" +
                "        \"nmUsuarioEmpresa\": \"Infotera\",\n" +
                "        \"sgNacionalidade\": \"BR\",\n" +
                "        \"stErro\": false,\n" +
                "        \"stGerarLog\": true,\n" +
                "        \"dsMetodo\": \"ActivitySearchRequest\",\n" +
                "        \"transStMarkupTaxa\": false,\n" +
                "        \"transStComissaoTaxa\": false,\n" +
                "        \"idiomaEnum\": \"PT_BR\"\n" +
                "    }\n" +
                "}";
    }


}

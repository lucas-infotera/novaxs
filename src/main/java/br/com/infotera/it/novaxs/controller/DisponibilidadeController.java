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
import br.com.infotera.common.util.LogWS;
import br.com.infotera.it.novaxs.services.DisponibilidadeWS;
import br.com.infotera.it.novaxs.utils.UtilsWS;
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
        wsRQ.getIntegrador().setDsMetodo("disponibilidade");

        try {
            result = disponibilidadeWS.disponibilidade(wsRQ);
        } catch (ErrorException ex) {
            result = new WSDisponibilidadeIngressoRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            result = new WSDisponibilidadeIngressoRS(null, new ErrorException(wsRQ.getIntegrador(), DisponibilidadeController.class, "disponibilidade", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(result.getIntegrador(), jsonRQ, result);
        }
        return gson.toJson(result);
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
                "                \"dsServico\": \"Venha desfrutar de uma experi??ncia maravilhosa navegando a bordo de nossos catamar??s de Cancun a Isla Mujeres!\\u003cbr /\\u003e\\n\\u003cbr /\\u003e\\nDurante o nosso passeio de 7 horas, desfrute do mar azul caribenha, do servi??o de bar aberto, da beleza dos recifes de coral e a observa????o de est??tuas Subaqu??tica, uma experi??ncia ??nica e inesquec??vel. Depois, chegue ?? famosa ilha das mulheres e dirija-se a um clube de praia privado para desfrutar de um delicioso buf?? composto por pratos regionais t??picos e um servi??od e bar aberto inclu??do. Ap??s o almo??o, desfrute de um momento de relax nas nossas espregui??adeiras do clube de praia, e mais tarde, navegue novamente at?? ao centro da Ilha das Mulheres para caminhar e explorar esta ilha fant??stica, repleta de hist??ria e cultura.\\u003cbr /\\u003e\\n\\u003cbr /\\u003e\\nNo final da visita ?? ilha navegaremos at?? Cancun com muita anima????o e uma ??ltima atividade por realizar (se o tempo permitir) a famosa atividade de \\\"voo do spinnaker\\\", uma atividade extrema mas sem d??via muito divertida.\\u003cbr /\\u003e\\n \\u003cbr\\u003e\\u003cbr\\u003e\\u003cstrong\\u003eCaracter??sticas\\u003c/strong\\u003e\\u003cbr\\u003e\\nIncluso: Livro guia\\u003cbr\\u003eIncluso: Bar aberto\\u003cbr\\u003eIncluso: Passeio de Barco\\u003cbr\\u003e\\u003cbr\\u003e\\u003cstrong\\u003eSegmentos\\u003c/strong\\u003e\\n\\u003cbr\\u003eDura????o:   Dia completo\\u003cbr\\u003eCategorias:   Cruzeiros e Atividades Aqu??ticas Atividades ao ar livre e Aventura Excurs??es\\u003cbr\\u003eServi??os:   Safe2Stay\",\n" +
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
                "                    \"nmModalidade\": \"Tour portugu??s\",\n" +
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
                "                                \"dsTarifa\": \"Crian??a de 0 a 4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"2096512424#GENERAL\",\n" +
                "                    \"nmModalidade\": \"Tour ingl??s\",\n" +
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
                "                                \"dsTarifa\": \"Crian??a de 0 a 4\"\n" +
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
                "                                \"dsTarifa\": \"Crian??a de 0 a 4\"\n" +
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
                "                \"nmServico\": \"Chichen Itzac cenote Hubiku, Almo??o e Valladolid\",\n" +
                "                \"dsServico\": \"O nome significa \\u0027\\u0027Na boca do po??o de Itza\\u0027\\u0027 e ?? o s??tio arqueol??gico mais visitado do M??xico, uma magn??fica exibi????o da cultura Maia. O destaque do s??tio ?? a \\u003cstrong\\u003e pir??mide de Kukulkan\\u003c/strong\\u003e, conhecida como El Castillo. Nesta excurs??o vai poder passear pela pir??mide, observando-a de todos os ??ngulos e admirando o trabalho que foi feito para construir esta magn??fica obra.\\u003cbr /\\u003e\\n\\u003cbr /\\u003e\\nA constru????o sobrevive desde o tempo dos Maias e ?? um exemplo fascinante desta cultura. Culturas mesoamericanas ocasionalmente constru??ram pir??mides maiores em cima de menores, e este ?? o caso de El Castillo, em que uma escava????o descobriu um templo com um trono de jaguar elaborado. Ao nascer e ao p??r do sol, nos equin??cios de primavera e outono, o canto da pir??mide cria uma sombra de Kukulkan, um deus serpente com penas, criando a ilus??o de uma cobra deslizando para o lado Norte com o movimento do sol.\\u003cbr /\\u003e\\n\\u003cbr /\\u003e\\nTamb??m existem belezas naturais para ver em Chichen Itza, como o \\u003cstrong\\u003e Cenote Sagrado\\u003c/strong\\u003e, uma impressionante passagem para uma cratera num corpo d\\u0027??gua subterr??neo que possui 60 metros de di??metro. O Chichen Itza ?? \\u003cstrong\\u003e um dos locais mais impressionantes do M??xico\\u003c/strong\\u003e, e como uma das Sete Maravilhas do Mundo, ?? uma experi??ncia que com certeza n??o ser?? esquecida.\\u003cbr /\\u003e\\n \\u003cbr\\u003e\\u003cbr\\u003e\\u003cstrong\\u003eCaracter??sticas\\u003c/strong\\u003e\\u003cbr\\u003e\\nIncluso: Livro guia\\u003cbr\\u003eIncluso: Servi??o de pickup e drop-off dispon??vel apenas para hot??is selecionados.\\u003cbr\\u003eIncluso: Almo??o\\u003cbr\\u003e\\u003cbr\\u003e\\u003cstrong\\u003eSegmentos\\u003c/strong\\u003e\\n\\u003cbr\\u003eServi??os:   Safe2Stay\\u003cbr\\u003eCategorias:   Arte e Cultura\\u003cbr\\u003eDura????o:   Dia completo\",\n" +
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
                "                    \"nmModalidade\": \"Tour ingl??s\",\n" +
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
                "                                \"dsTarifa\": \"Crian??a de 0 a 4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"1715938806#NOTICKOFF\",\n" +
                "                    \"nmModalidade\": \"Tour portugu??s\",\n" +
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
                "                                \"dsTarifa\": \"Crian??a de 0 a 4\"\n" +
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
                "                                \"dsTarifa\": \"Crian??a de 0 a 4\"\n" +
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

    public String teste3String() {
        return "{\n" +
                "    \"ingressoPesquisaList\": [\n" +
                "        {\n" +
                "            \"sqPesquisa\": 1,\n" +
                "            \"servicoTipo\": \"INGRESSO\",\n" +
                "            \"ingresso\": {\n" +
                "                \"servicoTipo\": \"INGRESSO\",\n" +
                "                \"cdServico\": \"products/47841\",\n" +
                "                \"nmServico\": \"Ingresso Individual com Formul??rio\",\n" +
                "                \"dsServico\": \"VariavelDeTeste\",\n" +
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
                "                        \"dsUrl\": \"https://www.ifrr.edu.br/midia/teste/image\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stExigeNome\": false,\n" +
                "                \"stUnico\": false,\n" +
                "                \"stDisponivel\": true,\n" +
                "                \"dsParametro\": \"{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"2147483647.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Produto\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"products/47841\\\",\\\"form\\\":[{\\\"name\\\":\\\"Nome do Cliente\\\",\\\"property\\\":\\\"client_name\\\",\\\"type\\\":\\\"text\\\",\\\"group\\\":\\\"\\\",\\\"order\\\":0,\\\"per_entry\\\":true,\\\"product\\\":\\\"Ingresso Individual com Formul??rio\\\",\\\"product_path\\\":\\\"products/47841\\\",\\\"required\\\":true},{\\\"name\\\":\\\"Data de Nascimento\\\",\\\"property\\\":\\\"client_birthday\\\",\\\"type\\\":\\\"date\\\",\\\"group\\\":\\\"\\\",\\\"order\\\":2,\\\"per_entry\\\":true,\\\"product\\\":\\\"Ingresso Individual com Formul??rio\\\",\\\"product_path\\\":\\\"products/47841\\\",\\\"required\\\":true},{\\\"name\\\":\\\"Documento do Cliente\\\",\\\"property\\\":\\\"client_document\\\",\\\"type\\\":\\\"text\\\",\\\"group\\\":\\\"\\\",\\\"order\\\":1,\\\"per_entry\\\":true,\\\"product\\\":\\\"Ingresso Individual com Formul??rio\\\",\\\"product_path\\\":\\\"products/47841\\\",\\\"required\\\":true},{\\\"name\\\":\\\"Tipo\\\",\\\"property\\\":\\\"type\\\",\\\"type\\\":\\\"list\\\",\\\"group\\\":\\\"\\\",\\\"order\\\":3,\\\"per_entry\\\":false,\\\"product\\\":\\\"Ingresso Individual com Formul??rio\\\",\\\"product_path\\\":\\\"products/47841\\\",\\\"required\\\":false,\\\"values\\\":[{\\\"show\\\":\\\"Grupo\\\",\\\"value\\\":\\\"Grupo\\\"},{\\\"show\\\":\\\"Individual\\\",\\\"value\\\":\\\"Individual\\\"}]}],\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"custo\\\":13950,\\\"schedules\\\":[],\\\"name\\\":\\\"Ingresso Individual com Formul??rio\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"47841\\\",\\\"shortName\\\":\\\"Ingresso Individual com Formul??rio\\\",\\\"value\\\":\\\"13950\\\",\\\"token\\\":\\\"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqf6aBhunFggi98sSfQv7z9F3klTBpaG/70S1JyaMofo6aP0IUNsAciENIywFIue7vUtmji1sAj+EBLSgXLYQiLu0L66OC/ysRvHlt6nCYEkeb\\\",\\\"faixaetaria\\\":\\\"ADT\\\",\\\"travelertype\\\":\\\"ADT\\\"}\",\n" +
                "                \"stSelecionado\": false,\n" +
                "                \"stOnRequest\": false,\n" +
                "                \"stTransfer\": false\n" +
                "            },\n" +
                "            \"ingressoModalidadeList\": [\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"47841\",\n" +
                "                    \"nmModalidade\": \"Ingresso Individual com Formul??rio\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 139.5,\n" +
                "                        \"vlPessoaNeto\": 69.75,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 69.75,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"vlTotal\": 139.5\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stSelecionado\": false\n" +
                "        },\n" +
                "        {   \n" +
                "            \"sqPesquisa\": 2,\n" +
                "            \"servicoTipo\": \"INGRESSO\",\n" +
                "            \"ingresso\": {\n" +
                "                \"servicoTipo\": \"INGRESSO\",\n" +
                "                \"cdServico\": \"products/46700\",\n" +
                "                \"nmServico\": \"Ingresso Individual\",\n" +
                "                \"dsServico\": \"VariavelDeTeste\",\n" +
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
                "                        \"dsUrl\": \"https://www.ifrr.edu.br/midia/teste/image\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stExigeNome\": false,\n" +
                "                \"stUnico\": false,\n" +
                "                \"stDisponivel\": true,\n" +
                "                \"dsParametro\": \"{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"100.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Produto\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"products/46700\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"custo\\\":9000,\\\"schedules\\\":[],\\\"name\\\":\\\"Ingresso Individual\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"46700\\\",\\\"shortName\\\":\\\"Ingresso Individual\\\",\\\"value\\\":\\\"9000\\\",\\\"token\\\":\\\"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqf3gbEMdyBrsdiNnP0tarQyzklTBpaG/70S1JyaMofo6aP0IUNsAciENIywFIue7vUtmji1sAj+EBLSgXLYQiLu2k9yvv8v6MlMvgb2OTz2+5\\\",\\\"faixaetaria\\\":\\\"ADT\\\",\\\"travelertype\\\":\\\"ADT\\\"}\",\n" +
                "                \"stSelecionado\": false,\n" +
                "                \"stOnRequest\": false,\n" +
                "                \"stTransfer\": false\n" +
                "            },\n" +
                "            \"ingressoModalidadeList\": [\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"46700\",\n" +
                "                    \"nmModalidade\": \"Ingresso Individual\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 90.0,\n" +
                "                        \"vlPessoaNeto\": 45.0,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 45.0,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"vlTotal\": 90.0\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stSelecionado\": false\n" +
                "        },\n" +
                "        {\n" +
                "            \"sqPesquisa\": 3,\n" +
                "            \"servicoTipo\": \"INGRESSO\",\n" +
                "            \"ingresso\": {\n" +
                "                \"servicoTipo\": \"INGRESSO\",\n" +
                "                \"cdServico\": \"products/46701\",\n" +
                "                \"nmServico\": \"Ingresso com Hor??rio\",\n" +
                "                \"dsServico\": \"VariavelDeTeste\",\n" +
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
                "                        \"dsUrl\": \"https://www.ifrr.edu.br/midia/teste/image\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stExigeNome\": false,\n" +
                "                \"stUnico\": false,\n" +
                "                \"stDisponivel\": true,\n" +
                "                \"dsParametro\": \"{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"2147483647.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Produto\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"products/46701\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"custo\\\":8091,\\\"schedules\\\":[{\\\"path\\\":\\\"products/46701\\\",\\\"schedule\\\":\\\"11:00\\\",\\\"available\\\":\\\"2147483647.00\\\"},{\\\"path\\\":\\\"products/46701\\\",\\\"schedule\\\":\\\"13:00\\\",\\\"available\\\":\\\"2147483647.00\\\"},{\\\"path\\\":\\\"products/46701\\\",\\\"schedule\\\":\\\"15:00\\\",\\\"available\\\":\\\"2147483647.00\\\"}],\\\"name\\\":\\\"Ingresso com Hor??rio\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"46701\\\",\\\"shortName\\\":\\\"Ingresso com Hor??rio\\\",\\\"value\\\":\\\"8091\\\",\\\"token\\\":\\\"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqf2IGAviRhbdHsuVdt10w8i7klTBpaG/70S1JyaMofo6aP0IUNsAciENIywFIue7vUtmji1sAj+EBLSgXLYQiLu2NVAd1fIfkdbumOnoPCkOm\\\",\\\"faixaetaria\\\":\\\"ADT\\\",\\\"schedule_type\\\":\\\"code\\\",\\\"needExtraData\\\":\\\"hora\\\",\\\"travelertype\\\":\\\"ADT\\\"}\",\n" +
                "                \"stSelecionado\": false,\n" +
                "                \"stOnRequest\": false,\n" +
                "                \"stTransfer\": false\n" +
                "            },\n" +
                "            \"ingressoModalidadeList\": [\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"products/46701\",\n" +
                "                    \"nmModalidade\": \"11:00\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 80.91,\n" +
                "                        \"vlPessoaNeto\": 40.455,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 40.455,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"vlTotal\": 80.91\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"dsModalidade\": \"Ingresso com agendamento11:00\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"products/46701\",\n" +
                "                    \"nmModalidade\": \"13:00\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 80.91,\n" +
                "                        \"vlPessoaNeto\": 40.455,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 40.455,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"vlTotal\": 80.91\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"dsModalidade\": \"Ingresso com agendamento13:00\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"products/46701\",\n" +
                "                    \"nmModalidade\": \"15:00\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 80.91,\n" +
                "                        \"vlPessoaNeto\": 40.455,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 40.455,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"vlTotal\": 80.91\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"dsModalidade\": \"Ingresso com agendamento15:00\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stSelecionado\": false\n" +
                "        },\n" +
                "        {\n" +
                "            \"sqPesquisa\": 4,\n" +
                "            \"servicoTipo\": \"INGRESSO\",\n" +
                "            \"ingresso\": {\n" +
                "                \"servicoTipo\": \"INGRESSO\",\n" +
                "                \"cdServico\": \"products/47245\",\n" +
                "                \"nmServico\": \"Ingresso CHD Individual\",\n" +
                "                \"dsServico\": \"VariavelDeTeste\",\n" +
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
                "                        \"dsUrl\": \"https://www.ifrr.edu.br/midia/teste/image\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stExigeNome\": false,\n" +
                "                \"stUnico\": false,\n" +
                "                \"stDisponivel\": true,\n" +
                "                \"dsParametro\": \"{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"2147483647.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Produto\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"products/47245\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"custo\\\":5400,\\\"schedules\\\":[],\\\"name\\\":\\\"Ingresso CHD Individual\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"47245\\\",\\\"shortName\\\":\\\"Ingresso CHD Individual\\\",\\\"value\\\":\\\"5400\\\",\\\"token\\\":\\\"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqf/ddvjpNsatLjsGep8IV2TnklTBpaG/70S1JyaMofo6aP0IUNsAciENIywFIue7vUtmji1sAj+EBLSgXLYQiLu0ep/Z184YETscKZ5VOH0Hk\\\",\\\"faixaetaria\\\":\\\"CHD\\\",\\\"travelertype\\\":\\\"CHD\\\"}\",\n" +
                "                \"stSelecionado\": false,\n" +
                "                \"stOnRequest\": false,\n" +
                "                \"stTransfer\": false\n" +
                "            },\n" +
                "            \"ingressoModalidadeList\": [\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"47245\",\n" +
                "                    \"nmModalidade\": \"Ingresso CHD Individual\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 54.0,\n" +
                "                        \"vlPessoaNeto\": 27.0,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 27.0,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"vlTotal\": 54.0\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stSelecionado\": false\n" +
                "        },\n" +
                "        {\n" +
                "            \"sqPesquisa\": 5,\n" +
                "            \"servicoTipo\": \"INGRESSO\",\n" +
                "            \"ingresso\": {\n" +
                "                \"servicoTipo\": \"INGRESSO\",\n" +
                "                \"cdServico\": \"productCombos/2111\",\n" +
                "                \"nmServico\": \"Combo 3 Ingressos\",\n" +
                "                \"dsServico\": \"VariavelDeTeste\",\n" +
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
                "                        \"dsUrl\": \"https://www.ifrr.edu.br/midia/teste/image\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://www.ifrr.edu.br/midia/teste/image\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://www.ifrr.edu.br/midia/teste/image\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stExigeNome\": false,\n" +
                "                \"stUnico\": false,\n" +
                "                \"stDisponivel\": true,\n" +
                "                \"dsParametro\": \"{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"100.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Combo\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"productCombos/2111\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"custo\\\":24300,\\\"schedules\\\":[],\\\"name\\\":\\\"Combo 3 Ingressos\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"2111\\\",\\\"value\\\":\\\"24300\\\",\\\"token\\\":\\\"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqf8l4J1O+SNNXPGLQcjtmJmlf1AZzq+UBrTQQxOdwL7lNE9Qa3CXWywEbL837/gyW1EZ2xLqPMowEunmbLxXFZa10GkEIZqhfXG+vUDyRBFi2LF9WGlQE3k3+gurZxqvNAw\\u003d\\u003d\\\",\\\"faixaetaria\\\":\\\"UNICO\\\",\\\"products\\\":[{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"select_date\\\":false,\\\"amount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"100.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Produto\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"products/46700\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"schedules\\\":[],\\\"faixaetaria\\\":\\\"ADT\\\",\\\"name\\\":\\\"Ingresso Individual\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"46700\\\",\\\"shortName\\\":\\\"Ingresso Individual\\\",\\\"travelertype\\\":\\\"ADT\\\"},{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"select_date\\\":false,\\\"amount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"100.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Produto\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"products/46700\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"schedules\\\":[],\\\"faixaetaria\\\":\\\"ADT\\\",\\\"name\\\":\\\"Ingresso Individual\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"46700\\\",\\\"shortName\\\":\\\"Ingresso Individual\\\",\\\"travelertype\\\":\\\"ADT\\\"},{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"select_date\\\":false,\\\"amount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"100.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Produto\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"products/46700\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"schedules\\\":[],\\\"faixaetaria\\\":\\\"ADT\\\",\\\"name\\\":\\\"Ingresso Individual\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"46700\\\",\\\"shortName\\\":\\\"Ingresso Individual\\\",\\\"travelertype\\\":\\\"ADT\\\"}],\\\"travelertype\\\":\\\"UNICO\\\"}\",\n" +
                "                \"stSelecionado\": false,\n" +
                "                \"stOnRequest\": false,\n" +
                "                \"stTransfer\": false\n" +
                "            },\n" +
                "            \"ingressoModalidadeList\": [\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"46700\",\n" +
                "                    \"nmModalidade\": \"Ingresso Individual 1\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 243.0,\n" +
                "                        \"vlPessoaNeto\": 121.5,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 121.5,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"vlTotal\": 243.0\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"46700\",\n" +
                "                    \"nmModalidade\": \"Ingresso Individual 2\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 243.0,\n" +
                "                        \"vlPessoaNeto\": 121.5,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 121.5,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"vlTotal\": 243.0\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"46700\",\n" +
                "                    \"nmModalidade\": \"Ingresso Individual 3\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 243.0,\n" +
                "                        \"vlPessoaNeto\": 121.5,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 121.5,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"vlTotal\": 243.0\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stSelecionado\": false\n" +
                "        },\n" +
                "        {\n" +
                "            \"sqPesquisa\": 6,\n" +
                "            \"servicoTipo\": \"INGRESSO\",\n" +
                "            \"ingresso\": {\n" +
                "                \"servicoTipo\": \"INGRESSO\",\n" +
                "                \"cdServico\": \"productCombos/2112\",\n" +
                "                \"nmServico\": \"Combo 3 Dias de Acesso\",\n" +
                "                \"dsServico\": \"VariavelDeTeste\",\n" +
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
                "                        \"dsUrl\": \"https://www.ifrr.edu.br/midia/teste/image\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://www.ifrr.edu.br/midia/teste/image\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"mediaCategoria\": \"SERVICO\",\n" +
                "                        \"dsUrl\": \"https://www.ifrr.edu.br/midia/teste/image\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"stExigeNome\": false,\n" +
                "                \"stUnico\": false,\n" +
                "                \"stDisponivel\": true,\n" +
                "                \"dsParametro\": \"{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"100.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Combo\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"productCombos/2112\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"custo\\\":14647,\\\"schedules\\\":[],\\\"name\\\":\\\"Combo 3 Dias de Acesso\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"2112\\\",\\\"value\\\":\\\"14648\\\",\\\"token\\\":\\\"NNefS+VpzJ6dPVxVVMCkE8gtkHizQj1qzvHs1DEGpO9o3E/FllCldts+MNHipysWIB8pUl50/Bs6NJZvs5Nqfydx5TXW9G42IIYCQL8Qeupf1AZzq+UBrTQQxOdwL7lNE9Qa3CXWywEbL837/gyW1EZ2xLqPMowEunmbLxXFZa0NV/jD2K//44iusssfvmgnYVWbawXsMNh+yrVpYUukvw\\u003d\\u003d\\\",\\\"faixaetaria\\\":\\\"UNICO\\\",\\\"products\\\":[{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"select_date\\\":true,\\\"amount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"100.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Produto\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"products/46700\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"schedules\\\":[],\\\"faixaetaria\\\":\\\"ADT\\\",\\\"name\\\":\\\"Ingresso Individual\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"46700\\\",\\\"shortName\\\":\\\"Ingresso Individual\\\",\\\"dates\\\":[\\\"02/07/2022\\\",\\\"03/07/2022\\\",\\\"04/07/2022\\\",\\\"05/07/2022\\\",\\\"06/07/2022\\\",\\\"07/07/2022\\\",\\\"08/07/2022\\\",\\\"09/07/2022\\\",\\\"10/07/2022\\\",\\\"11/07/2022\\\",\\\"12/07/2022\\\",\\\"13/07/2022\\\",\\\"14/07/2022\\\",\\\"15/07/2022\\\",\\\"16/07/2022\\\"],\\\"travelertype\\\":\\\"ADT\\\"},{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"select_date\\\":true,\\\"amount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"100.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Produto\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"products/46700\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"schedules\\\":[],\\\"faixaetaria\\\":\\\"ADT\\\",\\\"name\\\":\\\"Ingresso Individual\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"46700\\\",\\\"shortName\\\":\\\"Ingresso Individual\\\",\\\"dates\\\":[\\\"02/07/2022\\\",\\\"03/07/2022\\\",\\\"04/07/2022\\\",\\\"05/07/2022\\\",\\\"06/07/2022\\\",\\\"07/07/2022\\\",\\\"08/07/2022\\\",\\\"09/07/2022\\\",\\\"10/07/2022\\\",\\\"11/07/2022\\\",\\\"12/07/2022\\\",\\\"13/07/2022\\\",\\\"14/07/2022\\\",\\\"15/07/2022\\\",\\\"16/07/2022\\\"],\\\"travelertype\\\":\\\"ADT\\\"},{\\\"supplierName\\\":\\\"NXS - Operadora Demo\\\",\\\"image\\\":\\\"https://www.ifrr.edu.br/midia/teste/image\\\",\\\"minAmount\\\":\\\"1\\\",\\\"select_date\\\":true,\\\"amount\\\":\\\"1\\\",\\\"htmlMemo\\\":\\\"\\\",\\\"available\\\":\\\"100.00\\\",\\\"memo\\\":\\\"\\\",\\\"supplierPath\\\":\\\"agencies/954\\\",\\\"cancellationPolicies\\\":\\\"Cancelamento gratuito at?? 24 horas antes da data de uso.\\\\nCancelamento no dia do agendamento multa de 50%.\\\\nAp??s a data de agendamento multa de 100%.\\\",\\\"type\\\":\\\"Produto\\\",\\\"additionalLanguagesInfo\\\":[],\\\"path\\\":\\\"products/46700\\\",\\\"definePrecoNaVenda\\\":\\\"false\\\",\\\"schedules\\\":[],\\\"faixaetaria\\\":\\\"ADT\\\",\\\"name\\\":\\\"Ingresso Individual\\\",\\\"currency\\\":\\\"BRL\\\",\\\"id\\\":\\\"46700\\\",\\\"shortName\\\":\\\"Ingresso Individual\\\",\\\"dates\\\":[\\\"02/07/2022\\\",\\\"03/07/2022\\\",\\\"04/07/2022\\\",\\\"05/07/2022\\\",\\\"06/07/2022\\\",\\\"07/07/2022\\\",\\\"08/07/2022\\\",\\\"09/07/2022\\\",\\\"10/07/2022\\\",\\\"11/07/2022\\\",\\\"12/07/2022\\\",\\\"13/07/2022\\\",\\\"14/07/2022\\\",\\\"15/07/2022\\\",\\\"16/07/2022\\\"],\\\"travelertype\\\":\\\"ADT\\\"}],\\\"umaPessoaPorCombo\\\":true,\\\"travelertype\\\":\\\"UNICO\\\"}\",\n" +
                "                \"stSelecionado\": false,\n" +
                "                \"stOnRequest\": false,\n" +
                "                \"stTransfer\": false\n" +
                "            },\n" +
                "            \"ingressoModalidadeList\": [\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"46700\",\n" +
                "                    \"nmModalidade\": \"Ingresso Individual 1\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 146.48,\n" +
                "                        \"vlPessoaNeto\": 73.24,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 73.24,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-03T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-03T00:00:00Z\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"46700\",\n" +
                "                    \"nmModalidade\": \"Ingresso Individual 2\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 146.48,\n" +
                "                        \"vlPessoaNeto\": 73.24,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 73.24,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-03T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-03T00:00:00Z\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"cdModalidade\": \"46700\",\n" +
                "                    \"nmModalidade\": \"Ingresso Individual 3\",\n" +
                "                    \"tarifa\": {\n" +
                "                        \"sgMoedaNeto\": \"BRL\",\n" +
                "                        \"vlNeto\": 146.48,\n" +
                "                        \"vlPessoaNeto\": 73.24,\n" +
                "                        \"vlComissao\": 0.0,\n" +
                "                        \"pagtoFornecedorTipo\": \"FATURADO\",\n" +
                "                        \"tarifaNomeList\": [\n" +
                "                            {\n" +
                "                                \"paxTipo\": \"ADT\",\n" +
                "                                \"qtIdade\": 30,\n" +
                "                                \"tarifa\": {\n" +
                "                                    \"sgMoedaNeto\": \"BRL\",\n" +
                "                                    \"vlNeto\": 73.24,\n" +
                "                                    \"vlComissao\": 0.0,\n" +
                "                                    \"stTarifaPacote\": false,\n" +
                "                                    \"transStMarkupTaxa\": false,\n" +
                "                                    \"transStComissaoTaxa\": false\n" +
                "                                },\n" +
                "                                \"stAplicado\": false\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stTarifaPacote\": false,\n" +
                "                        \"transStMarkupTaxa\": false,\n" +
                "                        \"transStComissaoTaxa\": false\n" +
                "                    },\n" +
                "                    \"utilizacaoDatasList\": [\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-02T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-02T00:00:00Z\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"dtInicio\": \"2022-07-03T00:00:00Z\",\n" +
                "                            \"dtFim\": \"2022-07-03T00:00:00Z\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stSelecionado\": false\n" +
                "        }\n" +
                "    ],\n" +
                "    \"integrador\": {\n" +
                "        \"integradorEnum\": \"NOVAXS\",\n" +
                "        \"id\": 6231,\n" +
                "        \"nmIntegrador\": \"Beach Park\",\n" +
                "        \"ambiente\": \"PRODUCAO\",\n" +
                "        \"timeoutSegundos\": 4,\n" +
                "        \"idParceiro\": \"113\",\n" +
                "        \"ativo\": true,\n" +
                "        \"qtMaximaSessao\": 2,\n" +
                "        \"dsUrl\": \"http://192.168.15.114:8070/novaxs\",\n" +
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
                "        \"cdIntegra\": \"B0168CE82C1B0DE0C1F8B53497E23353\",\n" +
                "        \"sgEmpresa\": \"ORITEST\",\n" +
                "        \"nmUsuarioEmpresa\": \"Infotera\",\n" +
                "        \"sgNacionalidade\": \"BR\",\n" +
                "        \"stErro\": false,\n" +
                "        \"stGerarLog\": true,\n" +
                "        \"stLogAPI\": true,\n" +
                "        \"stGerarLogErro\": false,\n" +
                "        \"dsMetodo\": \"disponibilidadeIngresso\",\n" +
                "        \"dsAction\": \"getProductsByDateRQ\",\n" +
                "        \"transStMarkupTaxa\": false,\n" +
                "        \"transStComissaoTaxa\": false,\n" +
                "        \"idiomaEnum\": \"PT_BR\"\n" +
                "    }\n" +
                "}";
    }


}

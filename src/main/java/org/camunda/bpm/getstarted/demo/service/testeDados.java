package org.camunda.bpm.getstarted.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.camunda.bpm.getstarted.demo.util.TestExecutionListener;
import org.json.JSONObject;

@Service
public class testeDados {

    @Autowired
    private TestExecutionListener testExecutionListener;

    private static void getEmployees() {
        final String uri = "http://localhost:8080/springrestexample/employees.xml";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        System.out.println(result);
    }

    /* Dados de uma entidade identificada em uma busca universal no banco, pelo uuid*/
    @Bean
    @Scope(value = "prototype") // means that Spring will not instantiate the bean right on start, but will do it later on demand.
    public static BeanMap registro(String uuid) {
        JSONObject jsonObject = new JSONObject(
                "{\"nome\":\"Perbônia Mariscleusa\",\"quantidade\":123,\"data\":\"2022-05-25T14:08:04.029994-03:00\",\"objeto\":{\"cor\":\"vermelho\",\"qtd\":7.2,\"lista\":[1,2,3],\"nulo\":null,\"bool\":false,\"bolacha\":true,\"arrObjs\":[{\"qtd\":2.321},{\"qtd\":5.789},{\"qtd\":19.009}]}}");
        return org.camunda.bpm.getstarted.demo.util.ClassGen.generateBean(jsonObject);
    }

    /*
     * Dados de uma entidade identificada em uma busca universal no banco, pelo uuid
     */
    @Bean
    @Scope(value = "prototype")
    public String jaca() {
        return testExecutionListener.pid;
    }

    /* Dados de uma lista json obtida via REST em método GET
        Possui getters especiais:
        -- Se registro --
        • .algo() - [tipo] Faz alguma coisa

        -- Se Array --
        • .size() - [Int] Tamanho do Array recebido
        • .first() - [Object] Retorna apenas o Primeiro Objeto do Array recebido
        • .last() - [Object] Retorna apenas o Último Objeto do Array recebido
        • .firstMatch(String field, String val) - [Int] Tamanho do Array recebido
        • .firstMatchAny(String val) - [Int] Tamanho do Array recebido
    */
    @Bean
    @Scope(value = "prototype") // means that Spring will not instantiate the bean right on start, but will do it later on demand.
    public static BeanMap rest(String url) {
        JSONObject jsonObject = new JSONObject(
                "{\"nome\":\"Perbônia Mariscleusa\",\"quantidade\":123,\"data\":\"2022-05-25T14:08:04.029994-03:00\",\"objeto\":{\"cor\":\"vermelho\",\"qtd\":7.2,\"lista\":[1,2,3],\"nulo\":null,\"bool\":false,\"bolacha\":true,\"arrObjs\":[{\"qtd\":2.321},{\"qtd\":5.789},{\"qtd\":19.009}]}}");
        return org.camunda.bpm.getstarted.demo.util.ClassGen.generateBean(jsonObject);
    }

    /* TODO: Dados de uma entidade identificada passivamente pelo contexto BPMN em que é executada */
    @Bean
    @Scope(value = "prototype")
    public static BeanMap processo() {
        JSONObject jsonObject = new JSONObject("{\"nome\":\"Perbônia Mariscleusa\",\"quantidade\":123,\"data\":\"2022-05-25T14:08:04.029994-03:00\",\"objeto\":{\"cor\":\"vermelho\",\"qtd\":7.2,\"lista\":[1,2,3],\"nulo\":null,\"bool\":false,\"bolacha\":true,\"arrObjs\":[{\"qtd\":2.321},{\"qtd\":5.789},{\"qtd\":19.009}]}}");
        return org.camunda.bpm.getstarted.demo.util.ClassGen.generateBean(jsonObject);
    }
/* 
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBk() {
        return bk;
    }

    public void setBk(String bk) {
        this.bk = bk;
    } */

}
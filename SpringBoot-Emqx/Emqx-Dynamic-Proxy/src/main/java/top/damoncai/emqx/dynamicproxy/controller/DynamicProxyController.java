package top.damoncai.emqx.dynamicproxy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import top.damoncai.emqx.dynamicproxy.code.QosEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author zhishun.cai
 * @since 2022/2/22 19:18
 */
@Slf4j
@RestController
@RequestMapping("mqtt")
public class DynamicProxyController {
    private Map clientStatusMap = new HashMap();

    @PostMapping("webhook")
    public void webhook(@RequestBody Map params) {
        log.info("data: " + params);
        String action = (String) params.get("action");
        String clientId = (String) params.get("clientid");
        if(action.equals("client_connected")){
            //客户端成功接入
            clientStatusMap.put(clientId,true);
            //自动订阅autosub主题
            autoSub(clientId,"autosub/#",QosEnum.QoS2,true);
        }
        if(action.equals("client_disconnected")){
            //客户端断开连接
            clientStatusMap.put(clientId,false);
            //自动取消订阅autosub主题
            autoSub(clientId,"autosub/#",QosEnum.QoS2,false);
        }
    }

    /**
     * 自动订阅或取消订阅
     * @param clientId
     * @param topicfilter
     * @param qos
     * @param sub
     */
    private void autoSub(String clientId, String topicfilter, QosEnum qos, boolean sub) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("admin", "public")
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .build();
        //装配参数
        Map param = new HashMap();
        param.put("clientid", clientId);
        param.put("qos", qos.value());
        param.put("topic", topicfilter);
        log.info("请求emq的相关参数:{}", param);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(param, headers);
        //自动订阅
        if (sub) {
            new Thread(() -> {
                ResponseEntity<String> responseEntity =
                        restTemplate.postForEntity("http://192.168.200.129:8081/api/v4/mqtt/subscribe", entity,
                                String.class);
                log.info("自动订阅的结果:{}", responseEntity.getBody());
            }).start();
            return;
        }
        //自动取消订阅
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("http://192.168.200.129:8081/api/v4/mqtt/unsubscribe", entity,
                        String.class);
        log.info("自动取消订阅的结果:{}", responseEntity.getBody());
    }
}

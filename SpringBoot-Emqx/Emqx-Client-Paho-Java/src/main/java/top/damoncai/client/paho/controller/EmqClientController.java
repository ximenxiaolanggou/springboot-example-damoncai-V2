package top.damoncai.client.paho.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.damoncai.client.paho.client.EmqClient;
import top.damoncai.client.paho.code.QosEnum;

/**
 * <p>
 *
 * </p>
 *
 * @author zhishun.cai
 * @since 2022/2/22 12:56
 */
@RestController
@RequestMapping("emq/client")
public class EmqClientController {

    @Autowired
    private EmqClient emqClient;

    /**
     * 连接
     * @param username
     * @param password
     * @return
     */
    @GetMapping("connect/{username}/{password}")
    public String connect(@PathVariable String username,
                          @PathVariable String password) {
        emqClient.connect(username,password);
        return "success ~~";
    }

    /**
     * 发布消息
     * @param msg
     * @return
     */
    @GetMapping("publish")
    public String publish(@RequestParam String msg) {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    emqClient.publish("testtopic/123",msg, QosEnum.QoS2,false);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        return "消息发送成功 ~~";
    }

    /**
     * 订阅消息
     * @return
     */
    @GetMapping("subscribe")
    public String subscribe() {
        emqClient.subscribe("testtopic/#", QosEnum.QoS2);
        return "消息订阅成功 ~~";
    }

    /**
     * 关闭链接
     * @return
     */
    @GetMapping("disConnect")
    public String disConnect() {
        emqClient.disConnect();
        return "关闭链接成功 ~~";
    }
}

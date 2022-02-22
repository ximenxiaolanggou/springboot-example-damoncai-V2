package top.damoncai.client.paho.callback;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author zhishun.cai
 * @since 2022/2/22 12:53
 */

@Slf4j
@Component
public class MessageCallback implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        //丢失对服务端的连接后触发该方法回调，此处可以做一些特殊处理，比如重连
        log.info("丢失了对broker的连接");
    }
    /**
     * 订阅到消息后的回调
     * 该方法由mqtt客户端同步调用,在此方法未正确返回之前，不会发送ack确认消息到broker
     * 一旦该方法向外抛出了异常客户端将异常关闭，当再次连接时；所有QoS1,QoS2且客户端未进行ack确认的
     消息都将由
     * broker服务器再次发送到客户端
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("订阅到了消息;topic={},messageid={},qos={},msg={}",
                topic,
                message.getId(),
                message.getQos(),
                new String(message.getPayload()));
    }
    /**
     * 消息发布完成且收到ack确认后的回调
     * QoS0：消息被网络发出后触发一次
     * QoS1：当收到broker的PUBACK消息后触发
     * QoS2：当收到broer的PUBCOMP消息后触发
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        int messageId = token.getMessageId();
        String[] topics = token.getTopics();
        log.info("消息发送完成,messageId={},topics={}",messageId,topics);
    }
}

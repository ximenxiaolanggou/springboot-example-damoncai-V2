package top.damoncai.emqx.dynamicproxy.code;

/**
 * <p>
 *
 * </p>
 *
 * @author zhishun.cai
 * @since 2022/2/22 12:52
 */
public enum QosEnum {

    QoS0(0),
    QoS1(1),
    QoS2(2);

    QosEnum(int qos) {
        this.value = qos;
    }
    private final int value;
    public int value(){
        return this.value;
    }
}

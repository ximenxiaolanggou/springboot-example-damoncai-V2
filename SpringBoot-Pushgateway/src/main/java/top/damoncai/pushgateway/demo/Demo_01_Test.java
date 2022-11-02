package top.damoncai.pushgateway.demo;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author zhishun.cai
 * @since 2022/8/31 17:03
 */
public class Demo_01_Test {

    public static final Counter counterDemo = Counter.build()
            .name("push_way_counter")
            .labelNames("application","tsp")
            .help("Counter 实例")
            .register();

    // (time() - (max(push_way_counter) by (exported_job)) / 1000) < 60

    public static void main(String[] args) {
        PushGateway prometheusPush = new PushGateway("192.168.111.12:9091");

        new Thread(() -> {
            while (true) {
                try
                {
                    long tsp = new Date().getTime();
                    counterDemo.labels("application_test1",new Date().getTime() + "").inc(tsp);
                    prometheusPush.push(counterDemo,"job1");
                    Thread.sleep(30000);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

package top.damoncai.pushgateway.task;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *
 * </p>
 *
 * @author zhishun.cai
 * @since 2022/9/15 9:19
 */

//@Component
public class TestTask {

    public static final Counter counterDemo = Counter.build()
            .name("push_way_counter")
            .labelNames("wy","zxjr","ocs","xxjf","unit","instance")
            .help("Counter 实例")
            .register();

    private AtomicInteger count = new AtomicInteger(1);

    @Scheduled(cron = "*/10 * * * * *")
    public void taskOne() {

        try{
            String url = "192.168.111.11:9091";
            CollectorRegistry registry = new CollectorRegistry();
            Gauge guage = Gauge.build("my_custom_metric", "This is my custom metric.").labelNames("app", "date").create();
            String date = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date());
            guage.labels("my-pushgateway-test-0", date).set(25);
            guage.labels("my-pushgateway-test-1", date).dec();
            guage.labels("my-pushgateway-test-2", date).dec(2);
            guage.labels("my-pushgateway-test-3", date).inc();
            guage.labels("my-pushgateway-test-4", date).inc(5);
            guage.register(registry);
            PushGateway pg = new PushGateway(url);
            Map<String, String> groupingKey = new HashMap<String, String>();
            groupingKey.put("instance", "my_instance");
            pg.pushAdd(registry, "my_job", groupingKey);
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("count:" + count.getAndIncrement());
    }

    public static void main(String[] args) {
        long time = new Date().getTime();
        System.out.println(time);
    }
}

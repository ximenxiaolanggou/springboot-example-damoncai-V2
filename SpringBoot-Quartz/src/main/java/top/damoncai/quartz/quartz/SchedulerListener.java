package top.damoncai.quartz.quartz;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author zhishun.cai
 * @date 2020/4/23 9:01
 * @note 监听服务启动启动任务
 */
@Configuration
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private JobCollections jobCollections;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            jobCollections.startJobs();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}

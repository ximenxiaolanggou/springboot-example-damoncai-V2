package top.damoncai.quartz.quartz;

import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author zhishun.cai
 * @date 2020/4/15 14:09
 * @note
 */
@Configuration
public class SchedulerConfig{


    @Bean
    public SchedulerFactoryBean scheduler(JobFactory jobFactory) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setStartupDelay(5);
        bean.setJobFactory(jobFactory);
        return bean;
    }

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        JobFactoryConfig jobFactory = new JobFactoryConfig();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
}

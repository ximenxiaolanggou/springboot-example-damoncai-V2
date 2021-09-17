package top.damoncai.quartz.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhishun.cai
 * @date 2020/4/15 14:07
 * @note job工厂
 */
@Component
public class JobFactoryConfig extends SpringBeanJobFactory implements ApplicationContextAware {

    private transient AutowireCapableBeanFactory autowireCapablebeanFactory;

    private AutowireCapableBeanFactory autowireBean;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // 实例化对象
        Object jobInstance = super.createJobInstance(bundle);
        // 进行注入（Spring管理该Bean）
        autowireBean.autowireBean(jobInstance);
        // 返回对象
        return jobInstance;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        autowireBean = applicationContext.getAutowireCapableBeanFactory();
    }
}

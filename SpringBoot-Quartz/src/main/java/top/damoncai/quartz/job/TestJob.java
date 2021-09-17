package top.damoncai.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author zhishun.cai
 * @date 2021/8/19 14:01
 */

@DisallowConcurrentExecution
public class TestJob implements Job {

    private String name;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Test Job  ~~ " + this.name);
    }

    /**
     * 如果你在job类中，为JobDataMap中存储的数据的key增加set方法（
     * 那么Quartz的默认JobFactory实现在job被实例化的时候会自动调用这些set方法，这样你就不需要在execute()方法中显式地从map中取数据了。
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}

package top.damoncai.quartz.quartz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.damoncai.quartz.job.TestJob;
import top.damoncai.quartz.utils.ScheduleUtil;

/**
 * @author zhishun.cai
 * @date 2020/5/25 14:20
 * @note 定时任务启动集合
 */
@Component
public class JobCollections {

    @Autowired
    private Scheduler scheduler;

    public void startJobs() throws SchedulerException {
        startJob(TestJob.class,"0 1 1 ? * MON");
//        startJob(TestJob.class,"0 1 1 ? * MON");
        scheduler.start();
    }

    /**
     * 开始定时任务
     */
    private void startJob(Class<? extends Job> jobClass, String cronExpression) throws SchedulerException {

        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(new JobKey(ScheduleUtil.JOB_KEY_PREFIX + "1"))
                .build();

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("name","张三");
            // 构建Job（任务）信息
            // 表达式调度构造器
            CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule(cronExpression);
            // 生成触发器
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .usingJobData(jobDataMap)
                    .withIdentity(new TriggerKey(ScheduleUtil.TRIGGER_KEY_PREFIX + "1"))
                    .withSchedule(cronSchedule)
                    .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}

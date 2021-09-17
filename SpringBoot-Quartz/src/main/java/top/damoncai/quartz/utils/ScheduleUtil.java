package top.damoncai.quartz.utils;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * @author zhishun.cai
 * @date 2021/8/19 14:06
 */

@Slf4j
public class ScheduleUtil {

    public static final String JOB_KEY_PREFIX = "Job_Key_";
    public static final String TRIGGER_KEY_PREFIX = "Trigger_Key_";
    public static final String PARAMS_KEY_PREFIX = "Trigger_Key_";


    /**
     * 获取触发器 key
     */
    private static TriggerKey getTriggerKey(Integer jobId) {
        return TriggerKey.triggerKey(jobId + "");
    }

    /**
     * 获取jobKey
     */
    private static JobKey getJobKey(Integer jobId) {
        return JobKey.jobKey(JOB_KEY_PREFIX + jobId);
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, Integer jobId) {
        try {
            JobKey jobKey = getJobKey(jobId);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error("暂停定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     */
    public static void addTrigger(Scheduler scheduler, Trigger trigger,Integer id) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(getJobKey(id));
        } catch (SchedulerException e) {
            log.error("暂停定时任务失败", e);
        }
    }


    /**
     * 立即执行任务(执行一次)
     */
    public static void run(Scheduler scheduler, Integer id,Object data) {
        try {
            JobDataMap dataMap = new JobDataMap();
            if(data != null) {
                dataMap.put(PARAMS_KEY_PREFIX, data);
            }
            scheduler.triggerJob(getJobKey(id));
        } catch (SchedulerException e) {
            log.error("执行定时任务失败", e);
        }
    }

    /**
     * 立即执行任务(执行一次)
     */
    public static void unschedule(Scheduler scheduler, Integer id,Object data) {
        try {
            scheduler.unscheduleJob(getTriggerKey(id));
        } catch (SchedulerException e) {
            log.error("取消Trigger失败", e);
        }
    }


    /**
     * 重置任务
     */
    public static void resume(Scheduler scheduler, Integer id) {
        try {
            scheduler.resumeJob(getJobKey(id));
        } catch (SchedulerException e) {
            log.error("执行定时任务失败", e);
        }
    }
}

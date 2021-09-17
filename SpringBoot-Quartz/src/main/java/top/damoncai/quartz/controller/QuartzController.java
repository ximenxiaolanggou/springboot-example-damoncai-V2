package top.damoncai.quartz.controller;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.damoncai.quartz.utils.ScheduleUtil;

/**
 * @author zhishun.cai
 * @date 2021/8/19 13:58
 */

@RestController
@RequestMapping("quartz")
public class QuartzController {

    @Autowired
    private Scheduler scheduler;

    /**
     * 开始 只运行一次
     * @return
     */
    @GetMapping("run")
    public String run(Integer id) {
        ScheduleUtil.resume(scheduler,id);
        return "success ~~";
    }

    /**
     * 暂停
     * @return
     */
    @GetMapping("pause")
    public String pause(Integer id) {
        ScheduleUtil.pauseJob(scheduler,id);
        return "success ~~";
    }
    /**
     * 暂停
     * @return
     */
    @GetMapping("unschedule")
    public String unschedule(Integer id) {
        ScheduleUtil.pauseJob(scheduler,id);
        return "success ~~";
    }
    @GetMapping("addTrigger")
    public String resumeTrigger(Integer id) {
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule("0/5 * * * * ? ");
        // 生成触发器
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey(ScheduleUtil.TRIGGER_KEY_PREFIX + "2"))
                .withSchedule(cronSchedule)
                .build();
        ScheduleUtil.addTrigger(scheduler,trigger,1);
        return "success ~~";
    }


    /**
     * 重置
     * @return
     */
    @GetMapping("resume")
    public String stop() {
        return "success ~~";
    }
}

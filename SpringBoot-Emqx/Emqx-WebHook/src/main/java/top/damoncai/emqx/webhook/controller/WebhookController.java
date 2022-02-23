package top.damoncai.emqx.webhook.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author zhishun.cai
 * @since 2022/2/22 19:18
 */
@Slf4j
@RestController
@RequestMapping("mqtt")
public class WebhookController {

    @PostMapping("webhook")
    public void webhook(@RequestBody Map map) {
        log.info("data: " + map);
    }
}

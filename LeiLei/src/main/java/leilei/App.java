package leilei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zhishun.cai
 * @date 2021/8/22 18:55
 */

@SpringBootApplication
@RequestMapping
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    @GetMapping("index")
    public ModelAndView index () {
        // 创建并返回 ModelAndView
        ModelAndView mv = new ModelAndView();
        mv.setViewName("leilei"); //设置视图名
        return mv;
    }
}

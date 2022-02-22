package top.damoncai.emqx.http.controller;


import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/mqtt")
public class HttpAuthController {

    private Map<String,String> users;

    @PostConstruct
    public void init(){
        users = new HashMap<>();
        users.put("user","123456");//实际的密码应该是密文,mqtt的http认证组件传输过来的密码是明，我们需要自己进行加密验证
        users.put("emq-client2","123456");
        users.put("emq-client3","123456");
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestParam("clientid") String clientid,
                                  @RequestParam("username") String username,
                                  @RequestParam("password") String password){
        System.out.println("clientid:" + clientid);
        System.out.println("username:" + username);
        System.out.println("password:" + password);
        String value = users.get(username);
        if(StringUtils.isEmpty(value)){
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
        if(!value.equals(password)){
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}

package top.damoncai.emqx.http.controller;


import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/mqtt")
public class HttpAclController {

    private Map<String,String> users;

    @PostConstruct
    public void init(){
        users = new HashMap<>();
        users.put("admin","admin");//实际的密码应该是密文,mqtt的http认证组件传输过来的密码是明，我们需要自己进行加密验证
    }

    @PostMapping("/superuser")
    public ResponseEntity<?> auth(@RequestParam("clientid") String clientid,
                                  @RequestParam("username") String username){
        log.info("clientid:{}",clientid);
        log.info("username:{}",username);
        if(clientid.contains("admin") || username.contains("admin")){
            log.info("用户{}是超级用户",username);
            //是超级用户
            return new ResponseEntity<Object>(HttpStatus.OK);
        }else {
            log.info("用户{}不是超级用户",username);
            //不是超级用户
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/acl")
    public ResponseEntity acl(@RequestParam("access")int access,
                              @RequestParam("username")String username,
                              @RequestParam("clientid")String clientid,
                              @RequestParam("ipaddr")String ipaddr,
                              @RequestParam("topic")String topic,
                              @RequestParam("mountpoint")String mountpoint){
        log.info("EMQX发起客户端操作授权查询请求,access={},username={},clientid={},ipaddr={},topic={},mountpoint={}",
        access,username,clientid,ipaddr,topic,mountpoint);
        if(username.equals("emq-client2") && topic.equals("testtopic/#") && access == 1){
            log.info("客户端{}有权限订阅{}",username,topic);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if(username.equals("emq-client3") && topic.equals("testtopic/123") && access == 2){
            log.info("客户端{}有权限向{}发布消息",username,topic);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        log.info("客户端{},username={},没有权限对主题{}进行{}操作",clientid,username,topic,access==1?"订阅":"发布");
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);//无权限
    }
}

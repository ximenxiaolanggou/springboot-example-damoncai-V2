package top.damoncai.datasource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.damoncai.datasource.entity.User;
import top.damoncai.datasource.service.UserService;

import java.util.List;

/**
 * @author zhishun.cai
 * @date 2021/3/14 12:45
 */

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("test")
    public String test() {
        return "test ...";
    }
}

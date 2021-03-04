package top.damoncai.swagger.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhishun.cai
 * @date 2021/3/3 16:34
 */

@RestController
@RequestMapping
@Api(value = "SpringBoot-Swagger-测试接口", tags = {"SpringBoot-Swagger-测试接口"})
public class IndexController {

    @GetMapping
    @ApiOperation(value = "GET请求", notes = "GET请求 - notes", httpMethod = "GET")
    @ApiOperationSupport(order = 1)
    public String get() {
        return "get。。。";
    }

    @DeleteMapping
    @ApiOperationSupport(order = 2)
    public String delete() {
        return "delete。。。";
    }

    @PutMapping
    @ApiOperationSupport(order = 3)
    public String update() {
        return "update。。。";
    }

    @PostMapping
    @ApiOperationSupport(order = 4)
    public String add() {
        return "add。。。";
    }
}

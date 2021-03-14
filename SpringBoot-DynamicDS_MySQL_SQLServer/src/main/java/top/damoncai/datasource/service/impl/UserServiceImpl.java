package top.damoncai.datasource.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.damoncai.datasource.entity.User;
import top.damoncai.datasource.mapper.UserMapper;
import top.damoncai.datasource.service.UserService;

import java.util.List;

/**
 * @author zhishun.cai
 * @date 2021/3/14 12:46
 */

@Service
@DS("sqlserver")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> findAll() {
        return userMapper.findAll();
    }

}

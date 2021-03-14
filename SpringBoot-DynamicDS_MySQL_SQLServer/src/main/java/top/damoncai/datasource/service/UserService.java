package top.damoncai.datasource.service;

import top.damoncai.datasource.entity.User;

import java.util.List;

/**
 * @author zhishun.cai
 * @date 2021/3/14 12:46
 */

public interface UserService {

    public List<User> findAll();
}

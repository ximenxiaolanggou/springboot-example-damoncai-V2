package top.damoncai.datasource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.damoncai.datasource.entity.User;

import java.util.List;

/**
 * @author zhishun.cai
 * @date 2021/3/14 12:47
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    public List<User> findAll();

}

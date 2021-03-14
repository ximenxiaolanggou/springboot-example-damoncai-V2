package top.damoncai.datasource.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhishun.cai
 * @date 2021/3/14 12:44
 */
@TableName("user")
@Data
@ToString
public class User {

    private Integer id;

    private String name;
}

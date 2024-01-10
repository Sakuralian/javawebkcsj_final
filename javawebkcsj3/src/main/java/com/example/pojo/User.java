package com.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("tb_user")
@Data
public class User{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//用户ID
    @TableField("account")
    private String account;//账号
    @TableField("password")
    private String password;//密码
    @TableField("roleId")
    private Integer roleId;//角色ID

}

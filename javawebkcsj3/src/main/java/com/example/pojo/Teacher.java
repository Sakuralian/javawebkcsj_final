package com.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("tb_teacher")
@Data
public class Teacher{
    @TableId(value = "tea_id",type = IdType.INPUT)
    private Integer teaId;//教师ID
    @TableField("tea_name")
    private String teaName;//教师名称
    @TableField("gender")
    private String gender;//性别
    @TableField("dept")
    private String dept;//教师所属系
    @TableField("tel")
    private String tel;//教师电话
    @TableField("photo")
    private String photo;//教师个人照片
    @TableField("role_id")
    private Integer roleId;//职业ID
    @TableField("account")
    private String account;//教师账号
    @TableField("password")
    private String password;//教师密码

}

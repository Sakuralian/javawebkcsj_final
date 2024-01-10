package com.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tb_student")
@Data
public class Student {
    @TableId(value = "stu_id",type = IdType.INPUT)
    private Integer stuId;//学生ID
    @TableField("stu_name")
    private String stuName;//学生名称
    @TableField("gender")
    private String gender;//性别
    @TableField("dept")
    private String dept;//学生所属系
    @TableField("major")
    private String major;//学生专业
    @TableField("class_name")
    private String className;//班级
    @TableField("role_id")
    private Integer roleId;//职业ID
    @TableField("account")
    private String account;//学生账号
    @TableField("password")
    private String password;//学生密码
}

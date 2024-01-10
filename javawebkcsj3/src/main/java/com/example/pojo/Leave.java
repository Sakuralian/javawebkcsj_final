package com.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@TableName("tb_leave")
@Data
public class Leave implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;//ID
    @TableField("stu_id")
    private Integer stuId;//学生id
    @TableField("stu_name")
    private String stuName;//学生名
    @TableField("stu_dept")
    private String stuDept;//学生所属系
    @TableField("start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;//开始请假时间
    @TableField("end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;//结束请假时间
    @TableField("reason")
    private String reason;//请假原因
    @TableField("file")
    private String file;//附件
    @TableField("audit")
    private String audit;//审核状态
}

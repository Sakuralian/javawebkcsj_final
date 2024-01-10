package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojo.Student;
import com.example.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    Page<Student> queryPageStudent(@Param("page") Page<Student> page);

    @Select("SELECT MAX(stu_id) FROM tb_student")
    Integer lastStuId();

    @Select("select * from tb_user u, tb_student s where u.account=s.account and u.account=#{account}")
    Student findStuName(Student student);
}

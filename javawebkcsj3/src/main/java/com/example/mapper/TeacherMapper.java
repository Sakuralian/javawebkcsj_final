package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojo.Student;
import com.example.pojo.Teacher;
import com.example.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    Page<Teacher> queryPageTeacher(@Param("page") Page<Teacher> page);

    @Select("SELECT MAX(tea_id) FROM tb_teacher")
    Integer lastTeaId();

    @Select("select * from tb_user u, tb_teacher t where u.account=t.account and u.account=#{account}")
    Teacher findTeaName(Teacher teacher);
}

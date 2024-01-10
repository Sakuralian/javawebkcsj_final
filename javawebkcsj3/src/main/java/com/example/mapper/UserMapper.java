package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.Student;
import com.example.pojo.Teacher;
import com.example.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from tb_user " +
            "where account=#{account} and password=#{password} and role_id=#{roleId}")
    User login(User user);
    @Select("select u.id from tb_user u,tb_student s where u.account=s.account and u.account=#{account}")
    Integer findStuUserId(User user);

    @Select("select u.id from tb_user u,tb_teacher t where u.account=t.account and u.account=#{account}")
    Integer findTeaUserId(User user);
    @Update("update tb_user set password=#{password} where account=#{account}")
    Integer updatePassword(User user);
}

package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojo.Leave;
import com.example.pojo.Teacher;
import com.example.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

@Mapper
public interface LeaveMapper extends BaseMapper<Leave> {

    Page<Leave> queryPageLeave(@Param("page") Page<Leave> page);
    @Select("select * from tb_leave l, tb_student s where l.stu_id=s.stu_id and l.stu_id=#{stuId}")
    Page<Leave> queryPageLeaveByStuId(@Param("page") Page<Leave> page, @Param("stuId") Integer stuId);

    @Select("SELECT MAX(id) FROM tb_leave l, tb_student s where l.stu_id=s.stu_id and l.stu_id=#{stuId}")
    Integer lastIdByStuId(@Param("stuId") Integer stuId);
    @Update("update tb_leave set audit = #{audit} where id = #{id}")
    int updateAuditById(@Param("id") Integer id, @Param("audit") String audit);
}

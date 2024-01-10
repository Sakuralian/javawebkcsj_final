package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.TeacherMapper;
import com.example.pojo.Teacher;
import com.example.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "teacher")
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService{
    @Autowired
    private TeacherMapper teacherMapper;

    @Cacheable(key = "'allTeachers' + #pageNum + #pageSize")
    public Page<Teacher> getTeacherPage(int pageNum, int pageSize) {
        Page<Teacher> page = new Page<>(pageNum, pageSize);
        return teacherMapper.queryPageTeacher(page);
    }
}

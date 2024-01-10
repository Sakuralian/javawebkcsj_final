package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.StudentMapper;
import com.example.pojo.Student;
import com.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "student")
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService{
    @Autowired
    private StudentMapper studentMapper;

    @Cacheable(key = "'allStudents' + #pageNum + #pageSize")
    public Page<Student> getStudentPage(int pageNum, int pageSize) {
        Page<Student> page = new Page<>(pageNum, pageSize);
        return studentMapper.queryPageStudent(page);
    }
}

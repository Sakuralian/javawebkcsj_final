package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.Student;

public interface StudentService extends IService<Student> {
    Page<Student> getStudentPage(int pageNum, int pageSize);
}

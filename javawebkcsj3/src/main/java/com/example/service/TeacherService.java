package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    Page<Teacher> getTeacherPage(int pageNum, int pageSize);
}

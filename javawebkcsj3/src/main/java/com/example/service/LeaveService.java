package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.Leave;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;

public interface LeaveService extends IService<Leave> {
    Page<Leave> getLeavePage(int pageNum, int pageSize);
    int updateAuditById(@Param("id") Integer id, @Param("audit") String audit);

}

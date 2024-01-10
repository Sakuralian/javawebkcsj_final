package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.LeaveMapper;
import com.example.pojo.Leave;
import com.example.service.LeaveService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "leave")
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements LeaveService{
    @Autowired
    private LeaveMapper leaveMapper;

    @Cacheable(key = "'allLeaves' + #pageNum + #pageSize")
    public Page<Leave> getLeavePage(int pageNum, int pageSize) {
        Page<Leave> page = new Page<>(pageNum, pageSize);
        return leaveMapper.queryPageLeave(page);
    }
    public Page<Leave> getLeavePageByStuId(int pageNum, int pageSize, Integer stuId) {
        Page<Leave> page = new Page<>(pageNum, pageSize);
        return leaveMapper.queryPageLeaveByStuId(page,stuId);
    }
    public int updateAuditById(@Param("id") Integer id, @Param("audit") String audit){

        return leaveMapper.updateAuditById(id,audit);
    }

}
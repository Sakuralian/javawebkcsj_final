package com.example.config.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mapper.LeaveMapper;
import com.example.mapper.TeacherMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.Leave;
import com.example.pojo.Teacher;
import com.example.pojo.User;
import com.example.service.LeaveService;
import com.example.service.impl.LeaveServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class LeaveController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LeaveMapper leaveMapper;
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private TeacherMapper teacherMapper;

    //显示所有请假信息
    @RequestMapping("/findAllLeaves")
    public String showAllLeaves(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                User user, HttpSession session, Model model, HttpServletRequest req){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        Page<Leave> page = leaveService.getLeavePage(pageNum, pageSize);
        List<Leave> leaveList = page.getRecords();

        req.setAttribute("leaveList",leaveList);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());
        return "admin/showAllLeaves";
    }

    //根据学生名称进行模糊查询
    @RequestMapping("/findByStuName")
    public String findByStuName(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                String value,Teacher teacher, HttpSession session, Model model,HttpServletRequest req){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        QueryWrapper<Leave> queryWrapper =new QueryWrapper<>();
        queryWrapper.like("stu_name",value);

        Page<Leave> page = new Page<>(pageNum, pageSize);
        IPage<Leave> leaveIPage = leaveService.page(page, queryWrapper);
        List<Leave> leaves=leaveIPage.getRecords();

        req.setAttribute("leaveList",leaves);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());

        model.addAttribute("value", value);
        boolean isNumeric = value.matches("\\d+");
        model.addAttribute("isNumeric", isNumeric);

        return "admin/showAllLeaves";
    }

    //根据系别进行查询
    @RequestMapping("/findByStuDept")
    public String findByStuDept(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                String value,Teacher teacher, HttpSession session, Model model,HttpServletRequest req){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        QueryWrapper<Leave> queryWrapper =new QueryWrapper<>();
        queryWrapper.like("stu_dept",value);

        Page<Leave> page = new Page<>(pageNum, pageSize);
        IPage<Leave> leaveIPage = leaveService.page(page, queryWrapper);
        List<Leave> leaves=leaveIPage.getRecords();

        req.setAttribute("leaveList",leaves);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());

        model.addAttribute("value", value);
        boolean isNumeric = value.matches("\\d+");
        model.addAttribute("isNumeric", isNumeric);

        return "admin/showAllLeaves";
    }

    //根据假条ID查询请假信息,跳转审核页面
    @CacheEvict(value = "leave", allEntries = true)
    @RequestMapping("/findLeaveById/{id}")
    public String findLeaveById(@PathVariable("id") Integer id, HttpSession session, Model model){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        Leave leave=leaveMapper.selectById(id);
        int updatedRows = leaveMapper.updateAuditById(id,"审核中");
        model.addAttribute("l",leave);
        return "admin/auditLeave";
    }

    //审核请假信息
    @RequestMapping("/updateLeave")
    public String updateLeave(Leave leave, HttpSession session, Model model){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        int rows=leaveMapper.updateById(leave);

        return "redirect:findAllLeaves";
    }

    //删除请假信息
    @CacheEvict(value = "leave", allEntries = true)
    @RequestMapping("/deleteLeaveById/{id}")
    public String  deleteLeaveById(@PathVariable Integer id, HttpServletRequest request,User user, HttpSession session, Model model) {
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        int rows=leaveMapper.deleteById(id);
        return "redirect:/admin/findAllLeaves";
    }

    //批量删除学生请假信息
    @CacheEvict(value = "leave", allEntries = true)
    @RequestMapping("/deleteLeavesByIds")
    public String deleteLeavesByIds(Integer[] ids,Model model,HttpSession session){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        int rows=leaveMapper.deleteBatchIds(Arrays.asList(ids));
        return "redirect:findAllLeaves";
    }

}

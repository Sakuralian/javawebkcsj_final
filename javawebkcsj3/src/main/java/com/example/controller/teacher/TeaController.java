package com.example.controller.teacher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mapper.LeaveMapper;
import com.example.mapper.TeacherMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.Leave;
import com.example.pojo.Student;
import com.example.pojo.Teacher;
import com.example.pojo.User;
import com.example.service.LeaveService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher")
public class TeaController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LeaveMapper leaveMapper;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private TeacherMapper teacherMapper;

    @RequestMapping("/toTeacher")
    public String toTeacher(Teacher teacher, HttpSession session, Model model){
        Teacher teaFromSession = (Teacher) session.getAttribute("TEA_SESSION");
        model.addAttribute("teacher", teaFromSession);
        return "teacher/teacher";
    }

    //显示所有请假信息
    @RequestMapping("/findAllLeaves")
    public String showAllLeaves(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                  Teacher teacher, HttpSession session, Model model, HttpServletRequest req){
        Teacher teaFromSession = (Teacher) session.getAttribute("TEA_SESSION");
        model.addAttribute("teacher", teaFromSession);

        Page<Leave> page = leaveService.getLeavePage(pageNum, pageSize);
        List<Leave> leaveList = page.getRecords();

        req.setAttribute("leaveList",leaveList);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());
        return "teacher/showAllLeaves";
    }

    //根据学生名称进行模糊查询
    @RequestMapping("/findByStuName")
    public String findByStuName(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                    String value,Teacher teacher, HttpSession session, Model model,HttpServletRequest req){
        Teacher teaFromSession = (Teacher) session.getAttribute("TEA_SESSION");
        model.addAttribute("teacher", teaFromSession);

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

        return "teacher/showAllLeaves";
    }

    //根据系别进行查询
    @RequestMapping("/findByStuDept")
    public String findByStuDept(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                  String value,Teacher teacher, HttpSession session, Model model,HttpServletRequest req){
        Teacher teaFromSession = (Teacher) session.getAttribute("TEA_SESSION");
        model.addAttribute("teacher", teaFromSession);

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

        return "teacher/showAllLeaves";
    }

    //根据假条ID查询请假信息,跳转审核页面
    @CacheEvict(value = "leave", allEntries = true)
    @RequestMapping("/findLeaveById/{id}")
    public String findLeaveById(@PathVariable("id") Integer id, HttpSession session, Model model){
        Teacher teaFromSession = (Teacher) session.getAttribute("TEA_SESSION");
        model.addAttribute("teacher", teaFromSession);

        Leave leave=leaveMapper.selectById(id);
        int updatedRows = leaveService.updateAuditById(id,"审核中");
        model.addAttribute("l",leave);
        return "teacher/auditLeave";
    }

    //审核请假信息
    @CacheEvict(value = "leave", allEntries = true)
    @RequestMapping("/updateLeave")
    public String updateLeave(Leave leave, HttpSession session, Model model){
        Teacher teaFromSession = (Teacher) session.getAttribute("TEA_SESSION");
        model.addAttribute("teacher", teaFromSession);

        int rows=leaveMapper.updateById(leave);

        return "redirect:findAllLeaves";
    }

    //密码修改
    @RequestMapping("/toUpdatePassword")
    public String toUpdatePassWord(User user,Teacher teacher, HttpSession session, Model model){
        Teacher teaFromSession = (Teacher) session.getAttribute("TEA_SESSION");
        model.addAttribute("teacher", teaFromSession);
        user.setAccount(teaFromSession.getAccount());
        int u=userMapper.findTeaUserId(user);

        model.addAttribute("u",u);

        return "teacher/updatePassword";
    }

    @RequestMapping("/updatePassword")
    public String updatePassword(User user, HttpSession session, Model model, HttpServletRequest request, Map<String, Object> map){
        Teacher teaFromSession = (Teacher) session.getAttribute("TEA_SESSION");
        model.addAttribute("teacher", teaFromSession);

        String newPassword = request.getParameter("newPassword");
        String password = request.getParameter("password");
        if (newPassword.equals(password)){
            int rows=userMapper.updatePassword(user);
            map.put("successMessage", "密码修改成功！");
            return "redirect:findAllLeaves";
        }else {
            map.put("failMessage", "新密码与确认密码不一致，请重新输入！");
            return "teacher/updatePassword";
        }
    }

    //实现文件下载功能
    @RequestMapping("/download")
    public ResponseEntity<byte[]> fileDownload(HttpServletRequest request,
                                               String filename) throws Exception{
        // 指定要下载的文件所在路径
        Resource resource = new ClassPathResource("static/images/" + filename);
        // 创建该文件对象
        File file = resource.getFile();
        // 对文件名编码，防止中文文件乱码
        filename = this.getFilename(request, filename);
        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        // 通知浏览器以下载的方式打开文件
        headers.setContentDispositionFormData("attachment", filename);
        // 定义以流的形式下载返回文件数据
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // 使用Sring MVC框架的ResponseEntity对象封装返回下载数据
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.OK);
    }

    //文件名编码
    public String getFilename(HttpServletRequest request,
                              String filename) throws Exception {
        // IE不同版本User-Agent中出现的关键词
        String[] IEBrowserKeyWords = {"MSIE", "Trident", "Edge"};
        // 获取请求头代理信息
        String userAgent = request.getHeader("User-Agent");
        for (String keyWord : IEBrowserKeyWords) {
            if (userAgent.contains(keyWord)) {
                //IE内核浏览器，统一为UTF-8编码显示
                return URLEncoder.encode(filename, StandardCharsets.UTF_8);
            }
        }
        //火狐等其它浏览器统一为ISO-8859-1编码显示
        return new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }
}

package com.example.controller.student;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mapper.LeaveMapper;
import com.example.mapper.StudentMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.Leave;
import com.example.pojo.Student;
import com.example.pojo.User;
import com.example.service.LeaveService;
import com.example.service.StudentService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/student")
public class StuController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LeaveMapper leaveMapper;
    @Autowired
    private LeaveService leaveService;

    @RequestMapping("/toStudent")
    public String toStudent(Student student, HttpSession session, Model model){
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);
        return "student/student";
    }

    //显示请假信息
    @RequestMapping("/findAllLeaveByStuId")
    public String showAllLeavesByStuId(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                  Student student, HttpSession session, Model model, HttpServletRequest req){
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);

        Page<Leave> page = new Page<>(pageNum, pageSize);
        leaveMapper.queryPageLeaveByStuId(page,stuFromSession.getStuId());
        List<Leave> leaves = page.getRecords();
        req.setAttribute("leaveList",leaves);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());
        return "student/showAllLeave";
    }

    //根据审核状态进行查询
    @RequestMapping("/findByLeaveAudit")
    public String findByLeaveAudit(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                  String value,Student student, HttpSession session, Model model,HttpServletRequest req){
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);

        QueryWrapper<Leave> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("stu_id", stuFromSession.getStuId());
        queryWrapper.like("audit",value);
        Page<Leave> page = new Page<>(pageNum, pageSize);
        IPage<Leave> leaveIPage = leaveService.page(page,queryWrapper);

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

        return "student/showAllLeave";
    }

    //申请请假
    @CacheEvict(value = "leave", allEntries = true)
    @GetMapping(value = "/toAddLeave")
    public String toAddLeave(Student student, HttpSession session, Model model){
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);
        return "student/addLeave";
    }

    @PostMapping(value = "/addLeave")
    public String addLeave(Leave leave,Student student,HttpSession session,HttpServletRequest request,Model model){
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);

        boolean rows= leaveService.save(leave);

        return "redirect:findAllLeaveByStuId";
    }

    //根据假条ID查询请假信息,返回更新页面
    @RequestMapping("/findLeaveById/{id}")
    public String findLeaveById(@PathVariable("id") Integer id, HttpSession session, Model model){
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);

        Leave leave=leaveMapper.selectById(id);
        model.addAttribute("l",leave);
        return "student/updateLeave";
    }

    //修改请假信息
    @CacheEvict(value = "leave", allEntries = true)
    @RequestMapping("/updateLeave")
    public String updateLeave(Leave leave, HttpSession session, Model model){
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);

        int rows=leaveMapper.updateById(leave);

        return "redirect:findAllLeaveByStuId";
    }

    //删除请假信息
    @CacheEvict(value = "leave", allEntries = true)
    @RequestMapping("/deleteLeaveById/{id}")
    public String  deleteLeaveById(@PathVariable("id") Integer id, HttpServletRequest request, HttpSession session, Model model) {
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);

        int rows=leaveMapper.deleteById(id);
        return "redirect:/student/findAllLeaveByStuId";
    }

    //批量删除请假信息
    @CacheEvict(value = "leave", allEntries = true)
    @RequestMapping("/deleteLeavesByIds")
    public String deleteLeavesByIds(Integer[] ids,Model model,HttpSession session){
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);

        int rows=leaveMapper.deleteBatchIds(Arrays.asList(ids));
        return "redirect:findAllLeaveByStuId";
    }

    //密码修改
    @RequestMapping("/toUpdatePassword")
    public String toUpdatePassWord(User user,Student student, HttpSession session, Model model){
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);
        user.setAccount(stuFromSession.getAccount());
        int u=userMapper.findStuUserId(user);

        model.addAttribute("u",u);

        return "student/updatePassword";
    }

    @RequestMapping("/updatePassword")
    public String updatePassword(User user, HttpSession session, Model model, HttpServletRequest request, Map<String, Object> map){
        Student stuFromSession = (Student) session.getAttribute("STU_SESSION");
        model.addAttribute("student", stuFromSession);

        String newPassword = request.getParameter("newPassword");
        String password = request.getParameter("password");
        if (newPassword.equals(password)){
            int rows=userMapper.updatePassword(user);
            map.put("successMessage", "密码修改成功！");
            return "redirect:findAllLeaveByStuId";
        }else {
            map.put("failMessage", "新密码与确认密码不一致，请重新输入！");
            return "student/updatePassword";
        }
    }

    //实现文件上传
    @PostMapping("/upload")
    public String Upload(Model model, MultipartFile file, HttpSession session, HttpServletRequest request) throws IOException {
        //获取学生名信息
        String stuName = request.getParameter("stuName");
        if (!file.isEmpty()){
            String dirPath111 = "D:/学习/Java Web应用开发框架/project/javawebkcsj_final/javawebkcsj3/src/main/resources/static/upload/leave/";
            File filePath = new File(dirPath111);
            if (!filePath.exists()){
                filePath.mkdir();
            }
            String photoName = stuName+"_"+UUID.randomUUID()+"_"+file.getOriginalFilename();
            try {
                file.transferTo(new File(dirPath111+photoName));
            } catch (IOException e){
                e.printStackTrace();
                return "student/errorUpdate";
            }
            return "student/myInfoFile";
        } else {
            return "student/errorUpdate";
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


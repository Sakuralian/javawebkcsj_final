package com.example.config.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mapper.TeacherMapper;
import com.example.pojo.Student;
import com.example.pojo.Teacher;
import com.example.pojo.User;
import com.example.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherMapper teacherMapper;

    //显示所有教师信息
    @RequestMapping("/findAllTeachers")
    public String showAllTeachers(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                  User user, HttpSession session, Model model,HttpServletRequest req){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        Page<Teacher> page = teacherService.getTeacherPage(pageNum, pageSize);
        List<Teacher> teachers = page.getRecords();

        req.setAttribute("teacherList",teachers);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());
        return "admin/showAllTeachers";
    }

    //根据教师名称进行模糊查询
    @RequestMapping("/findByTeacherName")
    public String findByTeacherName(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                    String value,User user, HttpSession session, Model model,HttpServletRequest req){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        QueryWrapper<Teacher> queryWrapper =new QueryWrapper<>();
        queryWrapper.like("tea_name",value);

        Page<Teacher> page = new Page<>(pageNum, pageSize);
        IPage<Teacher> teacherPage = teacherService.page(page, queryWrapper);
        List<Teacher> teachers=teacherPage.getRecords();

        req.setAttribute("teacherList",teachers);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());

        model.addAttribute("value", value);
        boolean isNumeric = value.matches("\\d+");
        model.addAttribute("isNumeric", isNumeric);

        return "admin/showAllTeachers";
    }

    //根据教师ID进行查询
    @RequestMapping("/findByTeacherId")
    public String findByTeacherId(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                  String value,User user, HttpSession session, Model model,HttpServletRequest req){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        QueryWrapper<Teacher> queryWrapper =new QueryWrapper<>();
        queryWrapper.like("tea_id",value);
        Page<Teacher> page = new Page<>(pageNum, pageSize);
        IPage<Teacher> teacherPage = teacherService.page(page, queryWrapper);
        List<Teacher> teachers=teacherPage.getRecords();

        req.setAttribute("teacherList",teachers);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());

        model.addAttribute("value", value);
        boolean isNumeric = value.matches("\\d+");
        model.addAttribute("isNumeric", isNumeric);

        return "admin/showAllTeachers";
    }

    //添加教师信息
    @CacheEvict(value = "teacher", allEntries = true)
    @GetMapping(value = "/toAddTeacher")
    public String toAddTeacher(User user, HttpSession session, Model model){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);
        int newTeaId = teacherMapper.lastTeaId()+1;
        model.addAttribute("newTeaId",newTeaId);
        System.out.println(newTeaId);
        return "admin/addTeacher";
    }

    @PostMapping(value = "/addTeacher")
    public String addTeacher(Teacher teacher,HttpSession session,HttpServletRequest request,Model model){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        boolean rows= teacherService.save(teacher);

        return "redirect:findAllTeachers";
    }

    //根据教师ID查询教师信息,返回更新页面
    @RequestMapping("/findTeacherById/{teaId}")
    public String findTeacherById(@PathVariable("teaId") Integer teaId, User user, HttpSession session, Model model){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        Teacher teacher=teacherMapper.selectById(teaId);
        model.addAttribute("t",teacher);
        return "admin/updateTeacher";
    }

    //修改教师信息
    @CacheEvict(value = "teacher", allEntries = true)
    @RequestMapping("/updateTeacher")
    public String updateTeacher(Teacher teacher,User user, HttpSession session, Model model,MultipartFile photo){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        int rows=teacherMapper.updateById(teacher);

        return "redirect:findAllTeachers";
    }

    //删除教师信息
    @CacheEvict(value = "teacher", allEntries = true)
    @RequestMapping("/deleteTeacherById/{teaId}")
    public String  deleteTeacherById(@PathVariable Integer teaId, HttpServletRequest request,User user, HttpSession session, Model model) {
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        int rows=teacherMapper.deleteById(teaId);
        return "redirect:/admin/findAllTeachers";
    }

//    //批量删除教师信息
    @CacheEvict(value = "teacher", allEntries = true)
    @RequestMapping("/deleteTeachersByIds")
    public String deleteTeachersByIds(Integer[] ids,Model model,HttpSession session){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        int rows=teacherMapper.deleteBatchIds(Arrays.asList(ids));
        return "redirect:findAllTeachers";
    }


    //实现文件上传
    @PostMapping("/upload")
    public String Upload(Model model, MultipartFile photo, HttpSession session, HttpServletRequest request) throws IOException {
        //获取登录用户账号信息
        String username = request.getParameter("teaName");
        if (!photo.isEmpty()){
            String dirPath = "D:/学习/Java Web应用开发框架/project/javawebkcsj_final/javawebkcsj3/src/main/resources/static/upload/teacher/";
            File filePath = new File(dirPath);
            if (!filePath.exists()){
                filePath.mkdir();
            }
            String photoName = username+"_"+UUID.randomUUID()+"_"+photo.getOriginalFilename();
            try {
                photo.transferTo(new File(dirPath+photoName));
            } catch (IOException e){
                e.printStackTrace();
                return "admin/errorUpdate";
            }
            return "admin/myInfoFile";
        } else {
            return "admin/errorUpdate";
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

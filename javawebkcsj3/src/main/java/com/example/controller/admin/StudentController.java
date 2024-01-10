package com.example.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mapper.StudentMapper;
import com.example.pojo.Leave;
import com.example.pojo.Student;
import com.example.pojo.User;
import com.example.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentMapper studentMapper;

    //显示所有学生信息
    @RequestMapping("/findAllStudents")
    public String showAllStudents(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                  User user, HttpSession session, Model model, HttpServletRequest req){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        Page<Student> page = studentService.getStudentPage(pageNum, pageSize);
        List<Student> students = page.getRecords();
//        List<Student> students=studentService.list();
        req.setAttribute("studentList",students);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());
        return "admin/showAllStudents";
    }

    //根据学生名称进行模糊查询
    @RequestMapping("/findByStudentName")
    public String findByStudentName(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                    String value,User user, HttpSession session, Model model,HttpServletRequest req){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        QueryWrapper<Student> queryWrapper =new QueryWrapper<>();
        queryWrapper.like("stu_name",value);

        Page<Student> page = new Page<>(pageNum, pageSize);
        IPage<Student> studentPage = studentService.page(page, queryWrapper);
        List<Student> students=studentPage.getRecords();

        req.setAttribute("studentList",students);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());

        model.addAttribute("value", value);
        boolean isNumeric = value.matches("\\d+");
        model.addAttribute("isNumeric", isNumeric);

        return "admin/showAllStudents";
    }

    //根据学生ID进行查询
    @RequestMapping("/findByStudentId")
    public String findByStudentId(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "4") int pageSize,
                                  String value,User user, HttpSession session, Model model,HttpServletRequest req){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        QueryWrapper<Student> queryWrapper =new QueryWrapper<>();
        queryWrapper.like("stu_id",value);
        Page<Student> page = new Page<>(pageNum, pageSize);
        IPage<Student> studentPage = studentService.page(page, queryWrapper);
        List<Student> students=studentPage.getRecords();

        req.setAttribute("studentList",students);
        model.addAttribute("currentPage", page.getCurrent());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalRecords", page.getTotal());
        model.addAttribute("totalPages", page.getPages());
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());

        model.addAttribute("value", value);
        boolean isNumeric = value.matches("\\d+");
        model.addAttribute("isNumeric", isNumeric);

        return "admin/showAllStudents";
    }

    //添加学生信息
    @CacheEvict(value = "student", allEntries = true)
    @GetMapping(value = "/toAddStudent")
    public String toAddStudent(User user, HttpSession session, Model model){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);
        int newStuId = studentMapper.lastStuId()+1;
        model.addAttribute("newStuId",newStuId);
        System.out.println(newStuId);
        return "admin/addStudent";
    }

    @PostMapping(value = "/addStudent")
    public String addStudent(Student student,HttpSession session,HttpServletRequest request,Model model){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        boolean rows= studentService.save(student);

        return "redirect:findAllStudents";
    }

    //根据学生ID查询学生信息,返回更新页面
    @RequestMapping("/findStudentById/{stuId}")
    public String findStudentById(@PathVariable("stuId") Integer stuId, User user, HttpSession session, Model model){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        Student student=studentMapper.selectById(stuId);
        model.addAttribute("t",student);
        return "admin/updateStudent";
    }

    //修改学生信息
    @CacheEvict(value = "student", allEntries = true)
    @RequestMapping("/updateStudent")
    public String updateStudent(Student student, User user, HttpSession session, Model model, MultipartFile photo){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        int rows=studentMapper.updateById(student);

        return "redirect:findAllStudents";
    }

    //删除学生信息
    @CacheEvict(value = "student", allEntries = true)
    @RequestMapping("/deleteStudentById/{stuId}")
    public String  deleteStudentById(@PathVariable Integer stuId, HttpServletRequest request,User user, HttpSession session, Model model) {
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        int rows=studentMapper.deleteById(stuId);
        return "redirect:/admin/findAllStudents";
    }

    //批量删除学生信息
    @CacheEvict(value = "student", allEntries = true)
    @RequestMapping("/deleteStudentsByIds")
    public String deleteStudentsByIds(Integer[] ids,Model model,HttpSession session){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);

        int rows=studentMapper.deleteBatchIds(Arrays.asList(ids));
        return "redirect:findAllStudents";
    }

}

package com.example.controller;

import com.example.mapper.StudentMapper;
import com.example.mapper.TeacherMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.Student;
import com.example.pojo.Teacher;
import com.example.pojo.User;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private Producer codeProducer;

    //生成验证码图片
    @GetMapping("/common/kaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String randStr = codeProducer.createText();
            httpServletRequest.getSession().setAttribute("randStr", randStr);
            BufferedImage challenge = codeProducer.createImage(randStr);
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @RequestMapping("/login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(){
        return "redirect:login";
    }

    @RequestMapping(value = "/loginCheck",method = RequestMethod.POST)
    public String dologin(Teacher teacher,Student student, User user, HttpSession session, Model model, HttpServletRequest request, Map<String, Object> map){
        User u = userMapper.login(user);
        Student stu = studentMapper.findStuName(student);
        Teacher tea = teacherMapper.findTeaName(teacher);

        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String code = request.getParameter("code");
        String randStr = (String) request.getSession().getAttribute("randStr");

        System.out.println(randStr);

        Integer roleId = Integer.valueOf(request.getParameter("roleId"));
        if (account.equals("") || password.equals("")){
            map.put("msg","用户名或密码为空，请重新输入");
            return "login";
        }
        if (code.equals("")){
            map.put("msg","验证码为空");
            return "login";
        }
        if (!code.equals(randStr)){
            map.put("msg","验证码错误，请重新登录");
            return "login";
        }
        if (u!= null){
            session.setAttribute("USER_SESSION",u);
            session.setAttribute("STU_SESSION",stu);
            session.setAttribute("TEA_SESSION",tea);
            session.setAttribute("ROLE_ID",roleId);
            if (roleId == 0) {
                System.out.println(roleId);
                return "admin/admin";
            } else if (roleId == 1) {
                System.out.println(roleId);
                return "redirect:teacher/toTeacher";
            } else {
                System.out.println(roleId);
                return "redirect:student/toStudent";
            }
        }else {
            map.put("msg", "用户名或密码错误，请重新登录");
            return "login";
        }
    }

}

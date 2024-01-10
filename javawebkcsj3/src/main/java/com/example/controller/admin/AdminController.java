package com.example.controller.admin;

import com.example.pojo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/toAdmin")
    public String toAdmin(User user, HttpSession session, Model model){
        User userFromSession = (User) session.getAttribute("USER_SESSION");
        model.addAttribute("user", userFromSession);
        return "admin/admin";
    }
}

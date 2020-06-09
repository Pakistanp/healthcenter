package com.internet.applications.project.healthcenter.controller;

import com.internet.applications.project.healthcenter.model.User;
import com.internet.applications.project.healthcenter.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    @NonNull
    private final UserService userService;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String pesel,
            @RequestParam String phonenumber,
            @RequestParam String email,
            @RequestParam String password
    ) {
        userService.addNewUser(firstname, lastname, pesel, phonenumber, email, password);
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "login";
    }

    @GetMapping("/doctors")
    public ModelAndView doctorsPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("doctors");
        List<User> doctors = userService.getAllDoctors();
        User user = userService.getUserByUsername(request.getRemoteUser());
        modelAndView.addObject("doctors", doctors);
        modelAndView.addObject("userId", user.getId());
        modelAndView.addObject("role", user.getType());
        return modelAndView;
    }
}

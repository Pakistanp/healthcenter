package com.internet.applications.project.healthcenter.controller;

import com.internet.applications.project.healthcenter.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String loginPage(@RequestParam(required = false) String registeredUser) {
        return "login";
    }

    @GetMapping("/doctors")
    public String doctorsPage() {

        return "schedule";
    }
}

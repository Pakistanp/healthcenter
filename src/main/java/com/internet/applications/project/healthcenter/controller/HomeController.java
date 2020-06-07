package com.internet.applications.project.healthcenter.controller;

import com.internet.applications.project.healthcenter.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HomeController {

    @NonNull
    private final UserService userService;

    @GetMapping("/")
    public ModelAndView homePage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject( "role", userService.getUserByUsername(request.getRemoteUser()).getType());
        return modelAndView;
    }
}

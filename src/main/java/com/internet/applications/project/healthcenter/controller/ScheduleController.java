package com.internet.applications.project.healthcenter.controller;

import com.internet.applications.project.healthcenter.model.Schedule;
import com.internet.applications.project.healthcenter.model.ScheduleHour;
import com.internet.applications.project.healthcenter.model.ScheduleHourCollection;
import com.internet.applications.project.healthcenter.service.ScheduleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleController {

    @NonNull
    private final ScheduleService scheduleService;

    @GetMapping("/doctor/{dId}/schedule/{pId}")
    public ModelAndView scheduleDoctorPage(@PathVariable("dId") int dId, @PathVariable("pId") int pId)  {
        ModelAndView modelAndView = new ModelAndView("schedule");
        List<ScheduleHourCollection> schedulesHourCollection = scheduleService.createSchedule(dId);
        modelAndView.addObject( "schedulesHourCollection", schedulesHourCollection);
        modelAndView.addObject("userId", pId);
        modelAndView.addObject("docId", dId);
        return modelAndView;
    }

    @PostMapping("/doctor/{dId}/schedule/{pId}/{day}/{time}")
    public String scheduleCreate(@PathVariable("dId") int dId, @PathVariable("pId") int pId, @PathVariable("day") int dayOfWeek) {
        return "home";
    }
}

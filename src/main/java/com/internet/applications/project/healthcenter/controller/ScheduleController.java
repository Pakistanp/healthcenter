package com.internet.applications.project.healthcenter.controller;

import com.internet.applications.project.healthcenter.model.*;
import com.internet.applications.project.healthcenter.service.ScheduleService;
import com.internet.applications.project.healthcenter.service.UserService;
import com.internet.applications.project.healthcenter.service.VisitService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleController {

    @NonNull
    private final ScheduleService scheduleService;
    @NonNull
    private final UserService userService;
    @NonNull
    private final VisitService visitService;

    @GetMapping(value = "/doctor/{dId}/schedule/{pId}")
    public ModelAndView scheduleDoctorPage(@PathVariable("dId") int dId, @PathVariable("pId") int pId,
                                           @RequestParam(name = "action", required = false) String action,
                                           @RequestParam(name = "shiftWeeks", required = false, defaultValue = "0") int shiftWeeks,
                                           HttpServletRequest request)  {
        User user = userService.getUserByUsername(request.getRemoteUser());
        List<ScheduleHourCollection> schedulesHourCollection = scheduleService.createWeekSchedule(dId, pId, action, shiftWeeks);
        HashMap<Integer,User> patients = scheduleService.getAllPatients();
        HashMap<Integer, Visit> visitHashMap = visitService.getVisitsAllVisits();
        ModelAndView modelAndView = new ModelAndView("schedule");
        modelAndView.addObject( "schedulesHourCollection", schedulesHourCollection);
        modelAndView.addObject("userId", pId);
        modelAndView.addObject("docId", dId);
        modelAndView.addObject("role", user.getType());
        modelAndView.addObject("patients", patients);
        modelAndView.addObject("visits", visitHashMap);
        return modelAndView;
    }

    @PostMapping(value = "/doctor/{dId}/schedule/{pId}")
    public ModelAndView createSchedule(@PathVariable("dId") int doctorId, @PathVariable("pId") int patientId,
                                       @RequestParam(name = "date", required = false) String localDateTime,
                                       @RequestParam(name = "shiftWeeks", required = false, defaultValue = "0") int shiftWeeks,
                                       HttpServletRequest request)  {
        scheduleService.createSchedule(doctorId, patientId, localDateTime);
        User user = userService.getUserByUsername(request.getRemoteUser());
        List<ScheduleHourCollection> schedulesHourCollection = scheduleService.createWeekSchedule(doctorId, patientId, null, shiftWeeks);
        ModelAndView modelAndView = new ModelAndView("schedule");
        modelAndView.addObject( "schedulesHourCollection", schedulesHourCollection);
        modelAndView.addObject("userId", patientId);
        modelAndView.addObject("docId", doctorId);
        modelAndView.addObject("role", user.getType());
        return modelAndView;
    }

    @GetMapping(value = "/doctor/{dId}/schedule/{pId}/{sId}")
    public ModelAndView deleteSchedule(@PathVariable("dId") int doctorId, @PathVariable("pId") int patientId,
                                       @PathVariable("sId") int scheduleId,
                                       @RequestParam(name = "shiftWeeks", required = false, defaultValue = "0") int shiftWeeks,
                                       HttpServletRequest request)  {
        User user = userService.getUserByUsername(request.getRemoteUser());
        scheduleService.deleteSchedule(scheduleId, user.getId());
        ModelAndView modelAndView = new ModelAndView("schedule");
        List<ScheduleHourCollection> schedulesHourCollection = scheduleService.createWeekSchedule(doctorId, patientId, null, shiftWeeks);
        modelAndView.addObject( "schedulesHourCollection", schedulesHourCollection);
        modelAndView.addObject("userId", patientId);
        modelAndView.addObject("docId", doctorId);
        modelAndView.addObject("role", user.getType());
        return modelAndView;
    }
}

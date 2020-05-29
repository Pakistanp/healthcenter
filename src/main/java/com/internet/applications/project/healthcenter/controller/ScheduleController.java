package com.internet.applications.project.healthcenter.controller;

import com.internet.applications.project.healthcenter.model.Schedule;
import com.internet.applications.project.healthcenter.model.ScheduleHour;
import com.internet.applications.project.healthcenter.model.ScheduleHourCollection;
import com.internet.applications.project.healthcenter.service.ScheduleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleController {

    @NonNull
    private final ScheduleService scheduleService;

    @GetMapping(value = "/doctor/{dId}/schedule/{pId}")
    public ModelAndView scheduleDoctorPage(@PathVariable("dId") int dId, @PathVariable("pId") int pId,
                                           @RequestParam(name = "action", required = false) String action, @RequestParam(name = "shiftWeeks", required = false, defaultValue = "0") int shiftWeeks)  {
        ModelAndView modelAndView = new ModelAndView("schedule");
        List<ScheduleHourCollection> schedulesHourCollection = scheduleService.createWeekSchedule(dId, pId, action, shiftWeeks);
        modelAndView.addObject( "schedulesHourCollection", schedulesHourCollection);
        modelAndView.addObject("userId", pId);
        modelAndView.addObject("docId", dId);
        return modelAndView;
    }

    @PostMapping(value = "/doctor/{dId}/schedule/{pId}")
    public ModelAndView createSchedule(@PathVariable("dId") int doctorId, @PathVariable("pId") int patientId,
                                       @RequestParam(name = "date", required = false) String localDateTime,
                                       @RequestParam(name = "shiftWeeks", required = false, defaultValue = "0") int shiftWeeks)  {
        scheduleService.createSchedule(doctorId, patientId, localDateTime);
        ModelAndView modelAndView = new ModelAndView("schedule");
        List<ScheduleHourCollection> schedulesHourCollection = scheduleService.createWeekSchedule(doctorId, patientId, null, shiftWeeks);
        modelAndView.addObject( "schedulesHourCollection", schedulesHourCollection);
        modelAndView.addObject("userId", patientId);
        modelAndView.addObject("docId", doctorId);
        return modelAndView;
    }

    @GetMapping(value = "/doctor/{dId}/schedule/{pId}/{sId}")
    public ModelAndView createSchedule(@PathVariable("dId") int doctorId, @PathVariable("pId") int patientId,
                                       @PathVariable("sId") int scheduleId,
                                       @RequestParam(name = "shiftWeeks", required = false, defaultValue = "0") int shiftWeeks)  {
        scheduleService.deleteSchedule(scheduleId);
        ModelAndView modelAndView = new ModelAndView("schedule");
        List<ScheduleHourCollection> schedulesHourCollection = scheduleService.createWeekSchedule(doctorId, patientId, null, shiftWeeks);
        modelAndView.addObject( "schedulesHourCollection", schedulesHourCollection);
        modelAndView.addObject("userId", patientId);
        modelAndView.addObject("docId", doctorId);
        return modelAndView;
    }
}

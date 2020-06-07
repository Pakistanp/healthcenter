package com.internet.applications.project.healthcenter.controller;

import com.internet.applications.project.healthcenter.model.Schedule;
import com.internet.applications.project.healthcenter.model.VisitDetails;
import com.internet.applications.project.healthcenter.service.UserService;
import com.internet.applications.project.healthcenter.service.VisitService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitController {

    @NonNull
    private final VisitService visitService;

    @NonNull
    private final UserService userService;

    @GetMapping("/visits")
    public ModelAndView homePage(HttpServletRequest request) {
        int userId = userService.getUserByUsername(request.getRemoteUser()).getId();
        List<VisitDetails> myVisits = visitService.getVisitsByUserId(userId);
        ModelAndView modelAndView = new ModelAndView("visit");
        modelAndView.addObject("mode", "MODE_SHOW_VISITS");
        modelAndView.addObject("visits", myVisits);
        return modelAndView;
    }
    @GetMapping("/visit/{visitId}")
    public ModelAndView visitPage(@PathVariable("visitId") int visitId, @RequestParam(value = "schedule") int scheduleId) {
        ModelAndView modelAndView = new ModelAndView("visit");
        modelAndView.addObject("visitId", visitId);
        modelAndView.addObject("scheduleId", scheduleId);
        modelAndView.addObject("mode", "MODE_VISIT");
        return modelAndView;
    }

    @PostMapping("/visit/{visitId}")
    public String saveVisit(@RequestParam("file") MultipartFile file, @RequestParam(value = "schedule") int scheduleId ,
                            @RequestParam("diagnosis") String diagnosis, @PathVariable("visitId") int visitId) {
        visitService.createOrUpdateVisit(visitId, scheduleId, diagnosis, file);
        ModelAndView modelAndView = new ModelAndView("visit");

        return "redirect:/";
    }

    @GetMapping("/visit")
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestParam("id") int visitId) throws IOException {
        return visitService.openFile(visitId);
    }
}

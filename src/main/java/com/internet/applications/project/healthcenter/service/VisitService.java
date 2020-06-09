package com.internet.applications.project.healthcenter.service;

import com.internet.applications.project.healthcenter.dao.ScheduleDAO;
import com.internet.applications.project.healthcenter.dao.UserDAO;
import com.internet.applications.project.healthcenter.dao.VisitDAO;
import com.internet.applications.project.healthcenter.model.Schedule;
import com.internet.applications.project.healthcenter.model.User;
import com.internet.applications.project.healthcenter.model.Visit;
import com.internet.applications.project.healthcenter.model.VisitDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitService {

    private final String UPLOADED_FOLDER = "./src/main/resources/files/";
    private final String OPEN_FOLDER = "/files/";

    @NonNull
    private final VisitDAO visitDAO;
    @NonNull
    private final ScheduleDAO scheduleDAO;
    @NonNull
    private final UserDAO userDAO;

    public void createOrUpdateVisit(int visitId, int scheduleId, String diagnosis, MultipartFile file) {
        Schedule schedule = scheduleDAO.findById(scheduleId) != null ? scheduleDAO.findById(scheduleId).get(0) : null;
        Visit newVisit = new Visit(visitId != 0 ? visitId : 1, diagnosis, file != null ? file.getOriginalFilename() : "", scheduleId);

        if (visitId != 0) {
            Visit prevVisit = visitDAO.findById(visitId) != null ? visitDAO.findById(visitId).get(0) : null;
            deleteFileFromPath(prevVisit.getFilePath());
        }
        if (!newVisit.getFilePath().isEmpty()) {
            storeFile(file);
        }

        if (visitId != 0) {
            visitDAO.updateVisit(newVisit);
        }
        else {
            visitDAO.createVisit(newVisit);
        }
    }

    private void storeFile(MultipartFile file) {
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFileFromPath(String filePath) {
        // Get the file and delete
        Path path = Paths.get(UPLOADED_FOLDER + filePath);
        File file = path.toFile();
        file.delete();
    }

    public ResponseEntity<InputStreamResource> openFile(int visitId) throws IOException {
        Visit visit = visitDAO.findById(visitId).get(0);
        ClassPathResource pdfFile = new ClassPathResource(OPEN_FOLDER + visit.getFilePath());
        return ResponseEntity
                .ok()
                .contentLength(pdfFile.contentLength())
                .contentType(
                        MediaType.parseMediaType("application/pdf"))
                .body(new InputStreamResource(pdfFile.getInputStream()));
    }

    public List<VisitDetails> getVisitsByUserId(int userId) {
        List<Schedule> scheduleList = new ArrayList<>();
        List<VisitDetails> visitDetailsList = new ArrayList<>();
        User user = userDAO.getByUserId(userId);
        String userRole = user.getType();
        switch (userRole) {
            case "user" : {
                scheduleList = scheduleDAO.findByPatientId(userId);
                break;
            }
            case "doc" : {
                scheduleList = scheduleDAO.findByDoctorId(userId);
                break;
            }
        }
        for (Schedule schedule : scheduleList) {
            if ("user".equals(userRole)) {
                user = userDAO.getByUserId(schedule.getDoctorId());
            }
            else if ("doc".equals(userRole)) {
                user = userDAO.getByUserId(schedule.getPatientId());
            }
            String name = user.getFirstName() + " " + user.getLastName();
            List<Visit> visitList = getVisitByScheduleId(schedule.getId());
            Visit visit = visitList != null && visitList.size() > 0 ? visitList.get(0) : null;
            visitDetailsList.add(new VisitDetails(name ,schedule.getStartDate().toLocalDate().toString() + " " + schedule.getStartDate().toLocalTime().toString(),
                    schedule.getId(), visit != null && visit.getId() > 0 ? visit.getId() : 0));
        }
        return visitDetailsList;
    }

    public Visit getVisitById(int id) {
        List<Visit> visits = visitDAO.findById(id);
        return visits != null && visits.size() > 0 ? visits.get(0) : null;
    }

    public List<Visit> getVisitByScheduleId(int id) {
        return visitDAO.findByScheduleId(id);
    }

    public HashMap<Integer, Visit> getVisitsAllVisits() {
        HashMap<Integer, Visit> allVisits = new HashMap<>();
        List<Visit> visits = visitDAO.getAllVisits();
        for (Visit visit : visits) {
            allVisits.put(visit.getScheduleId(),visit);
        }
        return allVisits;
    }
}

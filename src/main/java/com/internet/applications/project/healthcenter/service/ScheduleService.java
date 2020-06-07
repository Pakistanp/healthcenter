package com.internet.applications.project.healthcenter.service;

import com.internet.applications.project.healthcenter.dao.ScheduleDAO;
import com.internet.applications.project.healthcenter.dao.UserDAO;
import com.internet.applications.project.healthcenter.model.Schedule;
import com.internet.applications.project.healthcenter.model.ScheduleHour;
import com.internet.applications.project.healthcenter.model.ScheduleHourCollection;
import com.internet.applications.project.healthcenter.model.User;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleService {

    public static final String SERVICE_PHONE_NUMBER = "+14079102283";
    public static String ACCOUNT_SID;
    public static String AUTH_TOKEN;

    @NonNull
    private final ScheduleDAO scheduleDAO;
    @NonNull
    private final UserDAO userDAO;

    public List<Schedule> findByDoctorId(int id) {
        return scheduleDAO.findByDoctorId(id);
    }
    private List<Schedule> findWeekByDoctorIdAndDate(int id, LocalDateTime now) {
        int day = now.getDayOfWeek().getValue();
        now = now.minusDays(day - 1);
        return scheduleDAO.findWeekByDoctorIdAndDate(id, now, now.plusDays(6));
    }

    public List<ScheduleHourCollection> createWeekSchedule(int did, int pid, String action, int shiftWeeks) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (action != null) {
            if ("prev".equals(action)) {
                shiftWeeks--;
            }
            if ("next".equals(action)) {
                shiftWeeks++;
            }
        }
        localDateTime = localDateTime.plusWeeks(shiftWeeks);

        List<Schedule> schedulesWeek = findWeekByDoctorIdAndDate(did, localDateTime);
        List<ScheduleHourCollection> scheduleHourListCollection = new ArrayList<>();
        for (LocalTime localTime = LocalTime.of(8, 00); localTime.isBefore(LocalTime.of(15, 59)); localTime = localTime.plusMinutes(30)) {
            List<ScheduleHour> scheduleHourList = new ArrayList<>();
            for (int dayOfWeek = 1 ; dayOfWeek <= 7 ; dayOfWeek++) {
                Schedule schedule = getScheduleByDayOfWeekAndTime(schedulesWeek, dayOfWeek, localTime);
                if (schedule == null) {
                    schedule = new Schedule(-1, LocalDateTime.of(localDateTime.minusDays(localDateTime.getDayOfWeek().getValue() - 1)
                            .plusDays(dayOfWeek - 1).toLocalDate(), localTime), did, pid);
                }
                ScheduleHour scheduleHour = new ScheduleHour(localTime.toString() + " - " + localTime.plusMinutes(30).toString() , dayOfWeek, schedule);
                scheduleHourList.add(scheduleHour);
            }
            scheduleHourListCollection.add(new ScheduleHourCollection(shiftWeeks, scheduleHourList));
        }
        return scheduleHourListCollection;
    }

    private Schedule getScheduleByDayOfWeekAndTime(List<Schedule> scheduleList, int dayOfWeek, LocalTime localTime){
        Schedule newSchedule = null;
        for (Schedule schedule:scheduleList) {
            if (schedule.getStartDate().getDayOfWeek().getValue() == dayOfWeek
                    && localTime.equals(LocalTime.of(schedule.getStartDate().getHour(), schedule.getStartDate().getMinute()))) {
                newSchedule = schedule;
                break;
            }
        }
        return newSchedule;
    }

    public void createSchedule(int doctorId, int patientId, String localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(localDateTime, formatter);
        scheduleDAO.createSchedule(new Schedule(1, dateTime, doctorId, patientId));
    }

    public void deleteSchedule(int scheduleId, int userId) {
        User user = userDAO.getByUserId(userId);
        String userRole = user.getType();
        if ("doc".equals(userRole)) {
            sendSMSNofification(scheduleId, user);
        }
        scheduleDAO.deleteSchedule(scheduleId);
    }

    private void sendSMSNofification(int scheduleId, User doctor) {
        StringBuilder stringBuilder = new StringBuilder("Wizyta która miała odbyć się ");
        Schedule schedule = scheduleDAO.findById(scheduleId).get(0);
        User patient = userDAO.getByUserId(schedule.getPatientId());

        stringBuilder.append(schedule.getStartDate().toLocalDate().toString())
                .append(" o godzinie ")
                .append(schedule.getStartDate().toLocalTime().toString())
                .append(" została odwołana przez doktora ")
                .append(doctor.getFirstName() + " ")
                .append(doctor.getLastName() + ".")
                .append(" Zaloguj się na HealthCenter w celu zarejestrowania się na kolejną wizyte.");

        getSMSServiceInformation();
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                //new PhoneNumber("+48602277802"),
                new PhoneNumber("+48" + patient.getPhoneNumber()),
                new PhoneNumber(SERVICE_PHONE_NUMBER),
                stringBuilder.toString()).create();
    }

    //this is temp solution because I do not want commit auth data
    private void getSMSServiceInformation() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("./SecFile"));
            ACCOUNT_SID = reader.readLine();
            AUTH_TOKEN = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

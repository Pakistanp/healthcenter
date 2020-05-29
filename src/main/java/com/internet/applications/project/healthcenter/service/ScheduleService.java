package com.internet.applications.project.healthcenter.service;

import com.internet.applications.project.healthcenter.dao.ScheduleDAO;
import com.internet.applications.project.healthcenter.model.Schedule;
import com.internet.applications.project.healthcenter.model.ScheduleHour;
import com.internet.applications.project.healthcenter.model.ScheduleHourCollection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleService {
    @NonNull
    private final ScheduleDAO scheduleDAO;

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

    public void deleteSchedule(int scheduleId) {
        scheduleDAO.deleteSchedule(scheduleId);
    }
}

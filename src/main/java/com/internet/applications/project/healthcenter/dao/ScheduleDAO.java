package com.internet.applications.project.healthcenter.dao;

import com.internet.applications.project.healthcenter.model.Schedule;
import lombok.NonNull;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateCrud;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ScheduleDAO {

    @NonNull
    private final RowMapper<Schedule> scheduleRowMapper = JdbcTemplateMapperFactory.newInstance().newRowMapper(Schedule.class);

    @NonNull
    private final JdbcTemplate jdbcTemplate;

    @NonNull
    private final JdbcTemplateCrud<Schedule, Integer> scheduleCrud;

    @Autowired
    public ScheduleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        scheduleCrud = JdbcTemplateMapperFactory
                .newInstance()
                .crud(Schedule.class, Integer.class).to(jdbcTemplate,"schedule");
    }

    public List<Schedule> findByDoctorId(int id) {
        return jdbcTemplate.query(
                "SELECT id, startdate, doctor_id, patient_id " +
                        "FROM schedule " +
                        "WHERE doctor_id = ? "
                ,
                preparedStatement -> preparedStatement.setInt(1, id),
                scheduleRowMapper);
    }

    public List<Schedule> findById(int id) {
        return jdbcTemplate.query(
                "SELECT id, startdate, doctor_id, patient_id " +
                        "FROM schedule " +
                        "WHERE id = ? "
                ,
                preparedStatement -> preparedStatement.setInt(1, id),
                scheduleRowMapper);
    }

    public void createSchedule(Schedule schedule) {
        scheduleCrud.create(schedule);
    }

    public List<Schedule> findWeekByDoctorIdAndDate(int id, LocalDateTime now, LocalDateTime plusDays) {
        return jdbcTemplate.query(
                "SELECT id, startdate, doctor_id, patient_id " +
                        "FROM schedule " +
                        "WHERE doctor_id = ? AND startdate BETWEEN ? AND ?"
                , new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setInt(1, id);
                        ps.setString(2, now.toLocalDate().toString());
                        ps.setString(3, plusDays.toLocalDate().toString());
                    }
                },
                scheduleRowMapper);
    }

    public void deleteSchedule(int scheduleId) {
        scheduleCrud.delete(scheduleId);
    }

    public List<Schedule> findByPatientId(int id) {
        return jdbcTemplate.query(
                "SELECT schedule.* " +
                        "FROM schedule, visits " +
                        "WHERE schedule.id = visits.schedule_id " +
                        "AND schedule.patient_id = ? "
                ,
                preparedStatement -> preparedStatement.setInt(1, id),
                scheduleRowMapper);
    }
}

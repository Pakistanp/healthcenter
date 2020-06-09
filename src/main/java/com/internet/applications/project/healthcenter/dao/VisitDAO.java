package com.internet.applications.project.healthcenter.dao;

import com.internet.applications.project.healthcenter.model.Visit;
import lombok.NonNull;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateCrud;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VisitDAO {

    @NonNull
    private final RowMapper<Visit> visitRowMapper = JdbcTemplateMapperFactory.newInstance().newRowMapper(Visit.class);

    @NonNull
    private final JdbcTemplate jdbcTemplate;

    @NonNull
    private final JdbcTemplateCrud<Visit, Integer> visitCrud;

    @Autowired
    public VisitDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        visitCrud = JdbcTemplateMapperFactory
                .newInstance()
                .crud(Visit.class, Integer.class).to(jdbcTemplate,"visits");
    }

    public void createVisit(Visit visit) {
        visitCrud.create(visit);
    }

    public void updateVisit(Visit visit) { visitCrud.update(visit); }

    public List<Visit> findById(int id) {
        return jdbcTemplate.query(
                "SELECT id, diagnosis, file_path, schedule_id " +
                        "FROM visits " +
                        "WHERE id = ? "
                ,
                preparedStatement -> preparedStatement.setInt(1, id),
                visitRowMapper);
    }

    public List<Visit> findByScheduleId(int id) {
        return jdbcTemplate.query(
                "SELECT id, diagnosis, file_path, schedule_id " +
                        "FROM visits " +
                        "WHERE schedule_id = ? "
                ,
                preparedStatement -> preparedStatement.setInt(1, id),
                visitRowMapper);
    }

    public List<Visit> getAllVisits() {
        return jdbcTemplate.query(
                "SELECT id, diagnosis, file_path, schedule_id " +
                        "FROM visits "
                ,
                visitRowMapper);
    }
}

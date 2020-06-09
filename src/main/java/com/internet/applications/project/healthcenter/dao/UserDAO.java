package com.internet.applications.project.healthcenter.dao;

import com.internet.applications.project.healthcenter.model.User;
import lombok.NonNull;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateCrud;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO  {

    @NonNull
    private final RowMapper<User> userRowMapper = JdbcTemplateMapperFactory.newInstance().newRowMapper(User.class);

    @NonNull
    private final JdbcTemplate jdbcTemplate;

    @NonNull
    private final JdbcTemplateCrud<User, Integer> userCrud;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userCrud = JdbcTemplateMapperFactory
                .newInstance()
                .crud(User.class, Integer.class).to(jdbcTemplate,"users");
    }
    public void createUser(User user) {
        userCrud.create(user);
    }

    public List<User> getAllDoctors() {
        return jdbcTemplate.query(
                "SELECT id, fname, lname, pesel, email, phonenumber, password, type " +
                        "FROM users " +
                        "WHERE type = ? "
                ,
                preparedStatement -> preparedStatement.setString(1, "doc"),
                userRowMapper);
    }

    public List<User> getAllPatients() {
        return jdbcTemplate.query(
                "SELECT id, fname, lname, pesel, email, phonenumber, password, type " +
                        "FROM users " +
                        "WHERE type = ? "
                ,
                preparedStatement -> preparedStatement.setString(1, "user"),
                userRowMapper);
    }

    public User getUserWithEmail(String pesel) {
        List<User> users = jdbcTemplate.query(
                "SELECT id, fname, lname, pesel, email, phonenumber, password, type " +
                        "FROM users " +
                        "WHERE Email = ? "
                ,
                preparedStatement -> preparedStatement.setString(1, pesel),
                userRowMapper);
        return users.get(0);
    }

    public User getByUserId(int id) {
        return jdbcTemplate.query(
                "SELECT id, fname, lname, pesel, email, phonenumber, password, type " +
                        "FROM users " +
                        "WHERE id = ? "
                ,
                preparedStatement -> preparedStatement.setInt(1, id),
                userRowMapper).get(0);
    }
}

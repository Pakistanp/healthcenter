package com.internet.applications.project.healthcenter.dao;

import com.internet.applications.project.healthcenter.model.User;
import lombok.NonNull;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateCrud;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO  {

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
}

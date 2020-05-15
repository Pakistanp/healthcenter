package com.internet.applications.project.healthcenter.service;


import com.internet.applications.project.healthcenter.dao.UserDAO;
import com.internet.applications.project.healthcenter.model.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Converter;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    @NonNull
    private final UserDAO userDAO;
    //@NonNull
   // private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void addNewUser(String firstName, String lastName, String pesel, String phonenumber, String email, String password) {
        User user = User.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .pesel(Long.parseLong(pesel))
                .phoneNumber(Integer.parseInt(phonenumber))
                .email(email)
                //.password(passwordEncoder.encode(password))
                .password(password)
                .type("user")
                .build();
        userDAO.createUser(user);
    }
}

package com.internet.applications.project.healthcenter.model;

import lombok.*;
import org.simpleflatmapper.map.annotation.Column;
import org.simpleflatmapper.map.annotation.Key;

import javax.validation.constraints.Positive;

@Data
@Builder
public class User {

    @Key
    @Positive
    @Setter(AccessLevel.NONE)
    private final int id;

    @NonNull
    @Setter(AccessLevel.NONE)
    @Column("fname")
    private final String firstName;

    @NonNull
    @Setter(AccessLevel.NONE)
    @Column("lname")
    private final String lastName;

    @Setter(AccessLevel.NONE)
    private final long pesel;

    @NonNull
    @Setter(AccessLevel.NONE)
    private final String email;

    @Setter(AccessLevel.NONE)
    @Column("phonenumber")
    private final int phoneNumber;

    @NonNull
    @Setter(AccessLevel.NONE)
    private final String password;

    @NonNull
    @Setter(AccessLevel.NONE)
    private final String type;
}

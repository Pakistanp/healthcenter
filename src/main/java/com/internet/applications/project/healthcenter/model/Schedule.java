package com.internet.applications.project.healthcenter.model;

import lombok.*;
import org.simpleflatmapper.map.annotation.Column;
import org.simpleflatmapper.map.annotation.Key;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Schedule {

    @Key
    @Positive
    @Setter(AccessLevel.NONE)
    private final int id;

    @NonNull
    @Setter(AccessLevel.NONE)
    @Column("startdate")
    private final LocalDateTime startDate;

    @Setter(AccessLevel.NONE)
    @Column("doctor_id")
    private final int doctorId;

    @Setter(AccessLevel.NONE)
    @Column("patient_id")
    private final int patientId;
}

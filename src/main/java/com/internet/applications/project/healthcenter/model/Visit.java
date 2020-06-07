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
public class Visit {

    @Key
    @Positive
    @Setter(AccessLevel.NONE)
    private final int id;

    @Setter(AccessLevel.NONE)
    private final String diagnosis;

    @Setter(AccessLevel.NONE)
    @Column("file_path")
    private final String filePath;

    @Setter(AccessLevel.NONE)
    @Column("schedule_id")
    private final int scheduleId;


}

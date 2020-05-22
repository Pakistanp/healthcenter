package com.internet.applications.project.healthcenter.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleHour {

    @Setter(AccessLevel.NONE)
    private final String time;

    @Setter(AccessLevel.NONE)
    private final int dayOfWeek;

    @Setter(AccessLevel.NONE)
    private final Schedule schedule;

}

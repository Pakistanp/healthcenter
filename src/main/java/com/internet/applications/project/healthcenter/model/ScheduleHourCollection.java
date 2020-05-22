package com.internet.applications.project.healthcenter.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleHourCollection {
    @Setter(AccessLevel.NONE)
    private final List<ScheduleHour> schedules;
}

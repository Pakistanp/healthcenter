package com.internet.applications.project.healthcenter.model;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitDetails {

    @Setter(AccessLevel.NONE)
    private final String name;

    @Setter(AccessLevel.NONE)
    private final String time;

    @Setter(AccessLevel.NONE)
    private final int scheduleId;

    @Setter(AccessLevel.NONE)
    private final int visitId;
}

package dev.timetable.spring.dto.schoolday;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RetrieveSchoolDaysInput {
    UUID ttid;
    Long id;
}

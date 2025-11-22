package dev.timetable.spring.dto.instructor;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 教員同期APIのInputDTO
 */
@Value
@Builder
@Jacksonized
public class SyncInstructorsInput {
    UUID ttid;
    List<UpdateInstructorInput> instructors;
    String updatedBy;
}

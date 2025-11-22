package dev.timetable.spring.dto.course;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 講座取得 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveCoursesInput {

    UUID ttid;
    Long subjectId;
    Long id;
}


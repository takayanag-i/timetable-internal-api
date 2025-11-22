package dev.timetable.spring.dto.grade;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 学年取得 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveGradesInput {

    Long id;
    UUID ttid;
}


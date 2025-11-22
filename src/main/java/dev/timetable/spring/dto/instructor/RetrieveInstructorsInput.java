package dev.timetable.spring.dto.instructor;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 教員取得 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveInstructorsInput {
    
    UUID ttid;
    Long id;
}
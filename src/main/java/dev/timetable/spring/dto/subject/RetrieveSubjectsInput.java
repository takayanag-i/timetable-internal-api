package dev.timetable.spring.dto.subject;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 科目取得 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveSubjectsInput {
    
    UUID ttid;
    Long id;
}
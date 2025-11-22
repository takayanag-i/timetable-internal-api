package dev.timetable.spring.dto.subject;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 科目同期APIのInputDTO
 */
@Value
@Builder
@Jacksonized
public class SyncSubjectsInput {
    UUID ttid;
    List<UpdateSubjectInput> subjects;
    String updatedBy;
}
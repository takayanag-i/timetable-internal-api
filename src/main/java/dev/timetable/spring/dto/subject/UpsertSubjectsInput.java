package dev.timetable.spring.dto.subject;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 科目作成更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertSubjectsInput {
    UUID ttid;
    List<SubjectInput> subjects;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class SubjectInput {
        Long id;
        String disciplineCode;
        String subjectName;
        Short credits;
        Long gradeId;
    }
}
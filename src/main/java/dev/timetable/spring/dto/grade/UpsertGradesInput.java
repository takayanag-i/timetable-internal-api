package dev.timetable.spring.dto.grade;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 学年作成更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertGradesInput {

    UUID ttid;
    List<UpdateGradeInput> grades;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class UpdateGradeInput {
        Long id;
        String gradeName;
    }
}


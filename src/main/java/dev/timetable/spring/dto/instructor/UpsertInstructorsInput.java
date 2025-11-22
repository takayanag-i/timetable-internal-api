package dev.timetable.spring.dto.instructor;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 教員作成更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertInstructorsInput {
    UUID ttid;
    List<InstructorInput> instructors;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class InstructorInput {
        Long id;
        String instructorName;
        String disciplineCode;
        List<AttendanceDayInput> attendanceDays;

        @Value
        @Builder
        @Jacksonized
        public static class AttendanceDayInput {
            Long id;
            String dayOfWeek;
            List<Short> unavailablePeriods;
        }
    }
}
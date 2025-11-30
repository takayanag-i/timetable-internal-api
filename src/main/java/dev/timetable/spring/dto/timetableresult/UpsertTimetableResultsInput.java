package dev.timetable.spring.dto.timetableresult;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 時間割編成結果作成更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertTimetableResultsInput {

    UUID ttid;
    List<TimetableResultInput> timetableResults;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class TimetableResultInput {
        Long id;
        List<TimetableEntryInput> timetableEntries;
        List<ConstraintViolationInput> constraintViolations;

        @Value
        @Builder
        @Jacksonized
        public static class TimetableEntryInput {
            Long id;
            Long homeroomId;
            String dayOfWeek;
            Integer period;
            Long courseId;
        }

        @Value
        @Builder
        @Jacksonized
        public static class ConstraintViolationInput {
            Long id;
            String constraintViolationCode;
            Object violatingKeys;
        }
    }
}

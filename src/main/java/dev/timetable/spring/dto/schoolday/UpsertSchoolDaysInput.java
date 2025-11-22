package dev.timetable.spring.dto.schoolday;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 学校曜日作成更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertSchoolDaysInput {
    UUID ttid;
    List<SchoolDayInput> schoolDays;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class SchoolDayInput {
        Long schoolDayId;
        String dayOfWeek;
        Boolean isAvailable;
        Short amPeriods;
        Short pmPeriods;
    }
}
package dev.timetable.spring.dto.instructor;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 教員-更新APIのリクエストDTO
 * IDがnullの場合は新規作成、IDがある場合は更新として扱う
 */
@Value
@Builder
@Jacksonized
public class UpdateInstructorInput {
    Long id; // nullの場合は新規作成
    String instructorName;
    String disciplineCode;
    List<UpdateAttendanceDayInput> attendanceDays;

    @Value
    @Builder
    @Jacksonized
    public static class UpdateAttendanceDayInput {
        Long id; // nullの場合は新規作成
        String dayOfWeek;
        List<Short> unavailablePeriods;
    }
}

package dev.timetable.spring.dto.timetableentry;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 時間割エントリ作成更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertTimetableEntriesInput {

    Long timetableResultId;
    List<TimetableEntryInput> timetableEntries;
    String by;

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
}

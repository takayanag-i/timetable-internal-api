package dev.timetable.spring.dto.homeroom;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 学級登録更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertHomeroomsInput {
    UUID ttid;
    List<HomeroomInput> homerooms;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class HomeroomInput {
        Long id;
        String homeroomName;
        Long gradeId;
        List<HomeroomDayInput> homeroomDays;

        @Value
        @Builder
        @Jacksonized
        public static class HomeroomDayInput {
            Long id;
            String dayOfWeek;
            Short periods;
        }
    }
}

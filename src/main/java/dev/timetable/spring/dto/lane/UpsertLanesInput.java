package dev.timetable.spring.dto.lane;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * レーン作成更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertLanesInput {

    List<LaneInput> lanes;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class LaneInput {
        Long id;
        List<Long> courseIds;
    }
}

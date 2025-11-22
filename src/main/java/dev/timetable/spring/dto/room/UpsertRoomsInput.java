package dev.timetable.spring.dto.room;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 教室作成更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertRoomsInput {
    UUID ttid;
    List<RoomInput> rooms;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class RoomInput {
        Long id;
        String roomName;
    }
}
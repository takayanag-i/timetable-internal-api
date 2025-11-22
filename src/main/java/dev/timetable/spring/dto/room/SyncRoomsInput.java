package dev.timetable.spring.dto.room;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 教室同期APIのInputDTO
 */
@Value
@Builder
@Jacksonized
public class SyncRoomsInput {
    UUID ttid;
    List<UpdateRoomInput> rooms;
    String updatedBy;
}

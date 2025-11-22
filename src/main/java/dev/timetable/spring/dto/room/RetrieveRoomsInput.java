package dev.timetable.spring.dto.room;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 教室取得 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveRoomsInput {
    
    UUID ttid;
    Long id;
}
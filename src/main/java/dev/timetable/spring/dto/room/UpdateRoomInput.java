package dev.timetable.spring.dto.room;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 教室更新入力DTO
 */
@Value
@Builder
@Jacksonized
public class UpdateRoomInput {
    /** ID（新規の場合はnull） */
    Long id;
    
    /** 教室名 */
    String roomName;
}

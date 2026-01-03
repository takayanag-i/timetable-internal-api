package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.timetable.spring.domain.entity.RoomEntity;
import dev.timetable.spring.dto.room.UpsertRoomsInput.RoomInput;

/**
 * 教室Entityのアップデータ.
 */
public class RoomUpdater {

    /**
     * 教室Entityを生成する.
     *
     * @param request 教室入力リクエスト
     * @param ttid 時間割ID
     * @param updatedBy 更新者
     * @param now 現在日時
     * @return 教室Entity
     */
    public static RoomEntity create(RoomInput request, UUID ttid, String updatedBy, OffsetDateTime now) {
        return RoomEntity.builder()
            .ttid(ttid)
            .roomName(request.getRoomName())
            .createdAt(now)
            .createdBy(updatedBy)
            .updatedAt(now)
            .updatedBy(updatedBy)
            .build();
    }

    /**
     * 教室Entityを更新する.
     *
     * @param entity 教室Entity
     * @param request 教室入力リクエスト
     * @param updatedBy 更新者
     * @param now 現在日時
     */
    public static void update(RoomEntity entity, RoomInput request, String updatedBy, OffsetDateTime now) {
        entity.setRoomName(request.getRoomName());
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
    }
}

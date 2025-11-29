package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.dto.timetableresult.UpsertTimetableResultsInput.TimetableResultInput;

/**
 * 時間割編成結果Updater.
 */
public class TimetableResultUpdater {

    private TimetableResultUpdater() {
    }

    /**
     * 時間割編成結果Entityを作成する.
     */
    public static TimetableResultEntity create(UUID ttid, TimetableResultInput input, String updatedBy, OffsetDateTime now) {
        return TimetableResultEntity.builder()
            .ttid(ttid)
            .createdAt(now)
            .createdBy(updatedBy)
            .updatedAt(now)
            .updatedBy(updatedBy)
            .build();
    }

    /**
     * 時間割編成結果Entityを更新する.
     */
    public static void update(TimetableResultEntity entity, TimetableResultInput input, String updatedBy, OffsetDateTime now) {
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
    }
}

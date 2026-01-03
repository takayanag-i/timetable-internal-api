package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.timetable.spring.domain.entity.GradeEntity;
import dev.timetable.spring.dto.grade.UpsertGradesInput.GradeInput;

/**
 * 学年Updater.
 */
public class GradeUpdater {

    private GradeUpdater() {
    }

    /**
     * 学年Entityを作成する.
     */
    public static GradeEntity create(UUID ttid, GradeInput input, String updatedBy, OffsetDateTime now) {
        return GradeEntity.builder()
            .ttid(ttid)
            .gradeName(input.getGradeName())
            .createdAt(now)
            .createdBy(updatedBy)
            .updatedAt(now)
            .updatedBy(updatedBy)
            .build();
    }

    /**
     * 学年Entityを更新する.
     */
    public static void update(GradeEntity entity, GradeInput input, String updatedBy, OffsetDateTime now) {
        entity.setGradeName(input.getGradeName());
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
    }
}


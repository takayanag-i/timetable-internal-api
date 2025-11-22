package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.timetable.spring.domain.entity.SchoolDayEntity;
import dev.timetable.spring.dto.schoolday.UpdateSchoolDayInput;
import dev.timetable.spring.dto.schoolday.UpsertSchoolDaysInput.SchoolDayInput;

public class SchoolDayUpdater {
    /**
     * 新規作成（同期用）.
     */
    public static SchoolDayEntity create(UpdateSchoolDayInput request, UUID ttid, String createdBy, OffsetDateTime now) {
        return SchoolDayEntity.builder()
            .ttid(ttid)
            .dayOfWeek(request.getDayOfWeek())
            .isAvailable(request.getIsAvailable())
            .amPeriods(request.getAmPeriods())
            .pmPeriods(request.getPmPeriods())
            .createdAt(now)
            .createdBy(createdBy)
            .updatedAt(now)
            .updatedBy(createdBy)
            .build();
    }

    /**
     * 更新（同期用）.
     */
    public static void update(SchoolDayEntity entity, UpdateSchoolDayInput request, String updatedBy, OffsetDateTime now) {
        entity.setDayOfWeek(request.getDayOfWeek());
        entity.setIsAvailable(request.getIsAvailable());
        entity.setAmPeriods(request.getAmPeriods());
        entity.setPmPeriods(request.getPmPeriods());
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
    }
    
    /**
     * 新規作成（upsert用）.
     */
    public static SchoolDayEntity create(SchoolDayInput request, UUID ttid, String createdBy, OffsetDateTime now) {
        return SchoolDayEntity.builder()
            .ttid(ttid)
            .dayOfWeek(request.getDayOfWeek())
            .isAvailable(request.getIsAvailable())
            .amPeriods(request.getAmPeriods())
            .pmPeriods(request.getPmPeriods())
            .createdAt(now)
            .createdBy(createdBy)
            .updatedAt(now)
            .updatedBy(createdBy)
            .build();
    }

    /**
     * 更新（upsert用）.
     */
    public static void update(SchoolDayEntity entity, SchoolDayInput request, String updatedBy, OffsetDateTime now) {
        entity.setDayOfWeek(request.getDayOfWeek());
        entity.setIsAvailable(request.getIsAvailable());
        entity.setAmPeriods(request.getAmPeriods());
        entity.setPmPeriods(request.getPmPeriods());
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
    }
}

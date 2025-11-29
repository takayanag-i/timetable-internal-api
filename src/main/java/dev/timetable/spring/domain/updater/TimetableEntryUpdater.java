package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;

import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.domain.entity.TimetableEntryEntity;
import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.dto.timetableentry.UpsertTimetableEntriesInput.TimetableEntryInput;

/**
 * 時間割エントリUpdater.
 */
public class TimetableEntryUpdater {

    private TimetableEntryUpdater() {
    }

    /**
     * 時間割エントリEntityを作成する.
     */
    public static TimetableEntryEntity create(
            TimetableResultEntity timetableResult,
            HomeroomEntity homeroom,
            CourseEntity course,
            TimetableEntryInput input,
            String updatedBy,
            OffsetDateTime now) {
        return TimetableEntryEntity.builder()
            .timetableResult(timetableResult)
            .homeroom(homeroom)
            .dayOfWeek(input.getDayOfWeek())
            .period(input.getPeriod().shortValue())
            .course(course)
            .createdAt(now)
            .createdBy(updatedBy)
            .updatedAt(now)
            .updatedBy(updatedBy)
            .build();
    }

    /**
     * 時間割エントリEntityを更新する.
     */
    public static void update(
            TimetableEntryEntity entity,
            HomeroomEntity homeroom,
            CourseEntity course,
            TimetableEntryInput input,
            String updatedBy,
            OffsetDateTime now) {
        entity.setHomeroom(homeroom);
        entity.setDayOfWeek(input.getDayOfWeek());
        entity.setPeriod(input.getPeriod().shortValue());
        entity.setCourse(course);
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
    }
}

package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;
import java.util.Map;

import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.LaneEntity;
import dev.timetable.spring.dto.lane.UpsertLanesInput.LaneInput;

/**
 * レーンEntityのアップデータ.
 */
public class LaneUpdater {

    public static void update(LaneEntity entity, LaneInput input, Map<Long, CourseEntity> courseMap, String updateBy, OffsetDateTime now) {
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updateBy);

        // 既存の講座リストをクリアして新しい講座リストを設定
        entity.getCourses().clear();
        entity.getCourses().addAll(
            input.getCourseIds().stream()
                .map(courseMap::get)
                .toList()
        );
    }
}

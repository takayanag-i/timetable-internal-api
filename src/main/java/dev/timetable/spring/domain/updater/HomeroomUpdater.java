package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.timetable.spring.domain.entity.GradeEntity;
import dev.timetable.spring.domain.entity.HomeroomDayEntity;
import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.dto.homeroom.UpsertHomeroomsInput.HomeroomInput;

public class HomeroomUpdater {
    
    public static HomeroomEntity create(HomeroomInput req, UUID ttid, GradeEntity grade, String updatedBy, OffsetDateTime now) {
        HomeroomEntity entity = HomeroomEntity.builder()
                .ttid(ttid)
                .homeroomName(req.getHomeroomName())
                .grade(grade)
                .createdAt(now)
                .createdBy(updatedBy)
                .updatedAt(now)
                .updatedBy(updatedBy)
                .build();
        
        if (req.getHomeroomDays() != null) {
            entity.getHomeroomDays().addAll(
                req.getHomeroomDays().stream()
                    .map(dayReq -> HomeroomDayEntity.builder()
                        .homeroom(entity)
                        .dayOfWeek(dayReq.getDayOfWeek())
                        .periods(dayReq.getPeriods())
                        .createdAt(now)
                        .createdBy(updatedBy)
                        .updatedAt(now)
                        .updatedBy(updatedBy)
                        .build())
                    .toList()
            );
        }
        return entity;
    }
    
    public static void update(HomeroomEntity entity, HomeroomInput req, GradeEntity grade, String updatedBy, OffsetDateTime now) {
        entity.setHomeroomName(req.getHomeroomName());
        entity.setGrade(grade);
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
        
        // 既存の学級曜日をクリアして新しいものを追加
        entity.getHomeroomDays().clear();
        
        if (req.getHomeroomDays() != null) {
            entity.getHomeroomDays().addAll(
                req.getHomeroomDays().stream()
                    .map(dayReq -> HomeroomDayEntity.builder()
                        .homeroom(entity)
                        .dayOfWeek(dayReq.getDayOfWeek())
                        .periods(dayReq.getPeriods())
                        .createdAt(now)
                        .createdBy(updatedBy)
                        .updatedAt(now)
                        .updatedBy(updatedBy)
                        .build())
                    .toList()
            );
        }
    }
}

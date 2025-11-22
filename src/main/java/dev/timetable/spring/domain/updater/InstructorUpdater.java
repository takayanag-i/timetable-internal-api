package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.timetable.spring.domain.entity.AttendanceDayEntity;
import dev.timetable.spring.domain.entity.InstructorEntity;
import dev.timetable.spring.dto.instructor.UpdateInstructorInput;
import dev.timetable.spring.dto.instructor.UpsertInstructorsInput.InstructorInput;

public class InstructorUpdater {
    public static InstructorEntity createFromSync(UpdateInstructorInput req, UUID ttid, String updatedBy, OffsetDateTime now) {
        InstructorEntity entity = InstructorEntity.builder()
                .ttid(ttid)
                .instructorName(req.getInstructorName())
                .disciplineCode(req.getDisciplineCode())
                .createdAt(now)
                .createdBy(updatedBy)
                .updatedAt(now)
                .updatedBy(updatedBy)
                .build();
        
        if (req.getAttendanceDays() != null) {
            entity.getAttendanceDays().addAll(
                req.getAttendanceDays().stream()
                    .map(dayReq -> AttendanceDayEntity.builder()
                        .instructor(entity)
                        .dayOfWeek(dayReq.getDayOfWeek())
                        .unavailablePeriods(dayReq.getUnavailablePeriods())
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
    
    public static void updateFromSync(InstructorEntity entity, UpdateInstructorInput req, String updatedBy, OffsetDateTime now) {
        entity.setInstructorName(req.getInstructorName());
        entity.setDisciplineCode(req.getDisciplineCode());
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
        
        // 既存の勤怠曜日をクリアして新しいものを追加
        entity.getAttendanceDays().clear();
        
        if (req.getAttendanceDays() != null) {
            entity.getAttendanceDays().addAll(
                req.getAttendanceDays().stream()
                    .map(dayReq -> AttendanceDayEntity.builder()
                        .instructor(entity)
                        .dayOfWeek(dayReq.getDayOfWeek())
                        .unavailablePeriods(dayReq.getUnavailablePeriods())
                        .createdAt(now)
                        .createdBy(updatedBy)
                        .updatedAt(now)
                        .updatedBy(updatedBy)
                        .build())
                    .toList()
            );
        }
    }
    
    /**
     * 新規作成（upsert用）.
     */
    public static InstructorEntity create(InstructorInput req, UUID ttid, String updatedBy, OffsetDateTime now) {
        InstructorEntity entity = InstructorEntity.builder()
                .ttid(ttid)
                .instructorName(req.getInstructorName())
                .disciplineCode(req.getDisciplineCode())
                .createdAt(now)
                .createdBy(updatedBy)
                .updatedAt(now)
                .updatedBy(updatedBy)
                .build();
        
        if (req.getAttendanceDays() != null) {
            entity.getAttendanceDays().addAll(
                req.getAttendanceDays().stream()
                    .map(dayReq -> AttendanceDayEntity.builder()
                        .instructor(entity)
                        .dayOfWeek(dayReq.getDayOfWeek())
                        .unavailablePeriods(dayReq.getUnavailablePeriods())
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
    
    /**
     * 更新（upsert用）.
     */
    public static void update(InstructorEntity entity, InstructorInput req, String updatedBy, OffsetDateTime now) {
        entity.setInstructorName(req.getInstructorName());
        entity.setDisciplineCode(req.getDisciplineCode());
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
        
        // 既存の勤怠曜日をクリアして新しいものを追加
        entity.getAttendanceDays().clear();
        
        if (req.getAttendanceDays() != null) {
            entity.getAttendanceDays().addAll(
                req.getAttendanceDays().stream()
                    .map(dayReq -> AttendanceDayEntity.builder()
                        .instructor(entity)
                        .dayOfWeek(dayReq.getDayOfWeek())
                        .unavailablePeriods(dayReq.getUnavailablePeriods())
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

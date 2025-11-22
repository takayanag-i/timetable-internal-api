package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;
import java.util.Map;

import dev.timetable.spring.domain.entity.CourseDetailEntity;
import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.InstructorEntity;
import dev.timetable.spring.domain.entity.RoomEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.dto.course.UpsertCoursesInput.CourseInput;

public class CourseUpdater {
    
    public static CourseEntity create(
        CourseInput input,
        Map<Long, SubjectEntity> subjectMap, 
        Map<Long, InstructorEntity> instructorMap, 
        Map<Long, RoomEntity> roomMap, 
        String updatedBy, OffsetDateTime now
    ) {    
        SubjectEntity subject = subjectMap.get(input.getSubjectId());
        CourseEntity entity = CourseEntity.builder()
                .subject(subject)
                .courseName(input.getCourseName())
                .createdAt(now)
                .createdBy(updatedBy)
                .updatedAt(now)
                .updatedBy(updatedBy)
                .build();
        
        if (input.getCourseDetails() != null) {
            entity.getCourseDetails().addAll(
                input.getCourseDetails().stream()
                    .map(detailReq -> {
                        InstructorEntity instructor = instructorMap.get(detailReq.getInstructorId());
                        RoomEntity room = detailReq.getRoomId() != null ? roomMap.get(detailReq.getRoomId()) : null;
                        
                        return CourseDetailEntity.builder()
                                .course(entity)
                                .instructor(instructor)
                                .room(room)
                                .createdAt(now)
                                .createdBy(updatedBy)
                                .updatedAt(now)
                                .updatedBy(updatedBy)
                                .build();
                    })
                    .toList()
            );
        }
        return entity;
    }
    
    public static void update(
        CourseEntity entity,
        CourseInput input, 
        Map<Long, InstructorEntity> instructorMap, 
        Map<Long, RoomEntity> roomMap, 
        String updatedBy, OffsetDateTime now
    ) {
        entity.setCourseName(input.getCourseName());
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
        
        // 既存の講座詳細をクリアして新しいものを追加
        entity.getCourseDetails().clear();
        
        if (input.getCourseDetails() != null) {
            entity.getCourseDetails().addAll(
                input.getCourseDetails().stream()
                    .map(detailReq -> {
                        InstructorEntity instructor = instructorMap.get(detailReq.getInstructorId());
                        RoomEntity room = detailReq.getRoomId() != null ? roomMap.get(detailReq.getRoomId()) : null;
                        
                        return CourseDetailEntity.builder()
                                .course(entity)
                                .instructor(instructor)
                                .room(room)
                                .createdAt(now)
                                .createdBy(updatedBy)
                                .updatedAt(now)
                                .updatedBy(updatedBy)
                                .build();
                    })
                    .toList()
            );
        }
    }
}

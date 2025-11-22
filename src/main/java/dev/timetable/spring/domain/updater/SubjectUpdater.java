package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.timetable.spring.domain.entity.DisciplineEntity;
import dev.timetable.spring.domain.entity.GradeEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.domain.entity.GradeEntity;
import dev.timetable.spring.dto.subject.UpdateSubjectInput;
import dev.timetable.spring.dto.subject.UpsertSubjectsInput.SubjectInput;

public class SubjectUpdater {
    
    /**
     * UpdateSubjectInputを使って既存のEntityを更新する
     */
    public static void update(SubjectEntity entity, UpdateSubjectInput req, DisciplineEntity discipline, GradeEntity grade, String updatedBy, OffsetDateTime now) {
        entity.setDiscipline(discipline);
        entity.setSubjectName(req.getSubjectName());
        entity.setCredits(req.getCredits());
        entity.setGrade(grade);
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
    }
    
    /**
     * UpdateSubjectInputから新しいEntityを作成する
     */
    public static SubjectEntity create(UpdateSubjectInput req, DisciplineEntity discipline, GradeEntity grade, UUID ttid, String updatedBy, OffsetDateTime now) {
        return SubjectEntity.builder()
                .ttid(ttid)
                .discipline(discipline)
                .subjectName(req.getSubjectName())
                .credits(req.getCredits())
                .grade(grade)
                .createdAt(now)
                .createdBy(updatedBy)
                .updatedAt(now)
                .updatedBy(updatedBy)
                .build();
    }
    
    /**
     * SubjectInputから新しいEntityを作成する（upsert用）
     */
    public static SubjectEntity create(SubjectInput req, DisciplineEntity discipline, GradeEntity grade, UUID ttid, String updatedBy, OffsetDateTime now) {
        return SubjectEntity.builder()
                .ttid(ttid)
                .discipline(discipline)
                .subjectName(req.getSubjectName())
                .credits(req.getCredits())
                .grade(grade)
                .createdAt(now)
                .createdBy(updatedBy)
                .updatedAt(now)
                .updatedBy(updatedBy)
                .build();
    }
    
    /**
     * SubjectInputを使って既存のEntityを更新する（upsert用）
     */
    public static void update(SubjectEntity entity, SubjectInput req, DisciplineEntity discipline, GradeEntity grade, String updatedBy, OffsetDateTime now) {
        entity.setDiscipline(discipline);
        entity.setSubjectName(req.getSubjectName());
        entity.setCredits(req.getCredits());
        entity.setGrade(grade);
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
    }
}
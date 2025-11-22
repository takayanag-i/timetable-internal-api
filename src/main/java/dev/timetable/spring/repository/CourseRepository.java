package dev.timetable.spring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.CourseEntity;

/**
 * 講座Repository.
 */
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    /**
     * 親の科目の時間割IDで検索する.
     * 
     * @param ttid 時間割ID
     * @return エンティティリスト
     */
    List<CourseEntity> findBySubject_Ttid(UUID ttid);

    /**
     * 親の科目IDで検索する.
     * 
     * @param subjectId 科目ID
     * @return エンティティリスト
     */
    List<CourseEntity> findBySubjectId(Long subjectId);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param subjectId 科目ID
     * @param id 講座ID
     * @return エンティティリスト
     */
    @Query("SELECT course FROM CourseEntity course "
         + "WHERE (:subjectId IS NULL OR course.subject.id = :subjectId) "
         + "AND (:id IS NULL OR course.id = :id)")
    List<CourseEntity> findByConditions(
        @Param("subjectId") Long subjectId,
        @Param("id") Long id
    );
}
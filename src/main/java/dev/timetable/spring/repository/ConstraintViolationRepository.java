package dev.timetable.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.ConstraintViolationEntity;

/**
 * 制約違反Repository.
 */
public interface ConstraintViolationRepository extends JpaRepository<ConstraintViolationEntity, Long> {

    /**
     * 時間割編成結果IDで検索する.
     *
     * @param timetableResultId 時間割編成結果ID
     * @return エンティティリスト
     */
    List<ConstraintViolationEntity> findByTimetableResultId(Long timetableResultId);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param timetableResultId 時間割編成結果ID
     * @param id 制約違反ID
     * @return エンティティリスト
     */
    @Query("SELECT cv FROM ConstraintViolationEntity cv "
         + "WHERE (:timetableResultId IS NULL OR cv.timetableResult.id = :timetableResultId) "
         + "AND (:id IS NULL OR cv.id = :id)")
    List<ConstraintViolationEntity> findByConditions(
        @Param("timetableResultId") Long timetableResultId,
        @Param("id") Long id
    );
}

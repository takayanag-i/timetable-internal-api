package dev.timetable.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.TimetableEntryEntity;

/**
 * 時間割エントリRepository.
 */
public interface TimetableEntryRepository extends JpaRepository<TimetableEntryEntity, Long> {

    /**
     * 時間割編成結果IDで検索する.
     *
     * @param timetableResultId 時間割編成結果ID
     * @return エンティティリスト
     */
    List<TimetableEntryEntity> findByTimetableResultId(Long timetableResultId);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param timetableResultId 時間割編成結果ID
     * @param id 時間割エントリID
     * @return エンティティリスト
     */
    @Query("SELECT te FROM TimetableEntryEntity te "
         + "WHERE (:timetableResultId IS NULL OR te.timetableResult.id = :timetableResultId) "
         + "AND (:id IS NULL OR te.id = :id)")
    List<TimetableEntryEntity> findByConditions(
        @Param("timetableResultId") Long timetableResultId,
        @Param("id") Long id
    );
}

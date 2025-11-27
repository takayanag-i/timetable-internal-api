package dev.timetable.spring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.TimetableResultEntity;

/**
 * 時間割編成結果Repository.
 */
public interface TimetableResultRepository extends JpaRepository<TimetableResultEntity, Long> {

    /**
     * 親の時間割IDで検索する.
     *
     * @param ttid 時間割ID
     * @return エンティティリスト
     */
    List<TimetableResultEntity> findByTtid(UUID ttid);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param ttid 時間割ID
     * @param id 時間割編成結果ID
     * @return エンティティリスト
     */
    @Query("SELECT tr FROM TimetableResultEntity tr "
         + "WHERE (:ttid IS NULL OR tr.ttid = :ttid) "
         + "AND (:id IS NULL OR tr.id = :id)")
    List<TimetableResultEntity> findByConditions(
        @Param("ttid") UUID ttid,
        @Param("id") Long id
    );
}

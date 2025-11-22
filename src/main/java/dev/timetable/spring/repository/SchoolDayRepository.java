package dev.timetable.spring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.SchoolDayEntity;

/**
 * 学校曜日Repository.
 */
public interface SchoolDayRepository extends JpaRepository<SchoolDayEntity, Long>{
    /**
     * 親の時間割IDで検索する.
     *
     * @param ttid 時間割ID
     * @return エンティティリスト
     */
    List<SchoolDayEntity> findByTtid(UUID ttid);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param ttid 時間割ID
     * @param id 学校曜日ID
     * @return エンティティリスト
     */
    @Query("SELECT schoolDay FROM SchoolDayEntity schoolDay WHERE "
         + "(:ttid IS NULL OR schoolDay.ttid = :ttid) AND "
         + "(:id IS NULL OR schoolDay.id = :id)")
    List<SchoolDayEntity> findByConditions(
        @Param("ttid") UUID ttid,
        @Param("id") Long id
    );
}

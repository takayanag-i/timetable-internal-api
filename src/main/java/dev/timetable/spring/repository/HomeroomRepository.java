package dev.timetable.spring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.HomeroomEntity;

/**
 * 学級Repository.
 */
public interface HomeroomRepository extends JpaRepository<HomeroomEntity, Long> {

    /**
     * 親の時間割IDで検索する.
     *
     * @param ttid 時間割ID
     * @return エンティティリスト
     */
    List<HomeroomEntity> findByTtid(UUID ttid);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param ttid 時間割ID
     * @param id 学級ID
     * @return エンティティリスト
     */
    @Query("SELECT homeroom FROM HomeroomEntity homeroom "
         + "WHERE(:ttid IS NULL OR homeroom.ttid = :ttid) "
         + "AND (:id IS NULL OR homeroom.id = :id)")
    List<HomeroomEntity> findByConditions(
        @Param("ttid") UUID ttid,
        @Param("id") Long id
    );
}

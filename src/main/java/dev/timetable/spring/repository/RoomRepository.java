package dev.timetable.spring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.RoomEntity;

/**
 * 教室Repository.
 */
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    /**
     * 親の時間割IDで検索する.
     *
     * @param ttid 時間割ID
     * @return エンティティリスト
     */
    List<RoomEntity> findByTtid(UUID ttid);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param ttid 時間割ID
     * @param id 教室ID
     * @return エンティティリスト
     */
    @Query("SELECT room FROM RoomEntity room "
         + "WHERE (:ttid IS NULL OR room.ttid = :ttid) "
         + "AND (:id IS NULL OR room.id = :id)")
    List<RoomEntity> findByConditions(
        @Param("ttid") UUID ttid,
        @Param("id") Long id
    );
}
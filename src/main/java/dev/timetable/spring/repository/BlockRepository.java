package dev.timetable.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.BlockEntity;

/**
 * ブロックRepository.
 */
public interface BlockRepository extends JpaRepository<BlockEntity, Long> {

    /**
     * 親の学級IDで検索する.
     * 
     * @param homeroomId 学級ID
     */
    List<BlockEntity> findByHomeroomId(Long homeroomId);

    /**
     * 取得API用のAND条件検索を行う.
     * 
     * @param homeroomId 学級ID
     * @param id ブロックID
     * @return エンティティリスト
     */
    @Query("SELECT block FROM BlockEntity block "
         + "WHERE (:homeroomId IS NULL OR block.homeroomId = :homeroomId) "
         + "AND (:id IS NULL OR block.id = :id)")
    List<BlockEntity> findByConditions(
        @Param("homeroomId") Long homeroomId,
        @Param("id") Long id
    );
}

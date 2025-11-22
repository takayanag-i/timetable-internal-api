package dev.timetable.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.LaneEntity;

public interface LaneRepository extends JpaRepository<LaneEntity, Long> {
    /**
     * 親のブロックIDで検索する.
     *
     * @param blockId ブロックID
     * @return エンティティリスト
     */
    List<LaneEntity> findByBlock_Id(Long blockId);
    
    /**
     * コースIDに紐づくレーンを検索する.
     *
     * @param courseId コースID
     * @return エンティティリスト
     */
    @Query("SELECT lane FROM LaneEntity lane JOIN lane.courses course WHERE course.id = :courseId")
    List<LaneEntity> findByCourseId(@Param("courseId") Long courseId);
    
    /**
     * 取得API用のAND条件検索を行う.
     * 
     * @param blockId ブロックID
     * @param id レーンID
     * @return エンティティリスト
     */
    @Query("SELECT lane FROM LaneEntity lane "
         + "WHERE (:blockId IS NULL OR lane.block.id = :blockId) "
         + "AND (:id IS NULL OR lane.id = :id)")
    List<LaneEntity> findByConditions(
        @Param("blockId") Long blockId,
        @Param("id") Long id
    );
}

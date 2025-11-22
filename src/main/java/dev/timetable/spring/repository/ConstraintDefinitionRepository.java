package dev.timetable.spring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.ConstraintDefinitionEntity;

/**
 * 制約定義Repository.
 */
public interface ConstraintDefinitionRepository extends JpaRepository<ConstraintDefinitionEntity, Long> {

    /**
     * 親の時間割IDで検索する.
     *
     * @param ttid 時間割ID
     * @return エンティティリスト
     */
    List<ConstraintDefinitionEntity> findByTtid(UUID ttid);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param ttid 時間割ID
     * @param id 制約定義ID
     * @return エンティティリスト
     */
    @Query("SELECT constraintDefinition FROM ConstraintDefinitionEntity constraintDefinition "
         + "WHERE (:ttid IS NULL OR constraintDefinition.ttid = :ttid) "
         + "AND (:id IS NULL OR constraintDefinition.id = :id)")
    List<ConstraintDefinitionEntity> findByConditions(
        @Param("ttid") UUID ttid,
        @Param("id") Long id
    );
}

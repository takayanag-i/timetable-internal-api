package dev.timetable.spring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.GradeEntity;

/**
 * 学年Repository.
 */
public interface GradeRepository extends JpaRepository<GradeEntity, Long> {

    /**
     * 親の時間割IDで検索する.
     *
     * @param ttid 時間割ID
     * @return エンティティリスト
     */
    List<GradeEntity> findByTtid(UUID ttid);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param ttid 時間割ID
     * @param id 学年ID
     * @return エンティティリスト
     */
    @Query("SELECT grade FROM GradeEntity grade "
         + "WHERE (:ttid IS NULL OR grade.ttid = :ttid) "
         + "AND (:id IS NULL OR grade.id = :id)")
    List<GradeEntity> findByConditions(
        @Param("ttid") UUID ttid,
        @Param("id") Long id
    );
}


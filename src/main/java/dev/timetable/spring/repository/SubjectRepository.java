package dev.timetable.spring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.SubjectEntity;

/**
 * 科目Repository.
 */
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    /**
     * 親の時間割IDで検索する.
     *
     * @param ttid 時間割ID
     * @return エンティティリスト
     */
    List<SubjectEntity> findByTtid(UUID ttid);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param ttid 時間割ID
     * @param id 科目ID
     * @return エンティティリスト
     */
    @Query("SELECT subject FROM SubjectEntity subject "
         + "WHERE (:ttid IS NULL OR subject.ttid = :ttid) "
         + "AND (:id IS NULL OR subject.id = :id)")
    List<SubjectEntity> findByConditions(
        @Param("ttid") UUID ttid,
        @Param("id") Long id
    );
}
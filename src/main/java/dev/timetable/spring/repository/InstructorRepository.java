package dev.timetable.spring.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.timetable.spring.domain.entity.InstructorEntity;

public interface InstructorRepository extends JpaRepository<InstructorEntity, Long> {

    /**
     * 親の時間割IDで検索する.
     *
     * @param ttid 時間割ID
     * @return エンティティリスト
     */
    List<InstructorEntity> findByTtid(UUID ttid);

    /**
     * 取得API用のAND条件検索を行う.
     *
     * @param ttid 時間割ID
     * @param id 教員ID
     * @return エンティティリスト
     */
    @Query("SELECT instructor FROM InstructorEntity instructor "
         + "WHERE (:ttid IS NULL OR instructor.ttid = :ttid) "
         + "AND (:id IS NULL OR instructor.id = :id)")
    List<InstructorEntity> findByConditions(
        @Param("ttid") UUID ttid,
        @Param("id") Long id
    );
}

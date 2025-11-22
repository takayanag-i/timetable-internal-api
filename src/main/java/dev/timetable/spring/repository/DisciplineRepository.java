package dev.timetable.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.timetable.spring.domain.entity.DisciplineEntity;

/**
 * 教科Repository.
 */
public interface DisciplineRepository extends JpaRepository<DisciplineEntity, String> {
}
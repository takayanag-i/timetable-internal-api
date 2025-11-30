package dev.timetable.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.timetable.spring.domain.entity.ConstraintDefinitionMasterEntity;

/**
 * 制約定義マスタRepository.
 */
public interface ConstraintDefinitionMasterRepository
    extends JpaRepository<ConstraintDefinitionMasterEntity, String> {
}


package dev.timetable.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.timetable.spring.domain.entity.TimetableEntity;

public interface TimetableRepository extends JpaRepository<TimetableEntity, java.util.UUID> {
}

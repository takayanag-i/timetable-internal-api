package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;

import dev.timetable.spring.domain.entity.ConstraintViolationEntity;
import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.dto.constraintviolation.UpsertConstraintViolationsInput.ConstraintViolationInput;
import dev.timetable.spring.dto.timetableresult.UpsertTimetableResultsInput.TimetableResultInput;

/**
 * 制約違反Updater.
 */
public class ConstraintViolationUpdater {

    private ConstraintViolationUpdater() {
    }

    /**
     * 制約違反Entityを作成する.
     */
    public static ConstraintViolationEntity create(
            TimetableResultEntity timetableResult,
            ConstraintViolationInput input,
            String updatedBy,
            OffsetDateTime now) {
        return ConstraintViolationEntity.builder()
            .timetableResult(timetableResult)
            .constraintViolationCode(input.getConstraintViolationCode())
            .violatingKeys(input.getViolatingKeys())
            .createdAt(now)
            .createdBy(updatedBy)
            .updatedAt(now)
            .updatedBy(updatedBy)
            .build();
    }

    /**
     * 制約違反Entityを作成する（TimetableResultInput用）.
     */
    public static ConstraintViolationEntity create(
            TimetableResultEntity timetableResult,
            TimetableResultInput.ConstraintViolationInput input,
            String updatedBy,
            OffsetDateTime now) {
        return ConstraintViolationEntity.builder()
            .timetableResult(timetableResult)
            .constraintViolationCode(input.getConstraintViolationCode())
            .violatingKeys(input.getViolatingKeys())
            .createdAt(now)
            .createdBy(updatedBy)
            .updatedAt(now)
            .updatedBy(updatedBy)
            .build();
    }

    /**
     * 制約違反Entityを更新する.
     */
    public static void update(
            ConstraintViolationEntity entity,
            ConstraintViolationInput input,
            String updatedBy,
            OffsetDateTime now) {
        entity.setConstraintViolationCode(input.getConstraintViolationCode());
        entity.setViolatingKeys(input.getViolatingKeys());
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
    }
}

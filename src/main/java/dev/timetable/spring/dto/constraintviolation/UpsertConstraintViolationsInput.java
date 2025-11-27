package dev.timetable.spring.dto.constraintviolation;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 制約違反作成更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertConstraintViolationsInput {

    Long timetableResultId;
    List<ConstraintViolationInput> constraintViolations;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class ConstraintViolationInput {
        Long id;
        String constraintViolationCode;
        Object violatingKeys;
    }
}

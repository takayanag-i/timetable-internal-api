package dev.timetable.spring.dto.constraintviolation;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 制約違反取得 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveConstraintViolationsInput {

    Long id;
    Long timetableResultId;
}

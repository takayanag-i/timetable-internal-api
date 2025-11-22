package dev.timetable.spring.dto.constraintdefinition;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 制約定義削除 Input.
 */
@Value
@Builder
@Jacksonized
public class DeleteConstraintDefinitionsInput {
    List<Long> ids;
}

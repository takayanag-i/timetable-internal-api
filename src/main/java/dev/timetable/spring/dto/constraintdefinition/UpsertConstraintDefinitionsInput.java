package dev.timetable.spring.dto.constraintdefinition;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 制約定義作成更新 Input.
 */
@Value
@Builder
@Jacksonized
public class UpsertConstraintDefinitionsInput {
    UUID ttid;
    List<ConstraintDefinitionInput> constraintDefinitions;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class ConstraintDefinitionInput {
        Long id;
        String constraintDefinitionCode;
        Boolean softFlag;
        Double penaltyWeight;
        Object parameters;
    }
}

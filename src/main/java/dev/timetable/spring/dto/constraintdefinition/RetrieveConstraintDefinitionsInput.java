package dev.timetable.spring.dto.constraintdefinition;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 制約定義取得 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveConstraintDefinitionsInput {
    
    UUID ttid;
    Long id;
}

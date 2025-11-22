package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.timetable.spring.domain.entity.ConstraintDefinitionEntity;
import dev.timetable.spring.dto.constraintdefinition.UpsertConstraintDefinitionsInput.ConstraintDefinitionInput;

/**
 * 制約定義Updater.
 */
public class ConstraintDefinitionUpdater {
    
    /**
     * 新規作成.
     * 
     * @param input 制約定義Input
     * @param ttid 時間割ID
     * @param createdBy 作成者
     * @param now 現在日時
     * @return 制約定義Entity
     */
    public static ConstraintDefinitionEntity create(
            ConstraintDefinitionInput input, 
            UUID ttid, 
            String createdBy, 
            OffsetDateTime now) {
        return ConstraintDefinitionEntity.builder()
            .ttid(ttid)
            .constraintDefinitionCode(input.getConstraintDefinitionCode())
            .softFlag(input.getSoftFlag())
            .penaltyWeight(input.getPenaltyWeight())
            .parameters(input.getParameters())
            .createdAt(now)
            .createdBy(createdBy)
            .updatedAt(now)
            .updatedBy(createdBy)
            .build();
    }

    /**
     * 更新.
     * 
     * @param entity 制約定義Entity
     * @param input 制約定義Input
     * @param updatedBy 更新者
     * @param now 現在日時
     */
    public static void update(
            ConstraintDefinitionEntity entity, 
            ConstraintDefinitionInput input, 
            String updatedBy, 
            OffsetDateTime now) {
        entity.setConstraintDefinitionCode(input.getConstraintDefinitionCode());
        entity.setSoftFlag(input.getSoftFlag());
        entity.setPenaltyWeight(input.getPenaltyWeight());
        entity.setParameters(input.getParameters());
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
    }
}

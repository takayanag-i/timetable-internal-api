package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.ConstraintDefinitionEntity;
import dev.timetable.spring.domain.updater.ConstraintDefinitionUpdater;
import dev.timetable.spring.dto.constraintdefinition.DeleteConstraintDefinitionsInput;
import dev.timetable.spring.dto.constraintdefinition.RetrieveConstraintDefinitionsInput;
import dev.timetable.spring.dto.constraintdefinition.UpsertConstraintDefinitionsInput;
import dev.timetable.spring.dto.constraintdefinition.UpsertConstraintDefinitionsInput.ConstraintDefinitionInput;
import dev.timetable.spring.repository.ConstraintDefinitionRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 制約定義Service.
 */
@Service
@RequiredArgsConstructor
public class ConstraintDefinitionService {

    /** 制約定義Repository. */
    private final ConstraintDefinitionRepository constraintDefinitionRepository;

    /**
     * 制約定義を取得する.
     * 
     * @param input 取得Input
     * @return 制約定義リスト
     */
    @Transactional(readOnly = true)
    public List<ConstraintDefinitionEntity> retrieve(RetrieveConstraintDefinitionsInput input) {
        return constraintDefinitionRepository.findByConditions(input.getTtid(), input.getId());
    }

    /**
     * 制約定義を作成または更新する.
     *
     * @param input 作成更新Input
     * @return 制約定義リスト
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ConstraintDefinitionEntity> upsert(UpsertConstraintDefinitionsInput input) {
        UUID ttid = input.getTtid();
        List<ConstraintDefinitionInput> inputs = input.getConstraintDefinitions();
        List<ConstraintDefinitionEntity> result = new ArrayList<>();
        
        if (!inputs.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();
            
            // 作成と更新を分離
            List<ConstraintDefinitionInput> inputsToCreate = inputs.stream()
                .filter(req -> req.getId() == null)
                .toList();
            
            List<ConstraintDefinitionInput> inputsToUpdate = inputs.stream()
                .filter(req -> req.getId() != null)
                .toList();
            
            // 作成
            if (!inputsToCreate.isEmpty()) {
                List<ConstraintDefinitionEntity> entities = create(inputsToCreate, ttid, input.getBy(), now);
                result.addAll(entities);
            }
            
            // 更新
            if (!inputsToUpdate.isEmpty()) {
                List<ConstraintDefinitionEntity> entities = update(inputsToUpdate, input.getBy(), now);
                result.addAll(entities);
            }
        }
        return result;
    }

    /**
     * 制約定義を削除する.
     * 
     * @param input 削除Input
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(DeleteConstraintDefinitionsInput input) {
        List<Long> ids = input.getIds();
        
        if (ids != null && !ids.isEmpty()) {
            // Entityを一括取得
            List<ConstraintDefinitionEntity> entities = constraintDefinitionRepository.findAllById(ids);
            
            // 存在しない制約定義IDをチェック
            EntityValidationUtil.validateAllEntitiesExist(
                ids,
                entities,
                ConstraintDefinitionEntity::getId,
                "制約定義ID"
            );
            
            // DBから削除
            constraintDefinitionRepository.deleteAll(entities);
        }
    }

    /**
     * 制約定義を1件削除する.
     *
     * @param id 制約定義ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }
        // 存在確認
        List<Long> ids = java.util.List.of(id);
        List<ConstraintDefinitionEntity> entities = constraintDefinitionRepository.findAllById(ids);
        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            ConstraintDefinitionEntity::getId,
            "制約定義ID"
        );
        // 削除
        constraintDefinitionRepository.deleteById(id);
    }

    /**
     * 制約定義を作成する.
     * 
     * @param inputs 制約定義Inputリスト
     * @param ttid 時間割ID
     * @param updatedBy 更新者
     * @param now 現在日時
     * @return 制約定義Entityリスト
     */
    private List<ConstraintDefinitionEntity> create(
            List<ConstraintDefinitionInput> inputs, 
            UUID ttid, 
            String updatedBy, 
            OffsetDateTime now) {
        // Entityを一括作成
        List<ConstraintDefinitionEntity> entities = inputs.stream()
            .map(input -> ConstraintDefinitionUpdater.create(input, ttid, updatedBy, now))
            .toList();
        
        // DBに保存
        constraintDefinitionRepository.saveAll(entities);

        return entities;
    }

    /**
     * 制約定義を更新する.
     * 
     * @param inputs 制約定義Inputリスト
     * @param updatedBy 更新者
     * @param now 現在日時
     * @return 制約定義Entityリスト
     */
    private List<ConstraintDefinitionEntity> update(
            List<ConstraintDefinitionInput> inputs, 
            String updatedBy, 
            OffsetDateTime now) {
        // 制約定義IDを一括取得
        List<Long> ids = inputs.stream()
            .map(ConstraintDefinitionInput::getId)
            .toList();
        
        // Entityを一括取得
        List<ConstraintDefinitionEntity> entities = constraintDefinitionRepository.findAllById(ids);
        
        // 存在しない制約定義IDをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            ConstraintDefinitionEntity::getId,
            "制約定義ID"
        );
        
        // ID -> Entity のMap作成
        Map<Long, ConstraintDefinitionEntity> entityMap = EntityMapUtil.toMap(
            entities,
            ConstraintDefinitionEntity::getId
        );
        
        // Entityを一括更新
        inputs.forEach(input -> {
            ConstraintDefinitionEntity entity = entityMap.get(input.getId());
            ConstraintDefinitionUpdater.update(entity, input, updatedBy, now);
        });
        
        // DBに保存
        constraintDefinitionRepository.saveAll(entities);

        return entities;
    }
}

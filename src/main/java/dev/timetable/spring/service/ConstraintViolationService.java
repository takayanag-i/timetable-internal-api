package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.ConstraintViolationEntity;
import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.domain.updater.ConstraintViolationUpdater;
import dev.timetable.spring.dto.constraintviolation.RetrieveConstraintViolationsInput;
import dev.timetable.spring.dto.constraintviolation.UpsertConstraintViolationsInput;
import dev.timetable.spring.dto.constraintviolation.UpsertConstraintViolationsInput.ConstraintViolationInput;
import dev.timetable.spring.repository.ConstraintViolationRepository;
import dev.timetable.spring.repository.TimetableResultRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 制約違反Service.
 */
@Service
@RequiredArgsConstructor
public class ConstraintViolationService {

    private final ConstraintViolationRepository constraintViolationRepository;
    private final TimetableResultRepository timetableResultRepository;

    /**
     * 制約違反を取得する.
     *
     * @param input 取得Input
     * @return エンティティリスト
     */
    @Transactional(readOnly = true)
    public List<ConstraintViolationEntity> retrieve(RetrieveConstraintViolationsInput input) {
        return constraintViolationRepository.findByConditions(input.getTimetableResultId(), input.getId());
    }

    /**
     * 制約違反を作成または更新する.
     *
     * @param input 作成更新Input
     * @return エンティティリスト
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ConstraintViolationEntity> upsert(UpsertConstraintViolationsInput input) {
        List<ConstraintViolationInput> constraintViolations = input.getConstraintViolations();
        List<ConstraintViolationEntity> result = new ArrayList<>();

        if (!constraintViolations.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();

            List<ConstraintViolationInput> inputsToCreate = constraintViolations.stream()
                .filter(req -> req.getId() == null)
                .toList();

            List<ConstraintViolationInput> inputsToUpdate = constraintViolations.stream()
                .filter(req -> req.getId() != null)
                .toList();

            if (!inputsToCreate.isEmpty()) {
                Long timetableResultId = input.getTimetableResultId();
                if (timetableResultId == null) {
                    throw new IllegalArgumentException("制約違反の作成には時間割編成結果IDが必要です");
                }
                List<ConstraintViolationEntity> created = create(timetableResultId, inputsToCreate, input.getBy(), now);
                result.addAll(created);
            }

            if (!inputsToUpdate.isEmpty()) {
                List<ConstraintViolationEntity> updated = update(inputsToUpdate, input.getBy(), now);
                result.addAll(updated);
            }
        }

        return result;
    }

    private List<ConstraintViolationEntity> create(Long timetableResultId, List<ConstraintViolationInput> inputs, String updatedBy, OffsetDateTime now) {
        TimetableResultEntity timetableResult = timetableResultRepository.findById(timetableResultId)
            .orElseThrow(() -> new IllegalArgumentException("存在しない時間割編成結果ID: " + timetableResultId));

        List<ConstraintViolationEntity> entities = inputs.stream()
            .map(input -> ConstraintViolationUpdater.create(timetableResult, input, updatedBy, now))
            .toList();

        constraintViolationRepository.saveAll(entities);
        return entities;
    }

    private List<ConstraintViolationEntity> update(List<ConstraintViolationInput> inputs, String updatedBy, OffsetDateTime now) {
        List<Long> ids = inputs.stream()
            .map(ConstraintViolationInput::getId)
            .toList();

        List<ConstraintViolationEntity> entities = constraintViolationRepository.findAllById(ids);

        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            ConstraintViolationEntity::getId,
            "制約違反ID"
        );

        Map<Long, ConstraintViolationEntity> entityMap = EntityMapUtil.toMap(
            entities,
            ConstraintViolationEntity::getId
        );

        inputs.forEach(input -> {
            ConstraintViolationEntity entity = entityMap.get(input.getId());
            ConstraintViolationUpdater.update(entity, input, updatedBy, now);
        });

        constraintViolationRepository.saveAll(entities);
        return entities;
    }
}

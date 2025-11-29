package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.domain.updater.TimetableResultUpdater;
import dev.timetable.spring.dto.timetableresult.RetrieveTimetableResultsInput;
import dev.timetable.spring.dto.timetableresult.UpsertTimetableResultsInput;
import dev.timetable.spring.dto.timetableresult.UpsertTimetableResultsInput.TimetableResultInput;
import dev.timetable.spring.repository.TimetableResultRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 時間割編成結果Service.
 */
@Service
@RequiredArgsConstructor
public class TimetableResultService {

    private final TimetableResultRepository timetableResultRepository;

    /**
     * 時間割編成結果を取得する.
     *
     * @param input 取得Input
     * @return エンティティリスト
     */
    @Transactional(readOnly = true)
    public List<TimetableResultEntity> retrieve(RetrieveTimetableResultsInput input) {
        return timetableResultRepository.findByConditions(input.getTtid(), input.getId());
    }

    /**
     * 時間割編成結果を作成または更新する.
     *
     * @param input 作成更新Input
     * @return エンティティリスト
     */
    @Transactional(rollbackFor = Exception.class)
    public List<TimetableResultEntity> upsert(UpsertTimetableResultsInput input) {
        List<TimetableResultInput> timetableResults = input.getTimetableResults();
        List<TimetableResultEntity> result = new ArrayList<>();

        if (!timetableResults.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();

            List<TimetableResultInput> inputsToCreate = timetableResults.stream()
                .filter(req -> req.getId() == null)
                .toList();

            List<TimetableResultInput> inputsToUpdate = timetableResults.stream()
                .filter(req -> req.getId() != null)
                .toList();

            if (!inputsToCreate.isEmpty()) {
                UUID ttid = input.getTtid();
                if (ttid == null) {
                    throw new IllegalArgumentException("時間割編成結果の作成にはTTIDが必要です");
                }
                List<TimetableResultEntity> created = create(ttid, inputsToCreate, input.getBy(), now);
                result.addAll(created);
            }

            if (!inputsToUpdate.isEmpty()) {
                List<TimetableResultEntity> updated = update(inputsToUpdate, input.getBy(), now);
                result.addAll(updated);
            }
        }

        return result;
    }

    private List<TimetableResultEntity> create(UUID ttid, List<TimetableResultInput> inputs, String updatedBy, OffsetDateTime now) {
        List<TimetableResultEntity> entities = inputs.stream()
            .map(input -> TimetableResultUpdater.create(ttid, input, updatedBy, now))
            .toList();

        timetableResultRepository.saveAll(entities);
        return entities;
    }

    private List<TimetableResultEntity> update(List<TimetableResultInput> inputs, String updatedBy, OffsetDateTime now) {
        List<Long> ids = inputs.stream()
            .map(TimetableResultInput::getId)
            .toList();

        List<TimetableResultEntity> entities = timetableResultRepository.findAllById(ids);

        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            TimetableResultEntity::getId,
            "時間割編成結果ID"
        );

        Map<Long, TimetableResultEntity> entityMap = EntityMapUtil.toMap(
            entities,
            TimetableResultEntity::getId
        );

        inputs.forEach(input -> {
            TimetableResultEntity entity = entityMap.get(input.getId());
            TimetableResultUpdater.update(entity, input, updatedBy, now);
        });

        timetableResultRepository.saveAll(entities);
        return entities;
    }
}

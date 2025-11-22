package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.GradeEntity;
import dev.timetable.spring.domain.updater.GradeUpdater;
import dev.timetable.spring.dto.grade.RetrieveGradesInput;
import dev.timetable.spring.dto.grade.UpsertGradesInput;
import dev.timetable.spring.dto.grade.UpsertGradesInput.UpdateGradeInput;
import dev.timetable.spring.repository.GradeRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 学年Service.
 */
@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    /**
     * 学年を取得する.
     *
     * @param input 取得Input
     * @return エンティティリスト
     */
    @Transactional(readOnly = true)
    public List<GradeEntity> retrieve(RetrieveGradesInput input) {
        if (input.getId() != null) {
            return gradeRepository.findById(input.getId())
                .map(List::of)
                .orElseGet(ArrayList::new);
        } else if (input.getTtid() != null) {
            return gradeRepository.findByTtid(input.getTtid());
        }
        return gradeRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<GradeEntity> upsert(UpsertGradesInput input) {
        List<UpdateGradeInput> grades = input.getGrades();
        List<GradeEntity> result = new ArrayList<>();

        if (!grades.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();

            List<UpdateGradeInput> gradesToCreate = grades.stream()
                .filter(update -> update.getId() == null)
                .toList();

            List<UpdateGradeInput> gradesToUpdate = grades.stream()
                .filter(update -> update.getId() != null)
                .toList();

            if (!gradesToCreate.isEmpty()) {
                UUID ttid = input.getTtid();
                if (ttid == null) {
                    throw new IllegalArgumentException("学年の作成にはTTIDが必要です");
                }
                List<GradeEntity> created = create(ttid, gradesToCreate, input.getBy(), now);
                result.addAll(created);
            }

            if (!gradesToUpdate.isEmpty()) {
                List<GradeEntity> updated = update(gradesToUpdate, input.getBy(), now);
                result.addAll(updated);
            }
        }

        return result;
    }

    private List<GradeEntity> create(UUID ttid, List<UpdateGradeInput> inputs, String updatedBy, OffsetDateTime now) {
        List<GradeEntity> entities = inputs.stream()
            .map(input -> GradeUpdater.create(ttid, input, updatedBy, now))
            .toList();

        gradeRepository.saveAll(entities);
        return entities;
    }

    private List<GradeEntity> update(List<UpdateGradeInput> inputs, String updatedBy, OffsetDateTime now) {
        List<Long> ids = inputs.stream()
            .map(UpdateGradeInput::getId)
            .toList();

        List<GradeEntity> entities = gradeRepository.findAllById(ids);

        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            GradeEntity::getId,
            "学年ID"
        );

        Map<Long, GradeEntity> gradeMap = EntityMapUtil.toMap(
            entities,
            GradeEntity::getId
        );

        inputs.forEach(input -> {
            GradeEntity entity = gradeMap.get(input.getId());
            GradeUpdater.update(entity, input, updatedBy, now);
        });

        gradeRepository.saveAll(entities);
        return entities;
    }
}


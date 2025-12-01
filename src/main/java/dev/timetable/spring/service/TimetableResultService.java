package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.ConstraintViolationEntity;
import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.domain.entity.TimetableEntryEntity;
import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.domain.updater.ConstraintViolationUpdater;
import dev.timetable.spring.domain.updater.TimetableEntryUpdater;
import dev.timetable.spring.domain.updater.TimetableResultUpdater;
import dev.timetable.spring.dto.timetableresult.RetrieveTimetableResultsInput;
import dev.timetable.spring.dto.timetableresult.UpsertTimetableResultsInput;
import dev.timetable.spring.dto.timetableresult.UpsertTimetableResultsInput.TimetableResultInput;
import dev.timetable.spring.dto.timetableresult.UpsertTimetableResultsInput.TimetableResultInput.TimetableEntryInput;
import dev.timetable.spring.repository.CourseRepository;
import dev.timetable.spring.repository.HomeroomRepository;
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
    private final HomeroomRepository homeroomRepository;
    private final CourseRepository courseRepository;

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
        // 全入力から学級IDと講座IDを収集
        List<Long> allHomeroomIds = inputs.stream()
            .filter(input -> input.getTimetableEntries() != null)
            .flatMap(input -> input.getTimetableEntries().stream())
            .map(TimetableEntryInput::getHomeroomId)
            .distinct()
            .toList();

        List<Long> allCourseIds = inputs.stream()
            .filter(input -> input.getTimetableEntries() != null)
            .flatMap(input -> input.getTimetableEntries().stream())
            .map(TimetableEntryInput::getCourseId)
            .distinct()
            .toList();

        // エンティティマップを作成
        Map<Long, HomeroomEntity> homeroomMap = Map.of();
        if (!allHomeroomIds.isEmpty()) {
            List<HomeroomEntity> homerooms = homeroomRepository.findAllById(allHomeroomIds);
            EntityValidationUtil.validateAllEntitiesExist(allHomeroomIds, homerooms, HomeroomEntity::getId, "学級ID");
            homeroomMap = EntityMapUtil.toMap(homerooms, HomeroomEntity::getId);
        }

        Map<Long, CourseEntity> courseMap = Map.of();
        if (!allCourseIds.isEmpty()) {
            List<CourseEntity> courses = courseRepository.findAllById(allCourseIds);
            EntityValidationUtil.validateAllEntitiesExist(allCourseIds, courses, CourseEntity::getId, "講座ID");
            courseMap = EntityMapUtil.toMap(courses, CourseEntity::getId);
        }

        // 親エンティティを作成し、子エンティティも同時に追加
        final Map<Long, HomeroomEntity> finalHomeroomMap = homeroomMap;
        final Map<Long, CourseEntity> finalCourseMap = courseMap;

        List<TimetableResultEntity> entities = inputs.stream()
            .map(input -> {
                TimetableResultEntity entity = TimetableResultUpdater.create(ttid, input, updatedBy, now);

                // TimetableEntriesを作成して親に追加
                if (input.getTimetableEntries() != null && !input.getTimetableEntries().isEmpty()) {
                    List<TimetableEntryEntity> entries = input.getTimetableEntries().stream()
                        .map(entryInput -> TimetableEntryUpdater.create(
                            entity,
                            finalHomeroomMap.get(entryInput.getHomeroomId()),
                            finalCourseMap.get(entryInput.getCourseId()),
                            entryInput,
                            updatedBy,
                            now
                        ))
                        .toList();
                    entity.getTimetableEntries().addAll(entries);
                }

                // ConstraintViolationsを作成して親に追加
                if (input.getConstraintViolations() != null && !input.getConstraintViolations().isEmpty()) {
                    List<ConstraintViolationEntity> violations = input.getConstraintViolations().stream()
                        .map(violationInput -> ConstraintViolationUpdater.create(
                            entity,
                            violationInput,
                            updatedBy,
                            now
                        ))
                        .toList();
                    entity.getConstraintViolations().addAll(violations);
                }

                return entity;
            })
            .toList();

        // CascadeType.ALLにより1回のsaveで親と子が全て保存される
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

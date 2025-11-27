package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.domain.entity.TimetableEntryEntity;
import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.domain.updater.TimetableEntryUpdater;
import dev.timetable.spring.dto.timetableentry.RetrieveTimetableEntriesInput;
import dev.timetable.spring.dto.timetableentry.UpsertTimetableEntriesInput;
import dev.timetable.spring.dto.timetableentry.UpsertTimetableEntriesInput.TimetableEntryInput;
import dev.timetable.spring.repository.CourseRepository;
import dev.timetable.spring.repository.HomeroomRepository;
import dev.timetable.spring.repository.TimetableEntryRepository;
import dev.timetable.spring.repository.TimetableResultRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 時間割エントリService.
 */
@Service
@RequiredArgsConstructor
public class TimetableEntryService {

    private final TimetableEntryRepository timetableEntryRepository;
    private final TimetableResultRepository timetableResultRepository;
    private final HomeroomRepository homeroomRepository;
    private final CourseRepository courseRepository;

    /**
     * 時間割エントリを取得する.
     *
     * @param input 取得Input
     * @return エンティティリスト
     */
    @Transactional(readOnly = true)
    public List<TimetableEntryEntity> retrieve(RetrieveTimetableEntriesInput input) {
        return timetableEntryRepository.findByConditions(input.getTimetableResultId(), input.getId());
    }

    /**
     * 時間割エントリを作成または更新する.
     *
     * @param input 作成更新Input
     * @return エンティティリスト
     */
    @Transactional(rollbackFor = Exception.class)
    public List<TimetableEntryEntity> upsert(UpsertTimetableEntriesInput input) {
        List<TimetableEntryInput> timetableEntries = input.getTimetableEntries();
        List<TimetableEntryEntity> result = new ArrayList<>();

        if (!timetableEntries.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();

            List<TimetableEntryInput> inputsToCreate = timetableEntries.stream()
                .filter(req -> req.getId() == null)
                .toList();

            List<TimetableEntryInput> inputsToUpdate = timetableEntries.stream()
                .filter(req -> req.getId() != null)
                .toList();

            if (!inputsToCreate.isEmpty()) {
                Long timetableResultId = input.getTimetableResultId();
                if (timetableResultId == null) {
                    throw new IllegalArgumentException("時間割エントリの作成には時間割編成結果IDが必要です");
                }
                List<TimetableEntryEntity> created = create(timetableResultId, inputsToCreate, input.getBy(), now);
                result.addAll(created);
            }

            if (!inputsToUpdate.isEmpty()) {
                List<TimetableEntryEntity> updated = update(inputsToUpdate, input.getBy(), now);
                result.addAll(updated);
            }
        }

        return result;
    }

    private List<TimetableEntryEntity> create(Long timetableResultId, List<TimetableEntryInput> inputs, String updatedBy, OffsetDateTime now) {
        // 時間割編成結果を取得
        TimetableResultEntity timetableResult = timetableResultRepository.findById(timetableResultId)
            .orElseThrow(() -> new IllegalArgumentException("存在しない時間割編成結果ID: " + timetableResultId));

        // 学級マップを作成
        List<Long> homeroomIds = inputs.stream()
            .map(TimetableEntryInput::getHomeroomId)
            .distinct()
            .toList();
        List<HomeroomEntity> homerooms = homeroomRepository.findAllById(homeroomIds);
        EntityValidationUtil.validateAllEntitiesExist(homeroomIds, homerooms, HomeroomEntity::getId, "学級ID");
        Map<Long, HomeroomEntity> homeroomMap = EntityMapUtil.toMap(homerooms, HomeroomEntity::getId);

        // 講座マップを作成
        List<Long> courseIds = inputs.stream()
            .map(TimetableEntryInput::getCourseId)
            .distinct()
            .toList();
        List<CourseEntity> courses = courseRepository.findAllById(courseIds);
        EntityValidationUtil.validateAllEntitiesExist(courseIds, courses, CourseEntity::getId, "講座ID");
        Map<Long, CourseEntity> courseMap = EntityMapUtil.toMap(courses, CourseEntity::getId);

        List<TimetableEntryEntity> entities = inputs.stream()
            .map(input -> TimetableEntryUpdater.create(
                timetableResult,
                homeroomMap.get(input.getHomeroomId()),
                courseMap.get(input.getCourseId()),
                input,
                updatedBy,
                now
            ))
            .toList();

        timetableEntryRepository.saveAll(entities);
        return entities;
    }

    private List<TimetableEntryEntity> update(List<TimetableEntryInput> inputs, String updatedBy, OffsetDateTime now) {
        List<Long> ids = inputs.stream()
            .map(TimetableEntryInput::getId)
            .toList();

        List<TimetableEntryEntity> entities = timetableEntryRepository.findAllById(ids);

        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            TimetableEntryEntity::getId,
            "時間割エントリID"
        );

        // 学級マップを作成
        List<Long> homeroomIds = inputs.stream()
            .map(TimetableEntryInput::getHomeroomId)
            .distinct()
            .toList();
        List<HomeroomEntity> homerooms = homeroomRepository.findAllById(homeroomIds);
        EntityValidationUtil.validateAllEntitiesExist(homeroomIds, homerooms, HomeroomEntity::getId, "学級ID");
        Map<Long, HomeroomEntity> homeroomMap = EntityMapUtil.toMap(homerooms, HomeroomEntity::getId);

        // 講座マップを作成
        List<Long> courseIds = inputs.stream()
            .map(TimetableEntryInput::getCourseId)
            .distinct()
            .toList();
        List<CourseEntity> courses = courseRepository.findAllById(courseIds);
        EntityValidationUtil.validateAllEntitiesExist(courseIds, courses, CourseEntity::getId, "講座ID");
        Map<Long, CourseEntity> courseMap = EntityMapUtil.toMap(courses, CourseEntity::getId);

        Map<Long, TimetableEntryEntity> entityMap = EntityMapUtil.toMap(
            entities,
            TimetableEntryEntity::getId
        );

        inputs.forEach(input -> {
            TimetableEntryEntity entity = entityMap.get(input.getId());
            TimetableEntryUpdater.update(
                entity,
                homeroomMap.get(input.getHomeroomId()),
                courseMap.get(input.getCourseId()),
                input,
                updatedBy,
                now
            );
        });

        timetableEntryRepository.saveAll(entities);
        return entities;
    }
}

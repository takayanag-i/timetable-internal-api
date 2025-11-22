package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.dto.lane.RetrieveLanesInput;
import dev.timetable.spring.dto.lane.UpsertLanesInput;
import dev.timetable.spring.dto.lane.UpsertLanesInput.LaneInput;
import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.LaneEntity;
import dev.timetable.spring.domain.updater.LaneUpdater;
import dev.timetable.spring.repository.CourseRepository;
import dev.timetable.spring.repository.LaneRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * レーンService.
 */
@Service
@RequiredArgsConstructor
public class LaneService {

    /** レーンRepository. */
    private final LaneRepository laneRepository;
    
    /** 講座Repository. */
    private final CourseRepository courseRepository;

    /**
     * レーンを取得する.
     * 
     * @param input 検索条件
     * @return レーンEntityリスト
     */
    public List<LaneEntity> retrieve(RetrieveLanesInput input) {
        return laneRepository.findByConditions(
            input.getBlockId(),
            input.getId()
        );
    }

    /**
     * レーンを更新する.
     */
    @Transactional(rollbackFor = Exception.class)
    public List<LaneEntity> upsert(UpsertLanesInput req) {
        List<LaneInput> inputs = req.getLanes();
        List<LaneEntity> result = new ArrayList<>();
        
        OffsetDateTime now = OffsetDateTime.now();

        result = update(inputs, req.getBy(), now);
        
        return result;
    }

    private List<LaneEntity> update(List<LaneInput> inputs, String by, OffsetDateTime now) {
        // IDを一括取得
        List<Long> ids = inputs.stream()
            .map(LaneInput::getId)
            .toList();
        
        // Entityを一括取得
        List<LaneEntity> entities = laneRepository.findAllById(ids);

        // 存在しないレーンIDをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            LaneEntity::getId,
            "レーンID"
        );
        
        // ID -> Entity のMap作成
        Map<Long, LaneEntity> entityMap = EntityMapUtil.toMap(
            entities,
            LaneEntity::getId
        );

        // Entityを一括更新
        inputs.forEach(input -> {
            // 講座ID -> 講座EntityのMap作成
            List<Long> courseIds = input.getCourseIds();
            List<CourseEntity> courses = courseRepository.findAllById(courseIds);
            
            // 存在しない講座IDをチェック
            EntityValidationUtil.validateAllEntitiesExist(
                courseIds,
                courses,
                CourseEntity::getId,
                "講座ID"
            );
            
            Map<Long, CourseEntity> courseMap = EntityMapUtil.toMap(
                courses,
                CourseEntity::getId
            );

            LaneEntity entity = entityMap.get(input.getId());
            LaneUpdater.update(entity, input, courseMap, by, now);
        });
        
        // DBに保存
        laneRepository.saveAll(entities);

        return entities;
    }
}

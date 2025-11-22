package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.SchoolDayEntity;
import dev.timetable.spring.domain.updater.SchoolDayUpdater;
import dev.timetable.spring.dto.schoolday.RetrieveSchoolDaysInput;
import dev.timetable.spring.dto.schoolday.UpsertSchoolDaysInput;
import dev.timetable.spring.dto.schoolday.UpsertSchoolDaysInput.SchoolDayInput;
import dev.timetable.spring.repository.SchoolDayRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 学校曜日Service.
 */
@Service
@RequiredArgsConstructor
public class SchoolDayService {

    /** 学校曜日Repository. */
    private final SchoolDayRepository schoolDayRepository;

    /**
     * 学校曜日を取得する.
     *
     * @param input 取得Input
     * @return エンティティリスト
     */
    @Transactional(readOnly = true)
    public List<SchoolDayEntity> retrieve(RetrieveSchoolDaysInput input) {
        return schoolDayRepository.findByConditions(input.getTtid(), input.getId());
    }

    /**
     * 学校曜日を作成または更新する.
     *
     * @param req 作成更新Input
     */
    @Transactional(rollbackFor = Exception.class)
    public List<SchoolDayEntity> upsert(UpsertSchoolDaysInput req) {
        UUID ttid = req.getTtid();
                
        List<SchoolDayInput> inputs = req.getSchoolDays();
        List<SchoolDayEntity> result = new ArrayList<>();
        
        if (!inputs.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();
            
            // 作成と更新を分離
            List<SchoolDayInput> inputsToCreate = inputs.stream()
                .filter(input -> input.getSchoolDayId() == null)
                .toList();
            
            List<SchoolDayInput> inputsToUpdate = inputs.stream()
                .filter(input -> input.getSchoolDayId() != null)
                .toList();
            
            // 作成
            if (!inputsToCreate.isEmpty()) {
                List<SchoolDayEntity> entities = create(inputsToCreate, ttid, req.getBy(), now);
                result.addAll(entities);
            }
            
            // 更新
            if (!inputsToUpdate.isEmpty()) {
                List<SchoolDayEntity> entities = update(inputsToUpdate, req.getBy(), now);
                result.addAll(entities);
            }
        }
        return result;
    }
    
    private List<SchoolDayEntity> create(List<SchoolDayInput> inputs, UUID ttid, String updatedBy, OffsetDateTime now) {
        // Entityを一括作成
        List<SchoolDayEntity> entities = inputs.stream()
            .map(request -> SchoolDayUpdater.create(request, ttid, updatedBy, now))
            .toList();
        
        // DBに保存
        schoolDayRepository.saveAll(entities);

        return entities;
    }

    private List<SchoolDayEntity> update(List<SchoolDayInput> inputs, String updatedBy, OffsetDateTime now) {
        // 学校曜日IDを一括取得
        List<Long> ids = inputs.stream()
            .map(SchoolDayInput::getSchoolDayId)
            .toList();
        
        // Entityを一括取得
        List<SchoolDayEntity> entities = schoolDayRepository.findAllById(ids);
        
        // 存在しない学校曜日IDをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            SchoolDayEntity::getId,
            "学校曜日ID"
        );
        
        // ID -> Entity のMap作成
        Map<Long, SchoolDayEntity> entityMap = EntityMapUtil.toMap(
            entities,
            SchoolDayEntity::getId
        );
        
        // Entityを一括更新
        inputs.forEach(input -> {
            SchoolDayEntity entity = entityMap.get(input.getSchoolDayId());
            SchoolDayUpdater.update(entity, input, updatedBy, now);
        });
        
        // DBに保存
        schoolDayRepository.saveAll(entities);

        return entities;
    }
}

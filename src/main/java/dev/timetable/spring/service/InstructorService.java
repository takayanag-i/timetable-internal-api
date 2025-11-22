package dev.timetable.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.InstructorEntity;
import dev.timetable.spring.repository.InstructorRepository;
import dev.timetable.spring.domain.updater.InstructorUpdater;
import dev.timetable.spring.dto.instructor.UpsertInstructorsInput;
import dev.timetable.spring.dto.instructor.UpsertInstructorsInput.InstructorInput;
import dev.timetable.spring.dto.instructor.RetrieveInstructorsInput;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 教員Service.
 */
@Service
@RequiredArgsConstructor
public class InstructorService {

    /** 教員Repository. */
    private final InstructorRepository instructorRepository;

    /**
     * 教員を取得する.
     * 
     * 教員を集約ルートとして、関連する勤怠曜日も取得する.
     * 
     * @param input 取得Input
     * @return エンティティリスト
     */
    @Transactional(readOnly = true)
    public List<InstructorEntity> retrieve(RetrieveInstructorsInput input) {
        return instructorRepository.findByConditions(input.getTtid(), input.getId());
    }

    /**
     * 教員を作成または更新する.
     *
     * @param req 作成更新Input
     */
    @Transactional(rollbackFor = Exception.class)
    public List<InstructorEntity> upsert(UpsertInstructorsInput req) {
        UUID ttid = req.getTtid();
                
        List<InstructorInput> inputs = req.getInstructors();
        List<InstructorEntity> result = new ArrayList<>();
        
        if (!inputs.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();
            
            // 作成と更新を分離
            List<InstructorInput> inputsToCreate = inputs.stream()
                .filter(input -> input.getId() == null)
                .toList();
            
            List<InstructorInput> inputsToUpdate = inputs.stream()
                .filter(input -> input.getId() != null)
                .toList();
            
            // 作成
            if (!inputsToCreate.isEmpty()) {
                List<InstructorEntity> entities = create(inputsToCreate, ttid, req.getBy(), now);
                result.addAll(entities);
            }
            
            // 更新
            if (!inputsToUpdate.isEmpty()) {
                List<InstructorEntity> entities = update(inputsToUpdate, req.getBy(), now);
                result.addAll(entities);
            }
        }
        return result;
    }

    private List<InstructorEntity> create(List<InstructorInput> inputs, UUID ttid, String updatedBy, OffsetDateTime now) {
        // Entityを一括作成
        List<InstructorEntity> entities = inputs.stream()
            .map(request -> InstructorUpdater.create(request, ttid, updatedBy, now))
            .toList();
        
        // DBに保存
        instructorRepository.saveAll(entities);

        return entities;
    }

    private List<InstructorEntity> update(List<InstructorInput> inputs, String updatedBy, OffsetDateTime now) {
        // 教員IDを一括取得
        List<Long> ids = inputs.stream()
            .map(InstructorInput::getId)
            .toList();
        
        // Entityを一括取得
        List<InstructorEntity> entities = instructorRepository.findAllById(ids);
        
        // 存在しない教員IDをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            InstructorEntity::getId,
            "教員ID"
        );
        
        // ID -> Entity のMap作成
        Map<Long, InstructorEntity> entityMap = EntityMapUtil.toMap(
            entities,
            InstructorEntity::getId
        );
        
        // Entityを一括更新
        inputs.forEach(input -> {
            InstructorEntity entity = entityMap.get(input.getId());
            InstructorUpdater.update(entity, input, updatedBy, now);
        });
        
        // DBに保存
        instructorRepository.saveAll(entities);

        return entities;
    }
}

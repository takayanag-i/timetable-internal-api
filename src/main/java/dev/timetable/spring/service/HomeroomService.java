package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.dto.homeroom.RetrieveHomeroomsInput;
import dev.timetable.spring.dto.homeroom.UpsertHomeroomsInput;
import dev.timetable.spring.dto.homeroom.UpsertHomeroomsInput.HomeroomInput;
import dev.timetable.spring.domain.entity.BlockEntity;
import dev.timetable.spring.domain.entity.GradeEntity;
import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.domain.updater.HomeroomUpdater;
import dev.timetable.spring.repository.BlockRepository;
import dev.timetable.spring.repository.GradeRepository;
import dev.timetable.spring.repository.HomeroomRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 学級Service.
 */
@Service
@RequiredArgsConstructor
public class HomeroomService {

    /** 学級Repository. */
    private final HomeroomRepository homeroomRepository;

    /** ブロックRepository. */
    private final BlockRepository blockRepository;

    /** 学年Repository. */
    private final GradeRepository gradeRepository;

    /**
     * 学級を取得する.
     *
     * @param input 検索条件
     * @return エンティティリスト
     */
    @Transactional(readOnly = true)
    public List<HomeroomEntity> retrieve(RetrieveHomeroomsInput input) {
        return homeroomRepository.findByConditions(input.getTtid(), input.getId());
    }

    /**
     * 学級を作成または更新する.
     *
     * @param req 作成更新Input
     */
    @Transactional(rollbackFor = Exception.class)
    public List<HomeroomEntity> upsert(UpsertHomeroomsInput req) {
        UUID ttid = req.getTtid();
                
        List<HomeroomInput> inputs = req.getHomerooms();
        List<HomeroomEntity> result = new ArrayList<>();
        
        if (!inputs.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();
            
            // 作成と更新を分離
            List<HomeroomInput> inputsToCreate = inputs.stream()
                .filter(input -> input.getId() == null)
                .toList();
            
            List<HomeroomInput> inputsToUpdate = inputs.stream()
                .filter(input -> input.getId() != null)
                .toList();
            
            // 作成
            if (!inputsToCreate.isEmpty()) {
                List<HomeroomEntity> entities = create(inputsToCreate, ttid, req.getBy(), now);
                result.addAll(entities);
            }
            
            // 更新
            if (!inputsToUpdate.isEmpty()) {
                List<HomeroomEntity> entities = update(inputsToUpdate, req.getBy(), now);
                result.addAll(entities);
            }
        }
        return result;
    }

    private List<HomeroomEntity> create(List<HomeroomInput> inputs, UUID ttid, String updatedBy, OffsetDateTime now) {
        List<Long> gradeIds = inputs.stream()
            .map(HomeroomInput::getGradeId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        List<GradeEntity> grades = gradeRepository.findAllById(gradeIds);

        EntityValidationUtil.validateAllEntitiesExist(
            gradeIds,
            grades,
            GradeEntity::getId,
            "学年ID"
        );

        Map<Long, GradeEntity> gradeMap = EntityMapUtil.toMap(
            grades,
            GradeEntity::getId
        );

        // Entityを一括作成
        List<HomeroomEntity> entities = inputs.stream()
            .map(request -> {
                GradeEntity grade = request.getGradeId() == null ? null : gradeMap.get(request.getGradeId());
                return HomeroomUpdater.create(request, ttid, grade, updatedBy, now);
            })
            .toList();
        
        // DBに保存
        homeroomRepository.saveAll(entities);

        return entities;
    }

    private List<HomeroomEntity> update(List<HomeroomInput> inputs, String updatedBy, OffsetDateTime now) {
        // 学級IDを一括取得
        List<Long> ids = inputs.stream()
            .map(HomeroomInput::getId)
            .toList();
        
        // Entityを一括取得
        List<HomeroomEntity> entities = homeroomRepository.findAllById(ids);
        
        // 存在しない学級IDをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            HomeroomEntity::getId,
            "学級ID"
        );

        List<Long> gradeIds = inputs.stream()
            .map(HomeroomInput::getGradeId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        List<GradeEntity> grades = gradeRepository.findAllById(gradeIds);

        EntityValidationUtil.validateAllEntitiesExist(
            gradeIds,
            grades,
            GradeEntity::getId,
            "学年ID"
        );

        Map<Long, GradeEntity> gradeMap = EntityMapUtil.toMap(
            grades,
            GradeEntity::getId
        );
        
        // ID -> Entity のMap作成
        Map<Long, HomeroomEntity> entityMap = EntityMapUtil.toMap(
            entities,
            HomeroomEntity::getId
        );
        
        // Entityを一括更新
        inputs.forEach(input -> {
            HomeroomEntity entity = entityMap.get(input.getId());
            GradeEntity grade = input.getGradeId() == null ? null : gradeMap.get(input.getGradeId());
            HomeroomUpdater.update(entity, input, grade, updatedBy, now);
        });
        
        // DBに保存
        homeroomRepository.saveAll(entities);

        return entities;
    }

    /*
     * 学級を削除する.
     */
    public void delete(Long id) {
        // 学級Entityを取得
        HomeroomEntity entity = homeroomRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("存在しない学級ID: " + id));
        
        // 子のブロックEntityを取得
        List<BlockEntity> children = blockRepository.findByConditions(id, null);

        // ブロックを削除
        children.forEach(block -> {
            blockRepository.delete(block);
        });

        // 学級を削除
        homeroomRepository.delete(entity);
    }
}

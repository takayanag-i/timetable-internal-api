package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.BlockEntity;
import dev.timetable.spring.domain.entity.LaneEntity;
import dev.timetable.spring.domain.updater.BlockUpdater;
import dev.timetable.spring.dto.block.RetrieveBlocksInput;
import dev.timetable.spring.dto.block.UpsertBlocksInput;
import dev.timetable.spring.dto.block.UpsertBlocksInput.BlockInput;
import dev.timetable.spring.repository.BlockRepository;
import dev.timetable.spring.repository.LaneRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * ブロックService.
 */
@Service
@RequiredArgsConstructor
public class BlockService {
    
    /** ブロックRepository. */
    private final BlockRepository blockRepository;

    /** レーンRepository. */
    private final LaneRepository laneRepository;

    /**
     * ブロックを取得する.
     * 
     * @param input 検索Input
     * @return エンティティリスト
     */
    @Transactional(readOnly = true)
    public List<BlockEntity> retrieve(RetrieveBlocksInput input) {
        return blockRepository.findByConditions(input.getHomeroomId(), input.getId());
    }


    /**
     * ブロックを作成または更新する.
     * 
     * @param req 作成更新Input
     */
    @Transactional(rollbackFor = Exception.class)
    public List<BlockEntity> upsert(UpsertBlocksInput req) {
        List<BlockInput> inputs = req.getBlocks();
        List<BlockEntity> result = new ArrayList<>();
        
        if (!inputs.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();
            
            // 作成と更新を分離
            List<BlockInput> inputsToCreate = inputs.stream()
                .filter(input -> input.getId() == null)
                .toList();
            
            List<BlockInput> inputsToUpdate = inputs.stream()
                .filter(input -> input.getId() != null)
                .toList();
            
            // 作成
            if (!inputsToCreate.isEmpty()) {
                List<BlockEntity> entities = create(inputsToCreate, req.getBy(), now);
                result.addAll(entities);
            }
            
            // 更新
            if (!inputsToUpdate.isEmpty()) {
                List<BlockEntity> entities = update(inputsToUpdate, req.getBy(), now);
                result.addAll(entities);
            }
        }
        return result;
    }

    private List<BlockEntity> create(List<BlockInput> inputs, String by, OffsetDateTime now) {
        // Entity一括作成
        List<BlockEntity> entities = inputs.stream()
            .map(input -> BlockUpdater.create(input, by, now))
            .toList();
        
        // BlockEntityを保存すると、Cascadeによりレーンも自動的に保存され、
        // @JoinColumnによりblockIdも自動的に設定される
        blockRepository.saveAll(entities);

        return entities;
    }

    private List<BlockEntity> update(List<BlockInput> inputs, String by, OffsetDateTime now) {
        // IDを一括取得
        List<Long> ids = inputs.stream()
            .map(BlockInput::getId)
            .toList();
        
        List<BlockEntity> entities = blockRepository.findAllById(ids);
        
        // 存在しないIDをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            BlockEntity::getId,
            "ブロックID"
        );
        
        // ID -> Entity のMap作成
        Map<Long, BlockEntity> entityMap = EntityMapUtil.toMap(
            entities,
            BlockEntity::getId
        );
        
        // Entityを一括更新
        inputs.forEach(input -> {
            BlockEntity entity = entityMap.get(input.getId());
            BlockUpdater.update(entity, input, by, now);
        });
        
        // DBに保存
        blockRepository.saveAll(entities);
        
        return entities;
    }

    public void delete(Long id) {
        // ブロックEntityを取得
        BlockEntity entity = blockRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("存在しないブロックID: " + id));

        // 子のレーンEntityを取得
        List<LaneEntity> children = laneRepository.findByBlock_Id(entity.getId());

        // レーンと講座の紐づけを削除
        children.stream()
            .forEach(child -> {
                child.setCourses(List.of());
            });
        
        // 親子共々削除
        laneRepository.deleteAll(children);
        blockRepository.delete(entity);
    }
}

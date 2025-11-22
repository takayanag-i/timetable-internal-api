package dev.timetable.spring.domain.updater;

import java.time.OffsetDateTime;

import dev.timetable.spring.domain.entity.BlockEntity;
import dev.timetable.spring.domain.entity.LaneEntity;
import dev.timetable.spring.dto.block.UpsertBlocksInput.BlockInput;

public class BlockUpdater {
    
    public static BlockEntity create(
        BlockInput input,
        String updatedBy,
        OffsetDateTime now
    ) {
        BlockEntity entity = BlockEntity.builder()
                .homeroomId(input.getHomeroomId())
                .blockName(input.getBlockName())
                .createdAt(now)
                .createdBy(updatedBy)
                .updatedAt(now)
                .updatedBy(updatedBy)
                .build();
        
        // laneCountの数だけLaneEntityを生成してBlockEntityに追加
        for (int i = 0; i < input.getLaneCount(); i++) {
            LaneEntity lane = LaneEntity.builder()
                    .block(entity) // BlockEntityへの参照を設定
                    .createdAt(now)
                    .createdBy(updatedBy)
                    .updatedAt(now)
                    .updatedBy(updatedBy)
                    .build();
            entity.getLanes().add(lane);
        }
        
        return entity;
    }
    
    public static void update(BlockEntity entity, BlockInput req, String updatedBy, OffsetDateTime now) {
        entity.setBlockName(req.getBlockName());
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(updatedBy);
        
        // レーン数の変更に対応する
        int currentLaneCount = entity.getLanes().size();
        int requestedLaneCount = req.getLaneCount();
        
        if (requestedLaneCount > currentLaneCount) {
            // レーンを追加
            for (int i = currentLaneCount; i < requestedLaneCount; i++) {
                LaneEntity lane = LaneEntity.builder()
                        .block(entity) // BlockEntityへの参照を設定
                        .createdAt(now)
                        .createdBy(updatedBy)
                        .updatedAt(now)
                        .updatedBy(updatedBy)
                        .build();
                entity.getLanes().add(lane);
            }
        } else if (requestedLaneCount < currentLaneCount) { // TODO 挙動見直し
            // レーンを削除（後ろから削除）
            for (int i = currentLaneCount - 1; i >= requestedLaneCount; i--) {
                entity.getLanes().remove(i);
            }
        }
    }
}

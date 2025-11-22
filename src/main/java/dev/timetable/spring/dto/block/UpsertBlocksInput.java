package dev.timetable.spring.dto.block;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * ブロック作成更新 Input
 */
@Value
@Builder
@Jacksonized
public class UpsertBlocksInput {
    List<BlockInput> blocks;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class BlockInput {
        Long id;
        Long homeroomId;
        String blockName;
        Short laneCount;
    }
}

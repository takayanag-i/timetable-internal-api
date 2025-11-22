package dev.timetable.spring.dto.block;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * ブロック検索 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveBlocksInput {
    Long homeroomId;
    Long id;
}
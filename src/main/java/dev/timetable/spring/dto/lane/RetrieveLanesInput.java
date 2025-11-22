package dev.timetable.spring.dto.lane;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * レーン取得 Input.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetrieveLanesInput {
    /** ブロックID */
    private Long blockId;
    
    /** レーンID */
    private Long id;
}

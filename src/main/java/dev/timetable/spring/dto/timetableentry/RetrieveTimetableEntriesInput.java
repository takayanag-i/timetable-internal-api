package dev.timetable.spring.dto.timetableentry;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 時間割エントリ取得 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveTimetableEntriesInput {

    Long id;
    Long timetableResultId;
}

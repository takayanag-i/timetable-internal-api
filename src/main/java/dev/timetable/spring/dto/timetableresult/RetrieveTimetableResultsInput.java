package dev.timetable.spring.dto.timetableresult;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 時間割編成結果取得 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveTimetableResultsInput {

    Long id;
    UUID ttid;
}

package dev.timetable.spring.dto.homeroom;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 学級検索 Input.
 */
@Value
@Builder
@Jacksonized
public class RetrieveHomeroomsInput {
    UUID ttid;
    Long id;
}
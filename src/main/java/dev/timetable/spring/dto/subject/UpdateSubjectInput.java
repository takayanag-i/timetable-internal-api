package dev.timetable.spring.dto.subject;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 科目-更新APIのリクエストDTO
 * IDがnullの場合は新規作成、IDがある場合は更新として扱う
 */
@Value
@Builder
@Jacksonized
public class UpdateSubjectInput {
    Long id; // nullの場合は新規作成
    String disciplineCode;
    String subjectName;
    Short credits;
    Long gradeId;
}
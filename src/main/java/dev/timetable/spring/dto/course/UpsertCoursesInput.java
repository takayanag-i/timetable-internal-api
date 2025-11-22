package dev.timetable.spring.dto.course;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * 講座同期APIのInputDTO
 */
@Value
@Builder
@Jacksonized
public class UpsertCoursesInput {
    List<CourseInput> courses;
    String by;

    @Value
    @Builder
    @Jacksonized
    public static class CourseInput {
        Long id;
        Long subjectId;
        String courseName;
        List<CourseDetailInput> courseDetails;

        @Value
        @Builder
        @Jacksonized
        public static class CourseDetailInput {
            Long id;
            Long instructorId;
            Long roomId;
        }
    }
}

package dev.timetable.spring.dto.schoolday;

import lombok.Builder;
import lombok.Data;

/**
 * 学校曜日更新入力.
 */
@Data
@Builder
public class UpdateSchoolDayInput {
    /** ID（新規の場合はnull） */
    private Long schoolDayId;
    
    /** 曜日 */
    private String dayOfWeek;
    
    /** 利用可能フラグ */
    private Boolean isAvailable;
    
    /** 午前時限数 */
    private Short amPeriods;
    
    /** 午後時限数 */
    private Short pmPeriods;
}

package dev.timetable.spring.domain.entity;


import java.time.OffsetDateTime;
import java.util.List;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * 勤怠曜日Entity.
 */
@Entity
@Table(name = "ATTENDANCE_DAY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDayEntity {

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 曜日 */
    @Column(nullable = false)
    private String dayOfWeek;

    /** 不可時限リスト */
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<Short> unavailablePeriods;
    
    /** 教員Entity（教員IDで紐づく） */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instructor_id", nullable = false)
    private InstructorEntity instructor;

    /** 作成日時 */
    @Column(nullable = false)
    private OffsetDateTime createdAt;
    
    /** 作成者 */
    @Column(nullable = false)
    private String createdBy;
    
    /** 更新日時 */
    @Column(nullable = false)
    private OffsetDateTime updatedAt;
    
    /** 更新者 */
    @Column(nullable = false)
    private String updatedBy;
}
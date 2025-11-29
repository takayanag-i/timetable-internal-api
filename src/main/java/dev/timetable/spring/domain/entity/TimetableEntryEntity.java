package dev.timetable.spring.domain.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 時間割エントリEntity.
 */
@Entity
@Table(name = "TIMETABLE_ENTRY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
    name = "timetable_entry_id_seq_gen",
    sequenceName = "TIMETABLE_ENTRY_ID_SEQ",
    allocationSize = 1
)
public class TimetableEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timetable_entry_id_seq_gen")
    private Long id;

    /** 時間割編成結果 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "timetable_result_id", nullable = false)
    private TimetableResultEntity timetableResult;

    /** 学級 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "homeroom_id", nullable = false)
    private HomeroomEntity homeroom;

    /** 曜日 */
    @Column(nullable = false)
    private String dayOfWeek;

    /** 時限 */
    @Column(nullable = false)
    private Short period;

    /** 講座 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

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

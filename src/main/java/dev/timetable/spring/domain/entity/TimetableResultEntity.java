package dev.timetable.spring.domain.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 時間割編成結果Entity.
 */
@Entity
@Table(name = "TIMETABLE_RESULT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
    name = "timetable_result_id_seq_gen",
    sequenceName = "TIMETABLE_RESULT_ID_SEQ",
    allocationSize = 1
)
public class TimetableResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timetable_result_id_seq_gen")
    private Long id;

    /** 時間割ID */
    @Column(nullable = false)
    private UUID ttid;

    /** 時間割エントリリスト */
    @OneToMany(mappedBy = "timetableResult", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TimetableEntryEntity> timetableEntries = new ArrayList<>();

    /** 制約違反リスト */
    @OneToMany(mappedBy = "timetableResult", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ConstraintViolationEntity> constraintViolations = new ArrayList<>();

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

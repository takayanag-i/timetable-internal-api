package dev.timetable.spring.domain.entity;

import java.time.OffsetDateTime;

import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;
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
 * 制約違反Entity.
 */
@Entity
@Table(name = "CONSTRAINT_VIOLATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
    name = "constraint_violation_id_seq_gen",
    sequenceName = "CONSTRAINT_VIOLATION_ID_SEQ",
    allocationSize = 1
)
public class ConstraintViolationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "constraint_violation_id_seq_gen")
    private Long id;

    /** 時間割編成結果 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "timetable_result_id", nullable = false)
    private TimetableResultEntity timetableResult;

    /** 制約違反コード */
    @Column(nullable = false)
    private String constraintViolationCode;

    /** 違反キー */
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = true)
    private Object violatingKeys;

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

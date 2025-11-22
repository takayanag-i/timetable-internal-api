package dev.timetable.spring.domain.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CONSTRAINT_DEFINITION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
    name = "constraint_definition_id_seq_gen",
    sequenceName = "CONSTRAINT_DEFINITION_ID_SEQ",
    allocationSize = 1
)
public class ConstraintDefinitionEntity {

    /**
     * 制約定義ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "constraint_definition_id_seq_gen")
    private Long id;

    /** 時間割ID */
    @Column(nullable = false)
    private UUID ttid;

    /** 制約定義コード */
    @Column(nullable = false)
    private String constraintDefinitionCode;

    /** ソフトフラグ */
    @Column(nullable = false)
    private Boolean softFlag;

    /** ペナルティ重み */
    @Column(nullable = true)
    private Double penaltyWeight;

    /** パラメータ */
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = true)
    private Object parameters;

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

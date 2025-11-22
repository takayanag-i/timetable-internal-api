package dev.timetable.spring.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 制約定義マスタEntity.
 */
@Entity
@Table(name = "CONSTRAINT_DEFINITION_MASTER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstraintDefinitionMasterEntity {

    /** 制約定義コード */
    @Id
    @Column(name = "constraint_definition_code")
    private String constraintDefinitionCode;

    /** 制約定義名 */
    @Column(name = "constraint_definition_name", nullable = false)
    private String constraintDefinitionName;

    /** 説明 */
    @Column(name = "description", length = 1000)
    private String description;

    /** 必須フラグ */
    @Column(name = "mandatory_flag", nullable = false)
    private Boolean mandatoryFlag;

    /** ソフトフラグ */
    @Column(name = "soft_flag", nullable = false)
    private Boolean softFlag;
}

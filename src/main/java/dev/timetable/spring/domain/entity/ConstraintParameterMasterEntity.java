package dev.timetable.spring.domain.entity;

import java.io.Serializable;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 制約パラメータマスタEntity.
 */
@Entity
@Table(name = "CONSTRAINT_PARAMETER_MASTER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstraintParameterMasterEntity {

    /** 複合主キー */
    @EmbeddedId
    private ConstraintParameterMasterId id;

    /** パラメータ名 */
    @Column(name = "parameter_name", nullable = false)
    private String parameterName;

    /** パラメータ型 */
    @Column(name = "parameter_type", nullable = false, length = 50)
    private String parameterType;

    /** オプションリスト */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "option_list", columnDefinition = "jsonb")
    private String optionList;

    /**
     * 制約パラメータマスタ複合主キー.
     */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Builder
    public static class ConstraintParameterMasterId implements Serializable {

        /** 制約定義コード */
        @Column(name = "constraint_definition_code")
        private String constraintDefinitionCode;

        /** パラメータキー */
        @Column(name = "parameter_key")
        private String parameterKey;
    }
}

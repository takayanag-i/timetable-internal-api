package dev.timetable.spring.domain.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
 * ブロックEntity.
 */
@Entity
@Table(name = "BLOCK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
    name = "block_id_seq_gen",
    sequenceName = "BLOCK_ID_SEQ",
    allocationSize = 1
)
public class BlockEntity {

    /** ブロックID */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "block_id_seq_gen")
    private Long id;

    /** 学級ID */
    @Column(nullable = false)
    private Long homeroomId;

    /** ブロック名 */
    @Column(nullable = false)
    private String blockName;

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

    /**
     * レーンEntity
     */
    @OneToMany(mappedBy = "block", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    @Builder.Default
    private List<LaneEntity> lanes = new ArrayList<>();
}
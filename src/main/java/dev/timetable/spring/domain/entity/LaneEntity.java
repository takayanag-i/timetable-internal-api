package dev.timetable.spring.domain.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * レーンEntity.
 */
@Entity
@Table(name = "LANE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
    name = "lane_id_seq_gen",
    sequenceName = "LANE_ID_SEQ",
    allocationSize = 1
)
public class LaneEntity {

    /** レーンID */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lane_id_seq_gen")
    private Long id;

    /** ブロックEntity */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "block_id", nullable = false)
    private BlockEntity block;

    /** ブロックID (読み取り専用) */
    @Column(name = "block_id", nullable = false, insertable = false, updatable = false)
    private Long blockId;

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
     * 講座Entityリスト
     * 
     * 中間テーブル「LANE_COURSE」で紐づく。
     */
    @ManyToMany
    @JoinTable(name = "LANE_COURSE", joinColumns = @JoinColumn(name = "lane_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @Builder.Default
    private List<CourseEntity> courses = new ArrayList<>();
}
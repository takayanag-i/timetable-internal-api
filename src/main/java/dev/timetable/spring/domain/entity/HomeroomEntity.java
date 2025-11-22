package dev.timetable.spring.domain.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 学級Entity.
 */
@Entity
@Table(name = "HOMEROOM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
    name = "homeroom_id_seq_gen",
    sequenceName = "HOMEROOM_ID_SEQ",
    allocationSize = 1
)
public class HomeroomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "homeroom_id_seq_gen")
    private Long id;

    @Column(nullable = false)
    private UUID ttid;

    @Column(nullable = false)
    private String homeroomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private GradeEntity grade;

    @OneToMany(mappedBy = "homeroom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HomeroomDayEntity> homeroomDays  = new ArrayList<>();

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @Column(nullable = false)
    private String updatedBy;

    /*
     * ブロックは本エンティティに保持しない
     */
}

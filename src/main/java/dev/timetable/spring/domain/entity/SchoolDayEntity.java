package dev.timetable.spring.domain.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

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

/**
 * 学校曜日Entity.
 */
@Entity
@Table(name = "SCHOOL_DAY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
    name = "school_day_id_seq_gen",
    sequenceName = "SCHOOL_DAY_ID_SEQ",
    allocationSize = 1
)
public class SchoolDayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_day_id_seq_gen")
    private Long id;

    @Column(nullable = false)
    private UUID ttid;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private Boolean isAvailable;

    @Column
    private Short amPeriods;
    
    @Column
    private Short pmPeriods;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @Column(nullable = false)
    private String updatedBy;
}

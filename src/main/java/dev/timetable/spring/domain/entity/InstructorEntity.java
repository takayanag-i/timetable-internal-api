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
 * 教員Entity.
 */
@Entity
@Table(name = "INSTRUCTOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
    name = "instructor_id_seq_gen",
    sequenceName = "INSTRUCTOR_ID_SEQ",
    allocationSize = 1
)
public class InstructorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instructor_id_seq_gen")
    private Long id;

    @Column(nullable = false)
    private UUID ttid;

    @Column(nullable = false)
    private String instructorName;

    @Column(name = "discipline_code", nullable = false)
    private String disciplineCode;

    @OneToMany(mappedBy = "instructor",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @Builder.Default
    private List<AttendanceDayEntity> attendanceDays = new ArrayList<>();

    @OneToMany(mappedBy = "instructor",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @Builder.Default
    private List<CourseDetailEntity> courseDetails = new ArrayList<>();

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @Column(nullable = false)
    private String updatedBy;
}
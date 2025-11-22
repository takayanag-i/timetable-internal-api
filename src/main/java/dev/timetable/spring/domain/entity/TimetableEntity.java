package dev.timetable.spring.domain.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 時間割Entity.
 */
@Entity
@Table(name = "TIMETABLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimetableEntity {

    @Id
    private UUID ttid;

    @OneToMany(mappedBy = "ttid",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @Builder.Default
    private List<HomeroomEntity> homerooms = new ArrayList<>();

    @OneToMany(mappedBy = "ttid",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @Builder.Default
    private List<InstructorEntity> instructors = new ArrayList<>();

    @OneToMany(mappedBy = "ttid",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @Builder.Default
    private List<RoomEntity> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "ttid",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @Builder.Default
    private List<SubjectEntity> subjects = new ArrayList<>();

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @Column(nullable = false)
    private String updatedBy;
}
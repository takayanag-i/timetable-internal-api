package dev.timetable.spring.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.DisciplineEntity;
import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.domain.entity.TimetableEntryEntity;
import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.dto.timetableentry.RetrieveTimetableEntriesInput;
import dev.timetable.spring.dto.timetableentry.UpsertTimetableEntriesInput;
import dev.timetable.spring.dto.timetableentry.UpsertTimetableEntriesInput.TimetableEntryInput;
import dev.timetable.spring.repository.CourseRepository;
import dev.timetable.spring.repository.DisciplineRepository;
import dev.timetable.spring.repository.HomeroomRepository;
import dev.timetable.spring.repository.SubjectRepository;
import dev.timetable.spring.repository.TimetableEntryRepository;
import dev.timetable.spring.repository.TimetableResultRepository;

/**
 * TimetableEntryService の単体テスト（DB結合あり）。
 */
@SpringBootTest
@Transactional
class TimetableEntryServiceTest {

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    @Autowired
    private TimetableEntryService timetableEntryService;

    @Autowired
    private TimetableEntryRepository timetableEntryRepository;

    @Autowired
    private TimetableResultRepository timetableResultRepository;

    @Autowired
    private HomeroomRepository homeroomRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    private Long timetableResultId;
    private Long homeroomId;
    private Long courseId;
    private Long timetableEntryId;

    @BeforeEach
    void setUp() {
        OffsetDateTime now = OffsetDateTime.now();

        // Discipline作成
        DisciplineEntity discipline = DisciplineEntity.builder()
            .disciplineCode("TEST_DISC")
            .disciplineName("テスト教科")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        disciplineRepository.saveAndFlush(discipline);

        // Subject作成
        SubjectEntity subject = SubjectEntity.builder()
            .ttid(TEST_TTID)
            .discipline(discipline)
            .subjectName("テスト科目")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        subjectRepository.saveAndFlush(subject);

        // Course作成
        CourseEntity course = CourseEntity.builder()
            .subject(subject)
            .courseName("テスト講座")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        this.courseId = courseRepository.saveAndFlush(course).getId();

        // Homeroom作成
        HomeroomEntity homeroom = HomeroomEntity.builder()
            .ttid(TEST_TTID)
            .homeroomName("1年A組")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        this.homeroomId = homeroomRepository.saveAndFlush(homeroom).getId();

        // TimetableResult作成
        TimetableResultEntity timetableResult = TimetableResultEntity.builder()
            .ttid(TEST_TTID)
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        this.timetableResultId = timetableResultRepository.saveAndFlush(timetableResult).getId();

        // TimetableEntry作成
        TimetableEntryEntity timetableEntry = TimetableEntryEntity.builder()
            .timetableResult(timetableResult)
            .homeroom(homeroom)
            .dayOfWeek("MON")
            .period((short) 1)
            .course(course)
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        this.timetableEntryId = timetableEntryRepository.saveAndFlush(timetableEntry).getId();
    }

    @Test
    void retrieveByTimetableResultId() {
        List<TimetableEntryEntity> result = timetableEntryService.retrieve(
            RetrieveTimetableEntriesInput.builder()
                .timetableResultId(timetableResultId)
                .build()
        );

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(timetableEntryId);
    }

    @Test
    void retrieveById() {
        List<TimetableEntryEntity> result = timetableEntryService.retrieve(
            RetrieveTimetableEntriesInput.builder()
                .id(timetableEntryId)
                .build()
        );

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(timetableEntryId);
    }

    @Test
    void upsert() {
        // Create
        List<TimetableEntryEntity> created = timetableEntryService.upsert(
            UpsertTimetableEntriesInput.builder()
                .timetableResultId(timetableResultId)
                .timetableEntries(List.of(
                    TimetableEntryInput.builder()
                        .homeroomId(homeroomId)
                        .dayOfWeek("TUE")
                        .period(2)
                        .courseId(courseId)
                        .build()
                ))
                .by(TEST_USER)
                .build()
        );

        assertThat(created).hasSize(1);
        TimetableEntryEntity timetableEntry = created.getFirst();
        assertThat(timetableEntry.getDayOfWeek()).isEqualTo("TUE");
        assertThat(timetableEntry.getPeriod()).isEqualTo((short) 2);

        // Update
        List<TimetableEntryEntity> updated = timetableEntryService.upsert(
            UpsertTimetableEntriesInput.builder()
                .timetableResultId(timetableResultId)
                .timetableEntries(List.of(
                    TimetableEntryInput.builder()
                        .id(timetableEntry.getId())
                        .homeroomId(homeroomId)
                        .dayOfWeek("WED")
                        .period(3)
                        .courseId(courseId)
                        .build()
                ))
                .by(TEST_USER)
                .build()
        );

        assertThat(updated).hasSize(1);
        assertThat(updated.getFirst().getDayOfWeek()).isEqualTo("WED");
        assertThat(updated.getFirst().getPeriod()).isEqualTo((short) 3);

        TimetableEntryEntity persisted = timetableEntryRepository.findById(timetableEntry.getId()).orElseThrow();
        assertThat(persisted.getDayOfWeek()).isEqualTo("WED");
        assertThat(persisted.getPeriod()).isEqualTo((short) 3);
    }
}

package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.DisciplineEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.repository.DisciplineRepository;
import dev.timetable.spring.repository.SubjectRepository;

import static org.assertj.core.api.Assertions.*;

/**
 * SubjectService の単体テスト（DB結合あり）。
 */
@SpringBootTest
@Transactional
class SubjectServiceTest {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    private static final UUID TEST_TTID = UUID.fromString("00000000-aaaa-bbbb-cccc-111111111111");
    private static final String TEST_USER = "test-user";

    private long subjectId1;

    @BeforeEach
    void setUp() {
        // Given - 2件登録
        OffsetDateTime now = OffsetDateTime.now();

        // 教科データ準備
        DisciplineEntity mathDiscipline = DisciplineEntity.builder()
            .disciplineCode("MTH")
            .disciplineName("数学")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
            
        DisciplineEntity engDiscipline = DisciplineEntity.builder()
            .disciplineCode("ENG")
            .disciplineName("英語")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();

        disciplineRepository.saveAndFlush(mathDiscipline);
        disciplineRepository.saveAndFlush(engDiscipline);

        // 科目登録
        SubjectEntity existingSubject1 = SubjectEntity.builder()
            .ttid(TEST_TTID)
            .discipline(mathDiscipline)
            .subjectName("1数I")
            .credits((short) 3)
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();

        SubjectEntity existingSubject2 = SubjectEntity.builder()
            .ttid(TEST_TTID)
            .discipline(engDiscipline)
            .subjectName("1英コI")
            .credits((short) 2)
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();

        List<SubjectEntity> savedSubjects = subjectRepository.saveAllAndFlush(List.of(existingSubject1, existingSubject2));

        this.subjectId1 = savedSubjects.get(0).getId();
    }

    @Test
    void retrieveByTtid() {
        // When
        List<SubjectEntity> result = subjectService.retrieve(
            dev.timetable.spring.dto.subject.RetrieveSubjectsInput.builder()
                .ttid(TEST_TTID)
                .build()
        );
        // Then
        assertThat(result.size()).isGreaterThanOrEqualTo(2);
        assertThat(result.stream().map(SubjectEntity::getSubjectName))
            .contains("1数I", "1英コI");
    }

    @Test
    void retrieveById() {
        // When
        List<SubjectEntity> result = subjectService.retrieve(
            dev.timetable.spring.dto.subject.RetrieveSubjectsInput.builder()
                .id(subjectId1)
                .build()
        );
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(subjectId1);
    }
}

package dev.timetable.spring.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.GradeEntity;
import dev.timetable.spring.dto.grade.RetrieveGradesInput;
import dev.timetable.spring.dto.grade.UpsertGradesInput;
import dev.timetable.spring.dto.grade.UpsertGradesInput.GradeInput;
import dev.timetable.spring.repository.GradeRepository;

/**
 * GradeService の単体テスト（DB結合あり）。
 */
@SpringBootTest
@Transactional
class GradeServiceTest {

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    @Autowired
    private GradeService gradeService;

    @Autowired
    private GradeRepository gradeRepository;

    private Long gradeId;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // 共有のテストデータを1件作成
        OffsetDateTime now = OffsetDateTime.now();
        GradeEntity grade = GradeEntity.builder()
            .ttid(TEST_TTID)
            .gradeName("1年A")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        this.gradeId = gradeRepository.saveAndFlush(grade).getId();
    }

    @Test
    void retrieveByTtid() {
        // 異なるTTIDのデータを追加してフィルタ確認
        gradeRepository.save(
            GradeEntity.builder()
                .ttid(UUID.randomUUID())
                .gradeName("別学年")
                .createdAt(OffsetDateTime.now())
                .createdBy(TEST_USER)
                .updatedAt(OffsetDateTime.now())
                .updatedBy(TEST_USER)
                .build()
        );

        List<GradeEntity> result = gradeService.retrieve(
            RetrieveGradesInput.builder()
                .ttid(TEST_TTID)
                .build()
        );

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(gradeId);
    }

    @Test
    void retrieveById() {
        // When
        List<GradeEntity> result = gradeService.retrieve(
            RetrieveGradesInput.builder()
                .id(gradeId)
                .build()
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(gradeId);
    }

    @Test
    void upsert() {
        // Create
        List<GradeEntity> created = gradeService.upsert(
            UpsertGradesInput.builder()
                .ttid(TEST_TTID)
                .grades(List.of(
                    GradeInput.builder()
                        .gradeName("1年")
                        .build()
                ))
                .by(TEST_USER)
                .build()
        );

        assertThat(created).hasSize(1);
        GradeEntity grade = created.getFirst();
        assertThat(grade.getTtid()).isEqualTo(TEST_TTID);
        assertThat(grade.getGradeName()).isEqualTo("1年");

        // Update
        List<GradeEntity> updated = gradeService.upsert(
            UpsertGradesInput.builder()
                .ttid(TEST_TTID)
                .grades(List.of(
                    GradeInput.builder()
                        .id(grade.getId())
                        .gradeName("2年")
                        .build()
                ))
                .by(TEST_USER)
                .build()
        );

        assertThat(updated).hasSize(1);
        assertThat(updated.getFirst().getGradeName()).isEqualTo("2年");
        assertThat(updated.getFirst().getTtid()).isEqualTo(TEST_TTID);

        GradeEntity persisted = gradeRepository.findById(grade.getId()).orElseThrow();
        assertThat(persisted.getGradeName()).isEqualTo("2年");
        assertThat(persisted.getTtid()).isEqualTo(TEST_TTID);
    }
}


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

import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.dto.timetableresult.RetrieveTimetableResultsInput;
import dev.timetable.spring.dto.timetableresult.UpsertTimetableResultsInput;
import dev.timetable.spring.dto.timetableresult.UpsertTimetableResultsInput.TimetableResultInput;
import dev.timetable.spring.repository.TimetableResultRepository;

/**
 * TimetableResultService の単体テスト（DB結合あり）。
 */
@SpringBootTest
@Transactional
class TimetableResultServiceTest {

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    @Autowired
    private TimetableResultService timetableResultService;

    @Autowired
    private TimetableResultRepository timetableResultRepository;

    private Long timetableResultId;

    @BeforeEach
    void setUp() {
        // 共有のテストデータを1件作成
        OffsetDateTime now = OffsetDateTime.now();
        TimetableResultEntity timetableResult = TimetableResultEntity.builder()
            .ttid(TEST_TTID)
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        this.timetableResultId = timetableResultRepository.saveAndFlush(timetableResult).getId();
    }

    @Test
    void retrieveByTtid() {
        // 異なるTTIDのデータを追加してフィルタ確認
        timetableResultRepository.save(
            TimetableResultEntity.builder()
                .ttid(UUID.randomUUID())
                .createdAt(OffsetDateTime.now())
                .createdBy(TEST_USER)
                .updatedAt(OffsetDateTime.now())
                .updatedBy(TEST_USER)
                .build()
        );

        List<TimetableResultEntity> result = timetableResultService.retrieve(
            RetrieveTimetableResultsInput.builder()
                .ttid(TEST_TTID)
                .build()
        );

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(timetableResultId);
    }

    @Test
    void retrieveById() {
        // When
        List<TimetableResultEntity> result = timetableResultService.retrieve(
            RetrieveTimetableResultsInput.builder()
                .id(timetableResultId)
                .build()
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(timetableResultId);
    }

    @Test
    void upsert() {
        // Create
        List<TimetableResultEntity> created = timetableResultService.upsert(
            UpsertTimetableResultsInput.builder()
                .ttid(TEST_TTID)
                .timetableResults(List.of(
                    TimetableResultInput.builder()
                        .build()
                ))
                .by(TEST_USER)
                .build()
        );

        assertThat(created).hasSize(1);
        TimetableResultEntity timetableResult = created.getFirst();
        assertThat(timetableResult.getTtid()).isEqualTo(TEST_TTID);

        // Update
        List<TimetableResultEntity> updated = timetableResultService.upsert(
            UpsertTimetableResultsInput.builder()
                .ttid(TEST_TTID)
                .timetableResults(List.of(
                    TimetableResultInput.builder()
                        .id(timetableResult.getId())
                        .build()
                ))
                .by(TEST_USER)
                .build()
        );

        assertThat(updated).hasSize(1);
        assertThat(updated.getFirst().getTtid()).isEqualTo(TEST_TTID);

        TimetableResultEntity persisted = timetableResultRepository.findById(timetableResult.getId()).orElseThrow();
        assertThat(persisted.getTtid()).isEqualTo(TEST_TTID);
    }
}

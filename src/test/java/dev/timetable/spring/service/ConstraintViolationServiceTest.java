package dev.timetable.spring.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.ConstraintViolationEntity;
import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.dto.constraintviolation.RetrieveConstraintViolationsInput;
import dev.timetable.spring.dto.constraintviolation.UpsertConstraintViolationsInput;
import dev.timetable.spring.dto.constraintviolation.UpsertConstraintViolationsInput.ConstraintViolationInput;
import dev.timetable.spring.repository.ConstraintViolationRepository;
import dev.timetable.spring.repository.TimetableResultRepository;

/**
 * ConstraintViolationService の単体テスト（DB結合あり）。
 */
@SpringBootTest
@Transactional
class ConstraintViolationServiceTest {

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    @Autowired
    private ConstraintViolationService constraintViolationService;

    @Autowired
    private ConstraintViolationRepository constraintViolationRepository;

    @Autowired
    private TimetableResultRepository timetableResultRepository;

    private Long timetableResultId;
    private Long constraintViolationId;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // 時間割編成結果を作成
        OffsetDateTime now = OffsetDateTime.now();
        TimetableResultEntity timetableResult = TimetableResultEntity.builder()
            .ttid(TEST_TTID)
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        this.timetableResultId = timetableResultRepository.saveAndFlush(timetableResult).getId();

        // 制約違反を作成
        ConstraintViolationEntity constraintViolation = ConstraintViolationEntity.builder()
            .timetableResult(timetableResult)
            .constraintViolationCode("TEST_VIOLATION")
            .violatingKeys(null)
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        this.constraintViolationId = constraintViolationRepository.saveAndFlush(constraintViolation).getId();
    }

    @Test
    void retrieveByTimetableResultId() {
        List<ConstraintViolationEntity> result = constraintViolationService.retrieve(
            RetrieveConstraintViolationsInput.builder()
                .timetableResultId(timetableResultId)
                .build()
        );

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(constraintViolationId);
    }

    @Test
    void retrieveById() {
        List<ConstraintViolationEntity> result = constraintViolationService.retrieve(
            RetrieveConstraintViolationsInput.builder()
                .id(constraintViolationId)
                .build()
        );

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(constraintViolationId);
    }

    @Test
    void upsert() {
        // Create
        List<ConstraintViolationEntity> created = constraintViolationService.upsert(
            UpsertConstraintViolationsInput.builder()
                .timetableResultId(timetableResultId)
                .constraintViolations(List.of(
                    ConstraintViolationInput.builder()
                        .constraintViolationCode("NEW_VIOLATION")
                        .build()
                ))
                .by(TEST_USER)
                .build()
        );

        assertThat(created).hasSize(1);
        ConstraintViolationEntity constraintViolation = created.getFirst();
        assertThat(constraintViolation.getConstraintViolationCode()).isEqualTo("NEW_VIOLATION");

        // Update
        List<ConstraintViolationEntity> updated = constraintViolationService.upsert(
            UpsertConstraintViolationsInput.builder()
                .timetableResultId(timetableResultId)
                .constraintViolations(List.of(
                    ConstraintViolationInput.builder()
                        .id(constraintViolation.getId())
                        .constraintViolationCode("UPDATED_VIOLATION")
                        .build()
                ))
                .by(TEST_USER)
                .build()
        );

        assertThat(updated).hasSize(1);
        assertThat(updated.getFirst().getConstraintViolationCode()).isEqualTo("UPDATED_VIOLATION");

        ConstraintViolationEntity persisted = constraintViolationRepository.findById(constraintViolation.getId()).orElseThrow();
        assertThat(persisted.getConstraintViolationCode()).isEqualTo("UPDATED_VIOLATION");
    }
}

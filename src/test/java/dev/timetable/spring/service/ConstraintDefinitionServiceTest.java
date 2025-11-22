package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.ConstraintDefinitionEntity;
import dev.timetable.spring.dto.constraintdefinition.RetrieveConstraintDefinitionsInput;
import dev.timetable.spring.dto.constraintdefinition.UpsertConstraintDefinitionsInput;
import dev.timetable.spring.dto.constraintdefinition.UpsertConstraintDefinitionsInput.ConstraintDefinitionInput;
import dev.timetable.spring.repository.ConstraintDefinitionRepository;

import static org.assertj.core.api.Assertions.*;

/**
 * ConstraintDefinitionService の単体テスト（DBとの結合あり）.
 */
@SpringBootTest
@Transactional
class ConstraintDefinitionServiceTest {

    @Autowired
    private ConstraintDefinitionService constraintDefinitionService;

    @Autowired
    private ConstraintDefinitionRepository constraintDefinitionRepository;

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    private Long constraintDefinitionId;

    @BeforeEach
    void setUp() {
        // Repositoryクラスを使って直接テストデータを準備
        OffsetDateTime now = OffsetDateTime.now();
        
        ConstraintDefinitionEntity constraintDefinition = ConstraintDefinitionEntity.builder()
            .ttid(TEST_TTID)
            .constraintDefinitionCode("1")
            .softFlag(false)
            .penaltyWeight(1.0)
            .parameters(List.of((short) 10, (short) 20))
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        
        // Repositoryで保存
        ConstraintDefinitionEntity savedEntity = constraintDefinitionRepository.save(constraintDefinition);
        this.constraintDefinitionId = savedEntity.getId();
    }

    @Test
    void retrieveByTtid() {
        // When
        List<ConstraintDefinitionEntity> result = constraintDefinitionService.retrieve(
            RetrieveConstraintDefinitionsInput.builder()
                .ttid(TEST_TTID)
                .build()
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getConstraintDefinitionCode()).isEqualTo("1");
        assertThat(result.get(0).getSoftFlag()).isFalse();
        assertThat(result.get(0).getPenaltyWeight()).isEqualTo(1.0);
        assertThat((List<?>) result.get(0).getParameters()).hasSize(2);
    }

    @Test
    void retrieveById() {
        // When
        List<ConstraintDefinitionEntity> result = constraintDefinitionService.retrieve(
            RetrieveConstraintDefinitionsInput.builder()
                .ttid(TEST_TTID)
                .id(constraintDefinitionId)
                .build()
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(constraintDefinitionId);
    }

    @Test
    void upsert() {
        // Given
        UpsertConstraintDefinitionsInput upsertRequest = UpsertConstraintDefinitionsInput.builder()
            .ttid(TEST_TTID)
            .by("batch-user")
            .constraintDefinitions(List.of(
                ConstraintDefinitionInput.builder()
                    .id(constraintDefinitionId) // 既存ID = 更新
                    .constraintDefinitionCode("10")
                    .softFlag(false)
                    .penaltyWeight(3.0)
                    .parameters(List.of((short) 1))
                    .build(),
                ConstraintDefinitionInput.builder()
                    .id(null) // ID無し = 新規作成
                    .constraintDefinitionCode("20")
                    .softFlag(true)
                    .penaltyWeight(4.0)
                    .parameters(List.of((short) 2, (short) 3))
                    .build()
            ))
            .build();

        // When
        List<ConstraintDefinitionEntity> resultEntities = constraintDefinitionService.upsert(upsertRequest);

        // Then
        assertThat(resultEntities).hasSize(2);
        
        // 全件確認
        List<ConstraintDefinitionEntity> allEntities = constraintDefinitionRepository.findByTtid(TEST_TTID);
        assertThat(allEntities).hasSize(2);
    }

    @Test
    void delete() {
        // Given
        // When
        constraintDefinitionService.deleteById(constraintDefinitionId);

        // Then
        List<ConstraintDefinitionEntity> result = constraintDefinitionRepository.findByTtid(TEST_TTID);
        assertThat(result).hasSize(0);
    }
}

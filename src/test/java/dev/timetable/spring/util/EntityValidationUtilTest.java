package dev.timetable.spring.util;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * EntityValidationUtilのテスト.
 */
class EntityValidationUtilTest {

    @Test
    void 全てのエンティティが存在する場合_例外がスローされない() {
        // Given
        List<Long> requestedIds = Arrays.asList(1L, 2L, 3L);
        List<TestEntity> foundEntities = Arrays.asList(
            new TestEntity(1L),
            new TestEntity(2L),
            new TestEntity(3L)
        );

        // When & Then - 例外がスローされないことを確認
        assertThatCode(() -> 
            EntityValidationUtil.validateAllEntitiesExist(
                requestedIds,
                foundEntities,
                TestEntity::getId,
                "テストエンティティ"
            )
        ).doesNotThrowAnyException();
    }

    @Test
    void 一部のエンティティが存在しない場合_例外がスローされる() {
        // Given
        List<Long> requestedIds = Arrays.asList(1L, 2L, 3L);
        List<TestEntity> foundEntities = Arrays.asList(
            new TestEntity(1L),
            new TestEntity(2L)
            // ID=3のエンティティが存在しない
        );

        // When & Then
        assertThatThrownBy(() -> 
            EntityValidationUtil.validateAllEntitiesExist(
                requestedIds,
                foundEntities,
                TestEntity::getId,
                "テストエンティティ"
            )
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("存在しないテストエンティティ")
        .hasMessageContaining("3");
    }

    @Test
    void 複数のエンティティが存在しない場合_全ての欠落IDが含まれる() {
        // Given
        List<Long> requestedIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<TestEntity> foundEntities = Arrays.asList(
            new TestEntity(1L),
            new TestEntity(3L)
            // ID=2, 4, 5のエンティティが存在しない
        );

        // When & Then
        assertThatThrownBy(() -> 
            EntityValidationUtil.validateAllEntitiesExist(
                requestedIds,
                foundEntities,
                TestEntity::getId,
                "テストエンティティ"
            )
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("存在しないテストエンティティ")
        .hasMessageContaining("2")
        .hasMessageContaining("4")
        .hasMessageContaining("5");
    }

    @Test
    void 空のリクエストIDリストの場合_例外がスローされない() {
        // Given
        List<Long> requestedIds = Arrays.asList();
        List<TestEntity> foundEntities = Arrays.asList();

        // When & Then
        assertThatCode(() -> 
            EntityValidationUtil.validateAllEntitiesExist(
                requestedIds,
                foundEntities,
                TestEntity::getId,
                "テストエンティティ"
            )
        ).doesNotThrowAnyException();
    }

    // テスト用の簡易エンティティクラス
    private static class TestEntity {
        private final Long id;

        public TestEntity(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}

package dev.timetable.spring.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * EntityMapUtilのテスト.
 */
class EntityMapUtilTest {

    @Test
    void エンティティのコレクションをMapに変換できる() {
        // Given
        List<TestEntity> entities = Arrays.asList(
            new TestEntity(1L, "Entity1"),
            new TestEntity(2L, "Entity2"),
            new TestEntity(3L, "Entity3")
        );

        // When
        Map<Long, TestEntity> result = EntityMapUtil.toMap(
            entities,
            TestEntity::getId
        );

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(1L).getName()).isEqualTo("Entity1");
        assertThat(result.get(2L).getName()).isEqualTo("Entity2");
        assertThat(result.get(3L).getName()).isEqualTo("Entity3");
    }

    @Test
    void 空のコレクションの場合_空のMapを返す() {
        // Given
        List<TestEntity> entities = Arrays.asList();

        // When
        Map<Long, TestEntity> result = EntityMapUtil.toMap(
            entities,
            TestEntity::getId
        );

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void 文字列キーでのMapへの変換も可能() {
        // Given
        List<TestEntity> entities = Arrays.asList(
            new TestEntity(1L, "Alpha"),
            new TestEntity(2L, "Beta"),
            new TestEntity(3L, "Gamma")
        );

        // When
        Map<String, TestEntity> result = EntityMapUtil.toMap(
            entities,
            TestEntity::getName
        );

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get("Alpha").getId()).isEqualTo(1L);
        assertThat(result.get("Beta").getId()).isEqualTo(2L);
        assertThat(result.get("Gamma").getId()).isEqualTo(3L);
    }

    // テスト用の簡易エンティティクラス
    private static class TestEntity {
        private final Long id;
        private final String name;

        public TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}

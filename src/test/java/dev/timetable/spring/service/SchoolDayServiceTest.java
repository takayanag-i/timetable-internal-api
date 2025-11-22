package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.SchoolDayEntity;
import dev.timetable.spring.dto.schoolday.RetrieveSchoolDaysInput;
import dev.timetable.spring.dto.schoolday.UpsertSchoolDaysInput;
import dev.timetable.spring.dto.schoolday.UpsertSchoolDaysInput.SchoolDayInput;
import dev.timetable.spring.repository.SchoolDayRepository;

import static org.assertj.core.api.Assertions.*;

/**
 * 学校曜日Serviceの単体テスト.
 */
@SpringBootTest
@Transactional
class SchoolDayServiceTest {

    @Autowired
    private SchoolDayService schoolDayService;

    @Autowired
    private SchoolDayRepository schoolDayRepository;

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    private Long givenId;

    @BeforeEach
    void setUp() {
        // Given
        // 学校曜日を作成
        OffsetDateTime now = OffsetDateTime.now();

        SchoolDayEntity given = SchoolDayEntity.builder()
            .ttid(TEST_TTID)
            .dayOfWeek("mon")
            .isAvailable(true)
            .amPeriods((short) 4)
            .pmPeriods((short) 2)
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();

        schoolDayRepository.saveAndFlush(given);
        this.givenId = given.getId();
    }

    @Test
    void retrieveByTtid() {
        // When
        List<SchoolDayEntity> results = schoolDayService.retrieve(
            RetrieveSchoolDaysInput.builder()
                .ttid(TEST_TTID)
                .id(null)
                .build()
        );

        // Then
        assertThat(results).hasSize(1);
        assertThat(results)
            .extracting(SchoolDayEntity::getDayOfWeek)
            .containsExactlyInAnyOrder("mon");
    }

    @Test
    void retrieveById() {
        // When
        List<SchoolDayEntity> results = schoolDayService.retrieve(
            RetrieveSchoolDaysInput.builder()
                .id(givenId)
                .build()
        );

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getId()).isEqualTo(givenId);
    }

    @Test
    void upsert() {
        // When
        UpsertSchoolDaysInput upsertRequest = UpsertSchoolDaysInput.builder()
            .ttid(TEST_TTID)
            .schoolDays(List.of(
                SchoolDayInput.builder()
                    .schoolDayId(givenId) // 既存IDあり = 更新
                    .dayOfWeek("mon")
                    .isAvailable(true)
                    .amPeriods((short) 3) // 変更
                    .pmPeriods((short) 2)
                    .build(),
                SchoolDayInput.builder()
                    .schoolDayId(null) // 新規作成
                    .dayOfWeek("tue")
                    .isAvailable(false)
                    .amPeriods((short) 2)
                    .pmPeriods((short) 3)
                    .build()
            ))
            .by(TEST_USER)
            .build();

        List<SchoolDayEntity> response = schoolDayService.upsert(upsertRequest);

        // Then
        // 戻り値の確認
        assertThat(response).hasSize(2);
        // 新規作成した学校曜日の内容が正しいこと
        SchoolDayEntity createdResponse = response.stream()
            .filter(s -> "tue".equals(s.getDayOfWeek()))
            .findFirst()
            .orElseThrow();
        assertThat(createdResponse.getIsAvailable()).isEqualTo(false);
        assertThat(createdResponse.getAmPeriods()).isEqualTo((short) 2);
        assertThat(createdResponse.getPmPeriods()).isEqualTo((short) 3);
        // 更新した学校曜日の内容が正しいこと
        SchoolDayEntity updatedResonse = response.stream()
            .filter(s -> "mon".equals(s.getDayOfWeek()))
            .findFirst()
            .orElseThrow();
        assertThat(updatedResonse.getAmPeriods()).isEqualTo((short) 3);

        // DBの確認
        // 件数が2件であること
        List<SchoolDayEntity> entities = schoolDayRepository.findByTtid(TEST_TTID);
        assertThat(entities).hasSize(2);
        // 新規作成した学校曜日の内容が正しいこと
        SchoolDayEntity createdEntity = entities.stream()
            .filter(s -> "tue".equals(s.getDayOfWeek()))
            .findFirst()
            .orElseThrow();
        assertThat(createdEntity.getIsAvailable()).isEqualTo(false);
        assertThat(createdEntity.getAmPeriods()).isEqualTo((short) 2);
        assertThat(createdEntity.getPmPeriods()).isEqualTo((short) 3);
        // 更新した学校曜日の内容が正しいこと
        SchoolDayEntity updatedEntity = entities.stream()
            .filter(s -> "mon".equals(s.getDayOfWeek()))
            .findFirst()
            .orElseThrow();
        assertThat(updatedEntity.getAmPeriods()).isEqualTo((short) 3);
    }
}
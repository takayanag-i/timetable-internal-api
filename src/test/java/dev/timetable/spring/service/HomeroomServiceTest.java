package dev.timetable.spring.service;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.repository.HomeroomRepository;
import dev.timetable.spring.dto.homeroom.RetrieveHomeroomsInput;
import dev.timetable.spring.dto.homeroom.UpsertHomeroomsInput;
import dev.timetable.spring.dto.homeroom.UpsertHomeroomsInput.HomeroomInput;

import static org.assertj.core.api.Assertions.*;

/**
 * HomeroomService の単体テスト（DB結合あり）。
 */
@SpringBootTest
@Transactional
class HomeroomServiceTest {

    @Autowired
    private HomeroomService homeroomService;

    @Autowired
    private HomeroomRepository homeroomRepository;

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    private Long givenId;

    @BeforeEach
    void setUp() {
        // Given
        // 学級作成
        HomeroomEntity given = homeroomService.upsert(UpsertHomeroomsInput.builder()
            .ttid(TEST_TTID)
            .by(TEST_USER)
            .homerooms(List.of(
                HomeroomInput.builder()
                    .id(null)
                    .homeroomName("1-1")
                    .homeroomDays(List.of(
                        HomeroomInput.HomeroomDayInput.builder()
                            .id(null)
                            .dayOfWeek("mon")
                            .periods((short) 6)
                            .build()
                    ))
                    .build()
            ))
            .build())
            .getFirst();
        this.givenId = given.getId();

        // ブロック作成

    }

    @Test
    void retrieveByTtid() {
        // When
        RetrieveHomeroomsInput input = RetrieveHomeroomsInput.builder()
            .ttid(TEST_TTID)
            .build();

        List<HomeroomEntity> result = homeroomService.retrieve(input);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get(0).getHomeroomName()).isEqualTo("1-1");
        assertThat(result.get(0).getHomeroomDays()).hasSize(1);
        assertThat(result.get(0).getHomeroomDays().get(0).getDayOfWeek()).isEqualTo("mon");
    }

    @Test
    void retrieveById() {
        // When
        RetrieveHomeroomsInput input = RetrieveHomeroomsInput.builder()
            .id(givenId)
            .build();

        List<HomeroomEntity> result = homeroomService.retrieve(input);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(givenId);
    }
    @Test
    void upsert() {
        // When
        UpsertHomeroomsInput syncRequest = UpsertHomeroomsInput.builder()
            .ttid(TEST_TTID)
            .by(TEST_USER)
            .homerooms(List.of(
                HomeroomInput.builder()
                    .id(null) // 作成
                    .homeroomName("2-1")
                    .homeroomDays(List.of(
                        HomeroomInput.HomeroomDayInput.builder()
                            .id(null)
                            .dayOfWeek("tue")
                            .periods((short) 5)
                            .build()
                    ))
                    .build(),
                HomeroomInput.builder()
                    .id(givenId) // 更新
                    .homeroomName("1-2")
                    .homeroomDays(List.of(
                        HomeroomInput.HomeroomDayInput.builder()
                            .id(null)
                            .dayOfWeek("wed")
                            .periods((short) 6)
                            .build()
                    ))
                    .build()
            ))
            .build();

        List<HomeroomEntity> res = homeroomService.upsert(syncRequest);

        // Then
        // 戻り値の確認
        assertThat(res).hasSize(2);
        assertThat(res.stream().map(HomeroomEntity::getHomeroomName).toList())
            .contains("2-1", "1-2");
        
        // DBの確認
        List<HomeroomEntity> entities = homeroomRepository.findByTtid(TEST_TTID);
        // 件数が2件であること
        assertThat(entities).hasSize(2);
        // 作成した学級が存在すること
        assertThat(entities.stream().map(HomeroomEntity::getHomeroomName).toList())
            .contains("2-1");
        // 更新した学級の学級名が更新されていること
        assertThat(entities.stream().map(HomeroomEntity::getHomeroomName).toList())
            .contains("1-2");
    }
}

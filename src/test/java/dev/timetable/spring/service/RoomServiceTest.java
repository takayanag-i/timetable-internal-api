package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.RoomEntity;
import dev.timetable.spring.repository.RoomRepository;

import static org.assertj.core.api.Assertions.*;

/**
 * RoomService の単体テスト（DB結合あり）。
 */
@SpringBootTest
@Transactional
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    private long roomId1;

    @BeforeEach
    void setUp() {
        // Given - 2件登録
        OffsetDateTime now = OffsetDateTime.now();

        // 教室登録
        RoomEntity existingRoom1 = RoomEntity.builder()
            .ttid(TEST_TTID)
            .roomName("101教室")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();

        RoomEntity existingRoom2 = RoomEntity.builder()
            .ttid(TEST_TTID)
            .roomName("102教室")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();

        List<RoomEntity> savedRooms = roomRepository.saveAllAndFlush(List.of(existingRoom1, existingRoom2));

        this.roomId1 = savedRooms.get(0).getId();
    }
    @Test
    void retrieveByTtid() {
        // When
        List<RoomEntity> result = roomService.retrieve(
            dev.timetable.spring.dto.room.RetrieveRoomsInput.builder()
                .ttid(TEST_TTID)
                .build()
        );
        // Then
        assertThat(result.size()).isGreaterThanOrEqualTo(2);
        assertThat(result.stream().map(RoomEntity::getRoomName))
            .contains("101教室", "102教室");
    }

    @Test
    void retrieveById() {
        // When
        List<RoomEntity> result = roomService.retrieve(
            dev.timetable.spring.dto.room.RetrieveRoomsInput.builder()
                .id(roomId1)
                .build()
        );
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(roomId1);
    }
}

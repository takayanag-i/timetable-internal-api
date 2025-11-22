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

import dev.timetable.spring.domain.entity.BlockEntity;
import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.DisciplineEntity;
import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.domain.entity.LaneEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.dto.lane.UpsertLanesInput;
import dev.timetable.spring.repository.BlockRepository;
import dev.timetable.spring.repository.CourseRepository;
import dev.timetable.spring.repository.DisciplineRepository;
import dev.timetable.spring.repository.HomeroomRepository;
import dev.timetable.spring.repository.LaneRepository;
import dev.timetable.spring.repository.SubjectRepository;

/**
 * LaneService の単体テスト.
 */
@SpringBootTest
@Transactional
class LaneServiceTest {

    @Autowired
    private LaneService laneService;

    @Autowired
    private LaneRepository laneRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private HomeroomRepository homeroomRepository;

    @Autowired
    private BlockRepository blockRepository;

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    private Long givenId;
    private CourseEntity course1;
    private CourseEntity course2;

    @BeforeEach
    void setUp() {
        // Given
        // 教科を作成
        DisciplineEntity discipline = DisciplineEntity.builder()
            .disciplineCode("MTH")
            .disciplineName("数学")
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        disciplineRepository.save(discipline);

        // 学級を作成
        HomeroomEntity homeroom = HomeroomEntity.builder()
            .homeroomName("1-1")
            .ttid(TEST_TTID)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        homeroomRepository.save(homeroom);

        // ブロックを作成
        BlockEntity block = BlockEntity.builder()
            .blockName("1-1ブロック1")
            .homeroomId(homeroom.getId())
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        blockRepository.save(block);

        // レーンを作成
        LaneEntity lane = LaneEntity.builder()
            .block(block)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        laneRepository.save(lane);

        this.givenId = lane.getId();

        // 科目を作成
        SubjectEntity subject1 = SubjectEntity.builder()
            .subjectName("数学Ⅰ")
            .ttid(TEST_TTID)
            .credits((short) 3)
            .discipline(discipline)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        subjectRepository.save(subject1);

        SubjectEntity subject2 = SubjectEntity.builder()
            .subjectName("数学Ａ")
            .ttid(TEST_TTID)
            .credits((short) 4)
            .discipline(discipline)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        subjectRepository.save(subject2);

        // 講座を作成
        course1 = CourseEntity.builder()
            .courseName("1数Ⅰ1")
            .subject(subject1)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        courseRepository.save(course1);

        course2 = CourseEntity.builder()
            .courseName("1数Ａ1")
            .subject(subject2)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        courseRepository.save(course2);
    }

    @Test
    void retrieveByBlockId() {
        // When
        // 事前に setUp でレーン作成済み
        BlockEntity anyBlock = blockRepository.findAll().get(0);
        var result = laneService.retrieve(
            dev.timetable.spring.dto.lane.RetrieveLanesInput.builder()
                .blockId(anyBlock.getId())
                .build()
        );
        // Then
        assertThat(result.size()).isGreaterThanOrEqualTo(1);
        assertThat(result.stream().map(LaneEntity::getBlock).map(BlockEntity::getId))
            .contains(anyBlock.getId());
    }

    @Test
    void retrieveById() {
        // When
        var result = laneService.retrieve(
            dev.timetable.spring.dto.lane.RetrieveLanesInput.builder()
                .id(givenId)
                .build()
        );
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(givenId);
    }
    
    @Test
    void upsert() {
        // When
        UpsertLanesInput updateLaneRequest = UpsertLanesInput.builder()
            .by(TEST_USER)
            .lanes(List.of(
                UpsertLanesInput.LaneInput.builder()
                    .id(givenId)
                    .courseIds(List.of(course1.getId(), course2.getId()))
                    .build()
            ))
            .build();
            
        laneService.upsert(updateLaneRequest);

        // Then
        LaneEntity updatedLane = laneRepository.findById(givenId).orElseThrow();

        // レーンに講座が紐づいていることを確認
        assertThat(updatedLane.getCourses()).hasSize(2);
        assertThat(updatedLane.getCourses())
            .extracting(CourseEntity::getId)
            .containsExactlyInAnyOrder(course1.getId(), course2.getId());
    }
}

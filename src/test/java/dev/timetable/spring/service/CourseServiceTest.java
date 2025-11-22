package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.DisciplineEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.domain.entity.LaneEntity;
import dev.timetable.spring.domain.entity.BlockEntity;
import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.repository.CourseRepository;
import dev.timetable.spring.repository.DisciplineRepository;
import dev.timetable.spring.repository.SubjectRepository;
import dev.timetable.spring.repository.LaneRepository;
import dev.timetable.spring.repository.BlockRepository;
import dev.timetable.spring.repository.HomeroomRepository;
import dev.timetable.spring.dto.course.RetrieveCoursesInput;
import dev.timetable.spring.dto.course.UpsertCoursesInput;
import dev.timetable.spring.dto.course.UpsertCoursesInput.CourseInput;

import static org.assertj.core.api.Assertions.*;

/**
 * CourseService の単体テスト（DB結合あり）。
 */
@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private LaneRepository laneRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private HomeroomRepository homeroomRepository;

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    private Long givenId;
    private Long parentId;

    @BeforeEach
    void setUp() {
        // Given 
        // 教科を作成
        OffsetDateTime now = OffsetDateTime.now();
        DisciplineEntity discipline = DisciplineEntity.builder()
            .disciplineCode("SCI")
            .disciplineName("理科")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        disciplineRepository.save(discipline);

        // 科目を作成
        SubjectEntity subjectEntity = SubjectEntity.builder()
            .ttid(TEST_TTID)
            .discipline(discipline)
            .subjectName("化学基礎")
            .credits((short) 2)
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        subjectRepository.saveAndFlush(subjectEntity);
        this.parentId = subjectEntity.getId();

        // 講座を作成
        CourseEntity given = courseService.upsert(UpsertCoursesInput.builder()
            .by(TEST_USER)
            .courses(List.of(
                CourseInput.builder()
                    .id(null)
                    .subjectId(parentId)
                    .courseName("1化基3")
                    .courseDetails(List.of())
                    .build()
            ))
            .build())
            .getFirst();
        this.givenId = given.getId();
    }

    @Test
    void retrieveByTtid() {
        // Given
        courseService.upsert(UpsertCoursesInput.builder()
            .by(TEST_USER)
            .courses(List.of(
                CourseInput.builder()
                    .subjectId(parentId)
                    .courseName("1化基6")
                    .courseDetails(List.of())
                    .build()
            ))
            .build()
        );

        // When
        List<CourseEntity> result = courseService.retrieve(
            RetrieveCoursesInput.builder()
                .ttid(TEST_TTID)
                .build()
        );

        // Then
        assertThat(result.size()).isGreaterThanOrEqualTo(1);
        assertThat(result.stream().map(CourseEntity::getCourseName)).contains("1化基6");
    }

    @Test
    void retrieveById() {
        // When
        List<CourseEntity> result = courseService.retrieve(
            RetrieveCoursesInput.builder()
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
        UpsertCoursesInput syncRequest = UpsertCoursesInput.builder()
            .by(TEST_USER)
            .courses(List.of(
                CourseInput.builder()
                    .id(null) // 新規作成
                    .subjectId(parentId)
                    .courseName("1化基4")
                    .courseDetails(List.of())
                    .build(),
                CourseInput.builder()
                    .id(givenId) // 更新
                    .courseName("1化基5")
                    .courseDetails(List.of())
                    .build()
            ))
            .build();

        List<CourseEntity> res = courseService.upsert(syncRequest);

        // Then
        // 戻り値の確認
        assertThat(res).hasSize(2);
        assertThat(res.stream().map(CourseEntity::getCourseName).toList())
            .contains("1化基4", "1化基5");

        // DBの確認
        List<CourseEntity> entities = courseRepository.findBySubjectId(parentId);
        assertThat(entities).hasSize(2);
        assertThat(entities.stream().map(CourseEntity::getCourseName).toList())
            .contains("1化基4", "1化基5");
    }

    @Test
    void delete() {
        // Given - 学級、ブロック、レーンを作成
        HomeroomEntity homeroom = HomeroomEntity.builder()
            .homeroomName("テスト学級")
            .ttid(TEST_TTID)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        homeroomRepository.save(homeroom);

        BlockEntity block = BlockEntity.builder()
            .blockName("テストブロック")
            .homeroomId(homeroom.getId())
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        blockRepository.save(block);

        LaneEntity lane = LaneEntity.builder()
            .block(block)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        laneRepository.save(lane);

        // 講座をレーンに紐づける
        CourseEntity course = courseRepository.findById(givenId).orElseThrow();
        lane.getCourses().add(course);
        laneRepository.save(lane);

        // レーンに講座が紐づいていることを確認
        LaneEntity savedLane = laneRepository.findById(lane.getId()).orElseThrow();
        assertThat(savedLane.getCourses()).hasSize(1);
        assertThat(savedLane.getCourses().get(0).getId()).isEqualTo(givenId);

        // When - 講座を削除する
        courseService.delete(givenId);

        // Then - 講座が削除されていることを確認
        assertThat(courseRepository.findById(givenId)).isEmpty();

        // Then - レーンは残っているが、講座との紐づけが削除されていることを確認
        LaneEntity updatedLane = laneRepository.findById(lane.getId()).orElseThrow();
        assertThat(updatedLane.getCourses()).isEmpty();
    }
}

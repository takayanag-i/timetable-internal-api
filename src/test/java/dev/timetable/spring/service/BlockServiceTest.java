package dev.timetable.spring.service;

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
import dev.timetable.spring.domain.entity.InstructorEntity;
import dev.timetable.spring.domain.entity.LaneEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.repository.BlockRepository;
import dev.timetable.spring.repository.CourseRepository;
import dev.timetable.spring.repository.DisciplineRepository;
import dev.timetable.spring.repository.HomeroomRepository;
import dev.timetable.spring.repository.InstructorRepository;
import dev.timetable.spring.repository.LaneRepository;
import dev.timetable.spring.repository.SubjectRepository;
import dev.timetable.spring.dto.block.RetrieveBlocksInput;
import dev.timetable.spring.dto.block.UpsertBlocksInput;
import dev.timetable.spring.dto.block.UpsertBlocksInput.BlockInput;

import static org.assertj.core.api.Assertions.*;

/**
 * BlockService の単体テスト（DB結合あり）。
 */
@SpringBootTest
@Transactional
class BlockServiceTest {

    @Autowired
    private BlockService blockService;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private HomeroomRepository homeroomRepository;

    @Autowired
    private LaneRepository laneRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";
    
    // テストデータ用定数
    private static final String TEST_HOMEROOM_NAME = "1年1組";
    private static final String TEST_BLOCK_NAME = "1年1組ブロック1";
    private static final String NEW_BLOCK_NAME = "1年1組ブロック2";
    private static final String UPDATED_BLOCK_NAME = "1年1組ブロック3";
    private static final int TEST_LANE_COUNT = 2;

    private Long testHomeroomId;
    private Long testBlockId;

    @BeforeEach
    void setUp() {
        this.testHomeroomId = createTestHomeroom();
        this.testBlockId = createTestBlockWithLanes(testHomeroomId);
    }

    @Test
    void retrieveByHomeroomId() {
        // When - 学級に紐づくブロックを取得する
        List<BlockEntity> result = blockService.retrieve(
            RetrieveBlocksInput.builder()
                .homeroomId(testHomeroomId)
                .build()
        );

        // Then - 期待されるブロックが取得できる
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBlockName()).isEqualTo(TEST_BLOCK_NAME);
    }

    @Test
    void retrieveById() {
        // When
        List<BlockEntity> result = blockService.retrieve(
            RetrieveBlocksInput.builder()
                .id(testBlockId)
                .build()
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(testBlockId);
    }

    @Test
    void upsert() {
        // Given - テスト用の追加データ定数
        final short NEW_LANE_COUNT = 1;
        
        // When - 新規作成と更新を含むリクエストを実行する
        UpsertBlocksInput req = UpsertBlocksInput.builder()
            .by(TEST_USER)
            .blocks(List.of(
                BlockInput.builder()
                    .id(null) // 作成
                    .homeroomId(testHomeroomId)
                    .blockName(NEW_BLOCK_NAME)
                    .laneCount(NEW_LANE_COUNT)
                    .build(),
                BlockInput.builder()
                    .id(testBlockId) // 更新
                    .blockName(UPDATED_BLOCK_NAME)
                    .laneCount(NEW_LANE_COUNT)
                    .build()
            ))
            .build();

        List<BlockEntity> result = blockService.upsert(req);

        // Then - 戻り値の確認
        assertThat(result).hasSize(2);
        assertBlockExists(result, NEW_BLOCK_NAME);
        assertBlockExists(result, UPDATED_BLOCK_NAME);

        // Then - DBの状態確認
        List<BlockEntity> savedBlocks = blockRepository.findByHomeroomId(testHomeroomId);
        assertThat(savedBlocks).hasSize(2);
        assertBlockExists(savedBlocks, NEW_BLOCK_NAME);
        assertBlockExists(savedBlocks, UPDATED_BLOCK_NAME);
    }

    @Test
    void delete() {
        // Given - setUpで作成したブロックに講座を紐づけたレーンを作成
        CourseEntity testCourse = createTestCourse();
        BlockEntity existingBlock = blockRepository.findById(testBlockId).orElseThrow();
        createLaneWithCourse(existingBlock, testCourse);

        // When - ブロックを削除する
        blockService.delete(testBlockId);

        // Then - ブロックが削除されていることを確認
        assertThat(blockRepository.findById(testBlockId)).isEmpty();
        
        // Then - 関連するレーンも削除されていることを確認
        assertThat(laneRepository.findByBlock_Id(testBlockId)).isEmpty();

        // Then - 講座自体は削除されていないことを確認
        assertThat(courseRepository.findById(testCourse.getId())).isPresent();
    }

    /**
     * テスト用の学級エンティティを作成する
     * 
     * @return 作成された学級のID
     */
    private Long createTestHomeroom() {
        HomeroomEntity homeroom = HomeroomEntity.builder()
            .homeroomName(TEST_HOMEROOM_NAME)
            .ttid(TEST_TTID)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        return homeroomRepository.save(homeroom).getId();
    }

    /**
     * テスト用のブロックエンティティとレーンエンティティを作成する
     * 
     * @param homeroomId 学級ID
     * @return 作成されたブロックのID
     */
    private Long createTestBlockWithLanes(Long homeroomId) {
        // ブロックを作成
        BlockEntity block = BlockEntity.builder()
            .homeroomId(homeroomId)
            .blockName(TEST_BLOCK_NAME)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        block = blockRepository.save(block);

        // ブロックに対応するレーンを作成
        createTestLanes(block, TEST_LANE_COUNT);
        
        return block.getId();
    }

    /**
     * テスト用のレーンエンティティを作成する
     * 
     * @param block 親ブロック
     * @param laneCount 作成するレーン数
     */
    private void createTestLanes(BlockEntity block, int laneCount) {
        for (int i = 0; i < laneCount; i++) {
            LaneEntity lane = LaneEntity.builder()
                .block(block)
                .createdAt(OffsetDateTime.now())
                .createdBy(TEST_USER)
                .updatedAt(OffsetDateTime.now())
                .updatedBy(TEST_USER)
                .build();
            laneRepository.save(lane);
        }
    }

    /**
     * ブロックリストに指定された名前のブロックが存在することを確認する
     */
    private void assertBlockExists(List<BlockEntity> blocks, String blockName) {
        assertThat(blocks.stream().anyMatch(block -> blockName.equals(block.getBlockName()))).isTrue();
    }

    /**
     * テスト用の講座エンティティを作成する（教科・科目・教員を含む）
     */
    private CourseEntity createTestCourse() {
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

        // 科目を作成
        SubjectEntity subject = SubjectEntity.builder()
            .subjectName("数学I")
            .ttid(TEST_TTID)
            .credits((short) 3)
            .discipline(discipline)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        subjectRepository.save(subject);

        // 教員を作成
        InstructorEntity instructor = InstructorEntity.builder()
            .instructorName("数原")
            .disciplineCode("MTH")
            .ttid(TEST_TTID)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        instructorRepository.save(instructor);

        // 講座を作成
        CourseEntity course = CourseEntity.builder()
            .courseName("1数I1")
            .subject(subject)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        return courseRepository.save(course);
    }

    /**
     * 指定されたブロックに講座を紐づけたレーンを作成する
     */
    private void createLaneWithCourse(BlockEntity block, CourseEntity course) {
        LaneEntity lane = LaneEntity.builder()
            .block(block)
            .createdAt(OffsetDateTime.now())
            .createdBy(TEST_USER)
            .updatedAt(OffsetDateTime.now())
            .updatedBy(TEST_USER)
            .build();
        lane = laneRepository.save(lane);

        // レーンに講座を紐づける
        lane.getCourses().add(course);
        laneRepository.save(lane);
    }
}

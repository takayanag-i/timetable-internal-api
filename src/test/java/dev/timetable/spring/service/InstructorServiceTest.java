package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.AttendanceDayEntity;
import dev.timetable.spring.domain.entity.InstructorEntity;
import dev.timetable.spring.repository.InstructorRepository;
import dev.timetable.spring.dto.instructor.RetrieveInstructorsInput;
import dev.timetable.spring.dto.instructor.UpsertInstructorsInput;
import dev.timetable.spring.dto.instructor.UpsertInstructorsInput.InstructorInput;

import static org.assertj.core.api.Assertions.*;

/**
 * InstructorService の単体テスト（DB結合あり）。
 */
@SpringBootTest
@Transactional
class InstructorServiceTest {

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private InstructorRepository instructorRepository;

    private static final UUID TEST_TTID = UUID.randomUUID();
    private static final String TEST_USER = "test-user";

    private Long instructorId;

    @BeforeEach
    void setUp() {
        // Repositoryクラスを使って直接テストデータを準備
        OffsetDateTime now = OffsetDateTime.now();
        
        InstructorEntity instructor = InstructorEntity.builder()
            .ttid(TEST_TTID)
            .instructorName("山田太郎")
            .disciplineCode("JPN")
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        
        // 勤怠曜日を作成
        AttendanceDayEntity mondayAttendance = AttendanceDayEntity.builder()
            .instructor(instructor)
            .dayOfWeek("monday")
            .unavailablePeriods(List.of((short) 1, (short) 2))
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        
        AttendanceDayEntity tuesdayAttendance = AttendanceDayEntity.builder()
            .instructor(instructor)
            .dayOfWeek("tuesday")
            .unavailablePeriods(List.of((short) 3))
            .createdAt(now)
            .createdBy(TEST_USER)
            .updatedAt(now)
            .updatedBy(TEST_USER)
            .build();
        
        instructor.getAttendanceDays().add(mondayAttendance);
        instructor.getAttendanceDays().add(tuesdayAttendance);
        
        // Repositoryで保存
        InstructorEntity savedEntity = instructorRepository.save(instructor);
        this.instructorId = savedEntity.getId();
    }

    @Test
    void retrieveByTtid() {
        // When
        List<InstructorEntity> result = instructorService.retrieve(
            RetrieveInstructorsInput.builder()
                .ttid(TEST_TTID)
                .build()
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get(0).getInstructorName()).isEqualTo("山田太郎");
        assertThat(result.get(0).getDisciplineCode()).isEqualTo("JPN");
        assertThat(result.get(0).getAttendanceDays()).hasSize(2);
    }

    @Test
    void retrieveById() {
        // When
        var result = instructorService.retrieve(
            RetrieveInstructorsInput.builder()
                .id(instructorId)
                .build()
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(instructorId);
    }
    @Test
    void upsert() {
        // Given - 既存データを使用
        UpsertInstructorsInput upsertRequest = UpsertInstructorsInput.builder()
            .ttid(TEST_TTID)
            .by("upsert-user")
            .instructors(List.of(
                InstructorInput.builder()
                    .id(instructorId) // 既存ID = 更新
                    .instructorName("更新後教員")
                    .disciplineCode("ENG")
                    .attendanceDays(List.of(
                        InstructorInput.AttendanceDayInput.builder()
                            .id(null)
                            .dayOfWeek("friday")
                            .unavailablePeriods(List.of((short) 6))
                            .build()
                    ))
                    .build(),
                InstructorInput.builder()
                    .id(null) // ID無し = 新規作成
                    .instructorName("新規教員")
                    .disciplineCode("SCI")
                    .attendanceDays(List.of(
                        InstructorInput.AttendanceDayInput.builder()
                            .id(null)
                            .dayOfWeek("wednesday")
                            .unavailablePeriods(List.of((short) 3, (short) 4))
                            .build()
                    ))
                    .build()
            ))
            .build();

        // When
        List<InstructorEntity> resultEntities = instructorService.upsert(upsertRequest);

        // Then
        // 戻り値は2件（更新1件、新規作成1件）
        assertThat(resultEntities).hasSize(2);
        
        // 更新された教員の確認
        InstructorEntity updatedInstructor = instructorRepository.findById(instructorId).orElseThrow();
        assertThat(updatedInstructor.getInstructorName()).isEqualTo("更新後教員");
        assertThat(updatedInstructor.getDisciplineCode()).isEqualTo("ENG");
        assertThat(updatedInstructor.getUpdatedBy()).isEqualTo("upsert-user");
        assertThat(updatedInstructor.getAttendanceDays()).hasSize(1);
        assertThat(updatedInstructor.getAttendanceDays().get(0).getDayOfWeek()).isEqualTo("friday");
        
        // 新規作成された教員の確認
        List<InstructorEntity> allInstructors = instructorRepository.findByTtid(TEST_TTID);
        assertThat(allInstructors).hasSize(2); // upsertでは削除されないため既存も残る
        
        InstructorEntity newInstructor = allInstructors.stream()
            .filter(i -> "新規教員".equals(i.getInstructorName()))
            .findFirst()
            .orElseThrow();
        assertThat(newInstructor.getDisciplineCode()).isEqualTo("SCI");
        assertThat(newInstructor.getAttendanceDays()).hasSize(1);
        assertThat(newInstructor.getAttendanceDays().get(0).getDayOfWeek()).isEqualTo("wednesday");
    }
}

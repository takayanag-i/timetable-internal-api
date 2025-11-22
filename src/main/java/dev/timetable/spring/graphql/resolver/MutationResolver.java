package dev.timetable.spring.graphql.resolver;

import dev.timetable.spring.domain.entity.BlockEntity;
import dev.timetable.spring.domain.entity.ConstraintDefinitionEntity;
import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.domain.entity.GradeEntity;
import dev.timetable.spring.domain.entity.InstructorEntity;
import dev.timetable.spring.domain.entity.LaneEntity;
import dev.timetable.spring.domain.entity.RoomEntity;
import dev.timetable.spring.domain.entity.SchoolDayEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.dto.block.UpsertBlocksInput;
import dev.timetable.spring.dto.constraintdefinition.UpsertConstraintDefinitionsInput;
import dev.timetable.spring.dto.course.UpsertCoursesInput;
import dev.timetable.spring.dto.homeroom.UpsertHomeroomsInput;
import dev.timetable.spring.dto.instructor.UpsertInstructorsInput;
import dev.timetable.spring.dto.grade.UpsertGradesInput;
import dev.timetable.spring.dto.lane.UpsertLanesInput;
import dev.timetable.spring.dto.room.UpsertRoomsInput;
import dev.timetable.spring.dto.schoolday.UpsertSchoolDaysInput;
import dev.timetable.spring.dto.subject.UpsertSubjectsInput;
import dev.timetable.spring.service.BlockService;
import dev.timetable.spring.service.ConstraintDefinitionService;
import dev.timetable.spring.service.CourseService;
import dev.timetable.spring.service.HomeroomService;
import dev.timetable.spring.service.InstructorService;
import dev.timetable.spring.service.LaneService;
import dev.timetable.spring.service.RoomService;
import dev.timetable.spring.service.SchoolDayService;
import dev.timetable.spring.service.SubjectService;
import dev.timetable.spring.service.GradeService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;


/**
 * GraphQLのMutationリゾルバ.
 */
@Controller
@RequiredArgsConstructor
public class MutationResolver {

    private final HomeroomService homeroomService;
    private final SubjectService subjectService;
    private final RoomService roomService;
    private final SchoolDayService schoolDayService;
    private final CourseService courseService;
    private final InstructorService instructorService;
    private final BlockService blockService;
    private final LaneService laneService;
    private final ConstraintDefinitionService constraintDefinitionService;
    private final GradeService gradeService;

    /**
     * [GM001] 学校曜日作成更新API
     * 
     * @param input
     * @return
     */
    @MutationMapping
    public List<SchoolDayEntity> upsertSchoolDays(@Argument UpsertSchoolDaysInput input) {
        return schoolDayService.upsert(input);
    }
    
    /**
     * [GM002] 教員作成更新API
     * 
     * @param input
     * @return
     */
    @MutationMapping
    public List<InstructorEntity> upsertInstructors(@Argument UpsertInstructorsInput input) {
        return instructorService.upsert(input);
    }

    /**
     * [GM003] 科目作成更新API
     * 
     * @param input
     * @return
     */
    @MutationMapping
    public List<SubjectEntity> upsertSubjects(@Argument UpsertSubjectsInput input) {
        return subjectService.upsert(input);
    }

    /**
     * [GM004] 教室作成更新API
     * 
     * @param input
     * @return
     */
    @MutationMapping
    public List<RoomEntity> upsertRooms(@Argument UpsertRoomsInput input) {
        return roomService.upsert(input);
    }

    /**
     * [IGU005] 学級作成更新API
     * 
     * @param input
     * @return
     */
    @MutationMapping
    public List<HomeroomEntity> upsertHomerooms(@Argument UpsertHomeroomsInput input) {
        return homeroomService.upsert(input);
    }

    /**
     * [IGU006] ブロック作成更新API
     * 
     * @param input
     * @return
     */
    @MutationMapping
    public List<BlockEntity> upsertBlocks(@Argument UpsertBlocksInput input) {
        return blockService.upsert(input);
    }

    /**
     * [IGU007] 講座作成更新API
     * 
     * @param input
     * @return
     */
    @MutationMapping
    public List<CourseEntity> upsertCourses(@Argument UpsertCoursesInput input) {
        return courseService.upsert(input);
    }

    /**
     * [IGU008] レーン更新API
     * 
     * @param input
     * @return
     */
    @MutationMapping
    public List<LaneEntity> upsertLanes(@Argument UpsertLanesInput input) {
        return laneService.upsert(input);
    }

    /**
     * [IGU009] 学年作成更新API
     */
    @MutationMapping
    public List<GradeEntity> upsertGrades(@Argument UpsertGradesInput input) {
        return gradeService.upsert(input);
    }

    /**
     * [IGD005] 学級削除API
     */
    @MutationMapping
    public Boolean deleteHomeroom(@Argument Long id) {
        homeroomService.delete(id);
        return true;
    }

    /**
     * [IGD006] ブロック削除API
     */
    @MutationMapping
    public Boolean deleteBlock(@Argument Long id) {
        blockService.delete(id);
        return true;
    }

    /**
     * [IGD007] 講座削除API
     */
    @MutationMapping
    public Boolean deleteCourse(@Argument Long id) {
        courseService.delete(id);
        return true;
    }

    /**
     * [IGU010] 制約定義作成更新API
     * 
     * @param input
     * @return
     */
    @MutationMapping
    public List<ConstraintDefinitionEntity> upsertConstraintDefinitions(@Argument UpsertConstraintDefinitionsInput input) {
        return constraintDefinitionService.upsert(input);
    }

    /**
     * [IGD009] 制約定義削除API
     */
    @MutationMapping
    public Boolean deleteConstraintDefinition(@Argument Long id) {
        constraintDefinitionService.deleteById(id);
        return true;
    }
}
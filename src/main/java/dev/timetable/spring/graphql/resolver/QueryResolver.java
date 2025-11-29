package dev.timetable.spring.graphql.resolver;

import dev.timetable.spring.domain.entity.BlockEntity;
import dev.timetable.spring.domain.entity.ConstraintDefinitionEntity;
import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.GradeEntity;
import dev.timetable.spring.domain.entity.HomeroomEntity;
import dev.timetable.spring.domain.entity.InstructorEntity;
import dev.timetable.spring.domain.entity.LaneEntity;
import dev.timetable.spring.domain.entity.RoomEntity;
import dev.timetable.spring.domain.entity.SchoolDayEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.domain.entity.TimetableResultEntity;
import dev.timetable.spring.dto.block.RetrieveBlocksInput;
import dev.timetable.spring.dto.constraintdefinition.RetrieveConstraintDefinitionsInput;
import dev.timetable.spring.dto.homeroom.RetrieveHomeroomsInput;
import dev.timetable.spring.dto.course.RetrieveCoursesInput;
import dev.timetable.spring.dto.instructor.RetrieveInstructorsInput;
import dev.timetable.spring.dto.lane.RetrieveLanesInput;
import dev.timetable.spring.dto.room.RetrieveRoomsInput;
import dev.timetable.spring.dto.schoolday.RetrieveSchoolDaysInput;
import dev.timetable.spring.dto.subject.RetrieveSubjectsInput;
import dev.timetable.spring.dto.timetableresult.RetrieveTimetableResultsInput;
import dev.timetable.spring.dto.grade.RetrieveGradesInput;
import dev.timetable.spring.service.BlockService;
import dev.timetable.spring.service.ConstraintDefinitionService;
import dev.timetable.spring.service.HomeroomService;
import dev.timetable.spring.service.InstructorService;
import dev.timetable.spring.service.LaneService;
import dev.timetable.spring.service.RoomService;
import dev.timetable.spring.service.SchoolDayService;
import dev.timetable.spring.service.SubjectService;
import dev.timetable.spring.service.TimetableResultService;
import dev.timetable.spring.service.CourseService;
import dev.timetable.spring.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * GraphQLのQueryリゾルバ.
 */
@Controller // アノテーションは必ずController
@RequiredArgsConstructor
public class QueryResolver {

    private final SchoolDayService schoolDayService;
    private final HomeroomService homeroomService;
    private final SubjectService subjectService;
    private final RoomService roomService;
    private final InstructorService instructorService;
    private final BlockService blockService;
    private final LaneService laneService;
    private final ConstraintDefinitionService constraintDefinitionService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final TimetableResultService timetableResultService;
    
    /**
     * [IGQ001] 学校曜日取得API
     * 
     * @param input
     */
    @QueryMapping
    public List<SchoolDayEntity> schoolDays(@Argument RetrieveSchoolDaysInput input) {
        return schoolDayService.retrieve(input);
    }

    /**
     * [IGQ002] 教員取得API
     * 
     * @param input
     */
    @QueryMapping
    public List<InstructorEntity> instructors(@Argument RetrieveInstructorsInput input) {
        return instructorService.retrieve(input);
    }

    /**
     * [IGQ003] 教室取得API
     * 
     * @param input
     */
    @QueryMapping
    public List<RoomEntity> rooms(@Argument RetrieveRoomsInput input) {
        return roomService.retrieve(input);
    }

    /**
     * [IGQ004] 科目取得API
     * 
     * @param input
     */
    @QueryMapping
    public List<SubjectEntity> subjects(@Argument RetrieveSubjectsInput input) {
        return subjectService.retrieve(input);
    }
    
    /**
     * [IGQ005] 学級取得API
     * 
     * @param input
     */
    @QueryMapping
    public List<HomeroomEntity> homerooms(@Argument RetrieveHomeroomsInput input) {
        return homeroomService.retrieve(input);
    }

    @SchemaMapping(typeName = "Homeroom", field = "blocks")
    public List<BlockEntity> homeroomBlocks(HomeroomEntity homeroom) {
        return blockService.retrieve(
            RetrieveBlocksInput.builder()
                .homeroomId(homeroom.getId())
                .build()
        );
    }

    /**
     * [IGQ006] ブロック取得API
     * 
     * @param input
     */
    @QueryMapping
    public List<BlockEntity> blocks(@Argument RetrieveBlocksInput input) {
        return blockService.retrieve(input);
    }

    /**
     * [IGQ007] レーン取得API
     * 
     * @param input
     */
    @QueryMapping
    public List<LaneEntity> lanes(@Argument RetrieveLanesInput input) {
        return laneService.retrieve(input);
    }

    /**
     * [IGQ008] 講座取得API
     *
     * @param input
     */
    @QueryMapping
    public List<CourseEntity> courses(@Argument RetrieveCoursesInput input) {
        return courseService.retrieve(input);
    }

    /**
     * [IGQ009] 学年取得API
     *
     * @param input
     */
    @QueryMapping
    public List<GradeEntity> grades(@Argument RetrieveGradesInput input) {
        return gradeService.retrieve(input);
    }

    /**
     * [IGQ010] 制約定義取得API
     * 
     * @param input
     */
    @QueryMapping
    public List<ConstraintDefinitionEntity> constraintDefinitions(@Argument RetrieveConstraintDefinitionsInput input) {
        return constraintDefinitionService.retrieve(input);
    }

    /**
     * [IGQ011] 時間割編成結果取得API
     *
     * @param input
     */
    @QueryMapping
    public List<TimetableResultEntity> timetableResults(@Argument RetrieveTimetableResultsInput input) {
        return timetableResultService.retrieve(input);
    }
}
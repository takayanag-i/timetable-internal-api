package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.dto.course.RetrieveCoursesInput;
import dev.timetable.spring.dto.course.UpsertCoursesInput;
import dev.timetable.spring.dto.course.UpsertCoursesInput.CourseInput;
import dev.timetable.spring.domain.entity.CourseEntity;
import dev.timetable.spring.domain.entity.InstructorEntity;
import dev.timetable.spring.domain.entity.RoomEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.domain.entity.LaneEntity;
import dev.timetable.spring.domain.updater.CourseUpdater;
import dev.timetable.spring.repository.CourseRepository;
import dev.timetable.spring.repository.InstructorRepository;
import dev.timetable.spring.repository.RoomRepository;
import dev.timetable.spring.repository.SubjectRepository;
import dev.timetable.spring.repository.LaneRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 講座Service.
 */
@Service
@RequiredArgsConstructor
public class CourseService {

    /** 講座Repository. */
    private final CourseRepository courseRepository;
    
    /** 科目Repository. */
    private final SubjectRepository subjectRepository;
    
    /** 教員Repository. */
    private final InstructorRepository instructorRepository;
    
    /** 教室Repository. */
    private final RoomRepository roomRepository;
    
    /** レーンRepository. */
    private final LaneRepository laneRepository;

    /**
     * 講座を作成または更新する.
     * 
     * @param req 作成更新Input
     */
    @Transactional(rollbackFor = Exception.class)
    public List<CourseEntity> upsert(UpsertCoursesInput req) {
        
        List<CourseInput> inputs = req.getCourses();
        List<CourseEntity> result = new ArrayList<>();
        
        if (!inputs.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();
            
            // 新規作成と更新を分離
            List<CourseInput> inputsToCreate = inputs.stream()
                .filter(input -> input.getId() == null)
                .toList();
            
            List<CourseInput> inputsToUpdate = inputs.stream()
                .filter(input -> input.getId() != null)
                .toList();
            
            // 作成
            if (!inputsToCreate.isEmpty()) {
                List<CourseEntity> entities = create(inputsToCreate, req.getBy(), now);
                result.addAll(entities);
            }
            
            // 更新
            if (!inputsToUpdate.isEmpty()) {
                List<CourseEntity> entities = update(inputsToUpdate, req.getBy(), now);
                result.addAll(entities);
            }
        }
        return result;
    }

    /**
     * 講座を参照する.
     *
     * @param input 取得Input
     * @return エンティティリスト
     */
    @Transactional(readOnly = true)
    public List<CourseEntity> retrieve(RetrieveCoursesInput input) {
        if (input.getId() != null) {
            return courseRepository.findById(input.getId())
                .map(List::of)
                .orElseGet(ArrayList::new);
        } else if (input.getSubjectId() != null) {
            return courseRepository.findBySubjectId(input.getSubjectId());
        } else if (input.getTtid() != null) {
            return courseRepository.findBySubject_Ttid(input.getTtid());
        } else {
            return courseRepository.findAll();
        }
    }

    private List<CourseEntity> create(List<CourseInput> inputs, String createdBy, OffsetDateTime now) {

        List<Long> subjectIds = inputs.stream()
            .map(CourseInput::getSubjectId)
            .distinct()
            .toList();

        List<Long> instructorIds = inputs.stream()
            .flatMap(req -> req.getCourseDetails().stream())
            .map(CourseInput.CourseDetailInput::getInstructorId)
            .distinct()
            .toList();
        
        List<Long> roomIds = inputs.stream()
            .flatMap(req -> req.getCourseDetails().stream())
            .map(CourseInput.CourseDetailInput::getRoomId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
        
        Map<Long, SubjectEntity> subjectMap = EntityMapUtil.toMap(
            subjectRepository.findAllById(subjectIds),
            SubjectEntity::getId
        );
        
        Map<Long, InstructorEntity> instructorMap = EntityMapUtil.toMap(
            instructorRepository.findAllById(instructorIds),
            InstructorEntity::getId
        );
        
        Map<Long, RoomEntity> roomMap = EntityMapUtil.toMap(
            roomRepository.findAllById(roomIds),
            RoomEntity::getId
        );
        
        List<CourseEntity> entities = inputs.stream()
            .map(entity -> CourseUpdater.create(entity, subjectMap, instructorMap, roomMap, createdBy, now))
            .toList();
        
        courseRepository.saveAll(entities);

        return entities;
    }
    
    private List<CourseEntity> update(List<CourseInput> inputs, String updatedBy, OffsetDateTime now) {
        // IDを一括取得
        List<Long> ids = inputs.stream()
            .map(CourseInput::getId)
            .toList();
        
        List<CourseEntity> entities = courseRepository.findAllById(ids);
        
        // 存在しないIDをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            CourseEntity::getId,
            "講座ID"
        );
        
        // 必要な教員・教室情報を一括取得
        List<Long> instructorIds = inputs.stream()
            .flatMap(req -> req.getCourseDetails().stream())
            .map(CourseInput.CourseDetailInput::getInstructorId)
            .distinct()
            .toList();
        
        List<Long> roomIds = inputs.stream()
            .flatMap(req -> req.getCourseDetails().stream())
            .map(CourseInput.CourseDetailInput::getRoomId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
        
        Map<Long, InstructorEntity> instructorMap = EntityMapUtil.toMap(
            instructorRepository.findAllById(instructorIds),
            InstructorEntity::getId
        );
        
        Map<Long, RoomEntity> roomMap = EntityMapUtil.toMap(
            roomRepository.findAllById(roomIds),
            RoomEntity::getId
        );

        // ID -> Entity のMap作成
        Map<Long, CourseEntity> entityMap = EntityMapUtil.toMap(
            entities,
            CourseEntity::getId
        );

        inputs.forEach(input -> {
            CourseEntity entity = entityMap.get(input.getId());
            CourseUpdater.update(entity, input, instructorMap, roomMap, updatedBy, now);
        });

        return entities;
    }

    /**
     * 講座を削除する.
     * 
     * @param id 削除する講座ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 講座Entityを取得
        CourseEntity entity = courseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("存在しない講座ID: " + id));
        
        // 講座を含むレーンから講座を削除
        List<LaneEntity> lanesWithCourse = laneRepository.findByCourseId(id);
        lanesWithCourse.forEach(lane -> {
            lane.getCourses().removeIf(course -> course.getId().equals(id));
        });
        laneRepository.saveAll(lanesWithCourse);
        
        // 講座を削除（CourseDetailも cascade = CascadeType.ALL で自動削除される）
        courseRepository.delete(entity);
    }
}

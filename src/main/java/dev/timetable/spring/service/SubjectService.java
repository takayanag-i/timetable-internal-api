package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.dto.subject.UpsertSubjectsInput;
import dev.timetable.spring.dto.subject.UpsertSubjectsInput.SubjectInput;
import dev.timetable.spring.dto.subject.RetrieveSubjectsInput;
import dev.timetable.spring.domain.entity.DisciplineEntity;
import dev.timetable.spring.domain.entity.SubjectEntity;
import dev.timetable.spring.domain.entity.GradeEntity;
import dev.timetable.spring.domain.updater.SubjectUpdater;
import dev.timetable.spring.repository.DisciplineRepository;
import dev.timetable.spring.repository.SubjectRepository;
import dev.timetable.spring.repository.GradeRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 科目Service.
 */
@Service
@RequiredArgsConstructor
public class SubjectService {

    /** 科目Repository. */
    private final SubjectRepository subjectRepository;
    
    /** 教科Repository. */
    private final DisciplineRepository disciplineRepository;

    /** 学年Repository. */
    private final GradeRepository gradeRepository;


    @Transactional(readOnly = true)
    public List<SubjectEntity> retrieve(UUID ttid) {
        return subjectRepository.findByTtid(ttid);
    }

    /**
     * 科目を参照する.
     * 
     * @param input 取得Input
     * @return 科目リスト
     */
    @Transactional(readOnly = true)
    public List<SubjectEntity> retrieve(RetrieveSubjectsInput input) {
        if (input.getId() != null) {
            // 特定のIDが指定されている場合
            return subjectRepository.findById(input.getId())
                .map(List::of)
                .orElse(new ArrayList<>());
        } else if (input.getTtid() != null) {
            // TTIDが指定されている場合
            return subjectRepository.findByTtid(input.getTtid());
        } else {
            // どちらも指定されていない場合は空リストを返す
            return new ArrayList<>();
        }
    }

    /**
     * 科目を作成または更新する.
     *
     * @param req 作成更新Input
     */
    @Transactional(rollbackFor = Exception.class)
    public List<SubjectEntity> upsert(UpsertSubjectsInput req) {
        UUID ttid = req.getTtid();
                
        List<SubjectInput> inputs = req.getSubjects();
        List<SubjectEntity> result = new ArrayList<>();
        
        if (!inputs.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();
            
            // 作成と更新を分離
            List<SubjectInput> inputsToCreate = inputs.stream()
                .filter(input -> input.getId() == null)
                .toList();
            
            List<SubjectInput> inputsToUpdate = inputs.stream()
                .filter(input -> input.getId() != null)
                .toList();
            
            // 作成
            if (!inputsToCreate.isEmpty()) {
                List<SubjectEntity> entities = createForUpsert(inputsToCreate, ttid, req.getBy(), now);
                result.addAll(entities);
            }
            
            // 更新
            if (!inputsToUpdate.isEmpty()) {
                List<SubjectEntity> entities = updateForUpsert(inputsToUpdate, req.getBy(), now);
                result.addAll(entities);
            }
        }
        return result;
    }

    private List<SubjectEntity> createForUpsert(List<SubjectInput> inputs, UUID ttid, String updatedBy, OffsetDateTime now) {
        // 教科コードを一括取得
        List<String> disciplineCodes = inputs.stream()
            .map(SubjectInput::getDisciplineCode)
            .distinct()
            .toList();
        
        List<DisciplineEntity> disciplines = disciplineRepository.findAllById(disciplineCodes);
        
        // 存在しない教科コードをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            disciplineCodes,
            disciplines,
            DisciplineEntity::getDisciplineCode,
            "教科コード"
        );

        // 学年IDを一括取得
        List<Long> gradeIds = inputs.stream()
            .map(SubjectInput::getGradeId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        List<GradeEntity> grades = gradeRepository.findAllById(gradeIds);

        EntityValidationUtil.validateAllEntitiesExist(
            gradeIds,
            grades,
            GradeEntity::getId,
            "学年ID"
        );
        
        // EntityのMapを作成
        Map<String, DisciplineEntity> disciplineMap = EntityMapUtil.toMap(
            disciplines,
            DisciplineEntity::getDisciplineCode
        );

        Map<Long, GradeEntity> gradeMap = EntityMapUtil.toMap(
            grades,
            GradeEntity::getId
        );
        
        // 科目Entity一括作成
        List<SubjectEntity> entities = inputs.stream()
            .map(request -> {
                DisciplineEntity discipline = disciplineMap.get(request.getDisciplineCode());
                GradeEntity grade = request.getGradeId() == null ? null : gradeMap.get(request.getGradeId());
                return SubjectUpdater.create(request, discipline, grade, ttid, updatedBy, now);
            })
            .toList();
        
        subjectRepository.saveAll(entities);
        
        return entities;
    }

    private List<SubjectEntity> updateForUpsert(List<SubjectInput> inputs, String updatedBy, OffsetDateTime now) {
        // IDを一括取得
        List<Long> ids = inputs.stream()
            .map(SubjectInput::getId)
            .toList();
        
        List<SubjectEntity> entities = subjectRepository.findAllById(ids);
        
        // 存在しないIDをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            SubjectEntity::getId,
            "科目ID"
        );
        
        // 教科コードを一括取得
        List<String> disciplineCodes = inputs.stream()
            .map(SubjectInput::getDisciplineCode)
            .distinct()
            .toList();
        
        List<DisciplineEntity> disciplines = disciplineRepository.findAllById(disciplineCodes);
        
        // 存在しない教科コードをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            disciplineCodes,
            disciplines,
            DisciplineEntity::getDisciplineCode,
            "教科コード"
        );

        // 学年IDを一括取得
        List<Long> gradeIds = inputs.stream()
            .map(SubjectInput::getGradeId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        List<GradeEntity> grades = gradeRepository.findAllById(gradeIds);

        EntityValidationUtil.validateAllEntitiesExist(
            gradeIds,
            grades,
            GradeEntity::getId,
            "学年ID"
        );
        
        // Mapを作成
        Map<Long, SubjectEntity> subjectMap = EntityMapUtil.toMap(
            entities,
            SubjectEntity::getId
        );
            
        Map<String, DisciplineEntity> disciplineMap = EntityMapUtil.toMap(
            disciplines,
            DisciplineEntity::getDisciplineCode
        );

        Map<Long, GradeEntity> gradeMap = EntityMapUtil.toMap(
            grades,
            GradeEntity::getId
        );
        
        // 一括更新
        inputs.forEach(input -> {
            SubjectEntity entity = subjectMap.get(input.getId());
            DisciplineEntity discipline = disciplineMap.get(input.getDisciplineCode());
            GradeEntity grade = input.getGradeId() == null ? null : gradeMap.get(input.getGradeId());
            SubjectUpdater.update(entity, input, discipline, grade, updatedBy, now);
        });
        
        subjectRepository.saveAll(entities);
        
        return entities;
    }

    @Transactional(rollbackFor = Exception.class)
    private void delete(Long id) {
        SubjectEntity subjectEntity = subjectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("仮）科目Entityなし: " + id)); // FIXME 仮の例外処理
        
        subjectRepository.delete(subjectEntity);
    }
}
package dev.timetable.spring.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.domain.entity.ConstraintDefinitionMasterEntity;
import dev.timetable.spring.repository.ConstraintDefinitionMasterRepository;
import lombok.RequiredArgsConstructor;

/**
 * 制約定義マスタService.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConstraintDefinitionMasterService {

    private final ConstraintDefinitionMasterRepository repository;

    /**
     * 制約定義マスタを全件取得する（パラメータマスタも含む）.
     *
     * @return 制約定義マスタエンティティリスト
     */
    public List<ConstraintDefinitionMasterEntity> retrieveAll() {
        return repository.findAll();
    }
}


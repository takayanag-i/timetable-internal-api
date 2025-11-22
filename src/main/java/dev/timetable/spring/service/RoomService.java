package dev.timetable.spring.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.timetable.spring.dto.room.UpsertRoomsInput;
import dev.timetable.spring.dto.room.UpsertRoomsInput.RoomInput;
import dev.timetable.spring.dto.room.RetrieveRoomsInput;
import dev.timetable.spring.domain.entity.RoomEntity;
import dev.timetable.spring.domain.updater.RoomUpdater;
import dev.timetable.spring.repository.RoomRepository;
import dev.timetable.spring.util.EntityMapUtil;
import dev.timetable.spring.util.EntityValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * 教室Service.
 */
@Service
@RequiredArgsConstructor
public class RoomService {

    /** 教室Repository. */
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public List<RoomEntity> retrieve(UUID ttid) {
        return roomRepository.findByTtid(ttid);
    }

    /**
     * 教室を参照する.
     * 
     * @param input 取得Input
     * @return 教室リスト
     */
    @Transactional(readOnly = true)
    public List<RoomEntity> retrieve(RetrieveRoomsInput input) {
        if (input.getId() != null) {
            // 特定のIDが指定されている場合
            return roomRepository.findById(input.getId())
                .map(List::of)
                .orElse(new ArrayList<>());
        } else if (input.getTtid() != null) {
            // TTIDが指定されている場合
            return roomRepository.findByTtid(input.getTtid());
        } else {
            // どちらも指定されていない場合は空リストを返す
            return new ArrayList<>();
        }
    }

    /**
     * 教室を作成または更新する.
     *
     * @param req 作成更新Input
     */
    @Transactional(rollbackFor = Exception.class)
    public List<RoomEntity> upsert(UpsertRoomsInput req) {
        UUID ttid = req.getTtid();
                
        List<RoomInput> inputs = req.getRooms();
        List<RoomEntity> result = new ArrayList<>();
        
        if (!inputs.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now();
            
            // 作成と更新を分離
            List<RoomInput> inputsToCreate = inputs.stream()
                .filter(input -> input.getId() == null)
                .toList();
            
            List<RoomInput> inputsToUpdate = inputs.stream()
                .filter(input -> input.getId() != null)
                .toList();
            
            // 作成
            if (!inputsToCreate.isEmpty()) {
                List<RoomEntity> entities = createForUpsert(inputsToCreate, ttid, req.getBy(), now);
                result.addAll(entities);
            }
            
            // 更新
            if (!inputsToUpdate.isEmpty()) {
                List<RoomEntity> entities = updateForUpsert(inputsToUpdate, req.getBy(), now);
                result.addAll(entities);
            }
        }
        return result;
    }

    private List<RoomEntity> createForUpsert(List<RoomInput> inputs, UUID ttid, String updatedBy, OffsetDateTime now) {
        // Entityを一括作成
        List<RoomEntity> entities = inputs.stream()
            .map(request -> RoomUpdater.create(request, ttid, updatedBy, now))
            .toList();
        
        // DBに保存
        roomRepository.saveAll(entities);

        return entities;
    }

    private List<RoomEntity> updateForUpsert(List<RoomInput> inputs, String updatedBy, OffsetDateTime now) {
        // 教室IDを一括取得
        List<Long> ids = inputs.stream()
            .map(RoomInput::getId)
            .toList();
        
        // Entityを一括取得
        List<RoomEntity> entities = roomRepository.findAllById(ids);
        
        // 存在しない教室IDをチェック
        EntityValidationUtil.validateAllEntitiesExist(
            ids,
            entities,
            RoomEntity::getId,
            "教室ID"
        );
        
        // ID -> Entity のMap作成
        Map<Long, RoomEntity> entityMap = EntityMapUtil.toMap(
            entities,
            RoomEntity::getId
        );
        
        // Entityを一括更新
        inputs.forEach(input -> {
            RoomEntity entity = entityMap.get(input.getId());
            RoomUpdater.update(entity, input, updatedBy, now);
        });
        
        // DBに保存
        roomRepository.saveAll(entities);

        return entities;
    }
}

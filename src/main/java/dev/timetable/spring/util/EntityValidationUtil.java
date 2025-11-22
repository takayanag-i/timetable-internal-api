package dev.timetable.spring.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Entity検証用ユーティリティクラス.
 * 
 * サービス層で共通して使用されるEntity存在確認ロジックを提供する。
 */
public class EntityValidationUtil {

    private EntityValidationUtil() {
        // ユーティリティクラスのためインスタンス化を防ぐ
    }

    /**
     * エンティティのIDリストと取得されたエンティティを比較し、存在しないIDがあれば例外をスローする.
     * 
     * @param <T> エンティティの型
     * @param <ID> IDの型
     * @param requestedIds リクエストされたIDのリスト
     * @param foundEntities 実際に取得されたエンティティのリスト
     * @param idExtractor エンティティからIDを取得する関数
     * @param entityName エンティティの名前（エラーメッセージ用）
     * @throws IllegalArgumentException 存在しないIDがある場合
     */
    public static <T, ID> void validateAllEntitiesExist(
            List<ID> requestedIds,
            Collection<T> foundEntities,
            Function<T, ID> idExtractor,
            String entityName) {
        
        List<ID> existingIds = foundEntities.stream()
            .map(idExtractor)
            .toList();
        
        List<ID> missingIds = requestedIds.stream()
            .filter(id -> !existingIds.contains(id))
            .toList();
        
        if (!missingIds.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("存在しない%s: %s", entityName, missingIds)
            );
        }
    }
}

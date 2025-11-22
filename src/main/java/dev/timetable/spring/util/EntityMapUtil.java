package dev.timetable.spring.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * EntityのMap変換用ユーティリティクラス.
 * 
 * サービス層で頻繁に使用されるEntity → Map変換処理を提供する。
 */
public class EntityMapUtil {

    private EntityMapUtil() {
        // ユーティリティクラスのためインスタンス化を防ぐ
    }

    /**
     * エンティティのコレクションをキーとエンティティのMapに変換する.
     * 
     * @param <K> キーの型
     * @param <V> エンティティの型
     * @param entities エンティティのコレクション
     * @param keyExtractor エンティティからキーを取得する関数
     * @return キーとエンティティのMap
     */
    public static <K, V> Map<K, V> toMap(
            Collection<V> entities,
            Function<V, K> keyExtractor) {
        return entities.stream()
            .collect(Collectors.toMap(
                keyExtractor,
                Function.identity()
            ));
    }
}

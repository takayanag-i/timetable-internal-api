# ビルドステージ
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Gradleラッパーと設定ファイルをコピー
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 依存関係をダウンロード（キャッシュ効率化）
RUN ./gradlew dependencies --no-daemon || true

# ソースコードをコピー
COPY src src

# アプリケーションをビルド（テストをスキップ）
RUN ./gradlew bootJar -x test --no-daemon

# 実行ステージ
FROM eclipse-temurin:21-jre

WORKDIR /app

# ビルドステージからJARファイルをコピー
COPY --from=builder /app/build/libs/*.jar app.jar

# Cloud Runのデフォルトポート
ENV PORT=8080

# 非rootユーザーで実行
RUN useradd -m appuser && chown -R appuser:appuser /app
USER appuser

# アプリケーション起動
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=sandbox", "-jar", "app.jar"]

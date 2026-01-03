# Internal API
## Container
- **OS**: Ubuntu 24.04.2 LTS (noble)
- **JDK**: Temurin 21.0.7+6-LTS (Java21)

## Run Local Server

- docker-composeがあるディレクトリから実行する場合
```shell
docker-compose exec spring sh -c "cd /workspaces/timetable/spring && ./gradlew bootRun"
```

## Actions
- ユニットテスト
    - JUnitのテストケースを実行
    - Jacocoのカバレッジレポートを生成

## MCP Server
- postgresql
    - docker-compose内のローカルDBにアクセスする場合
    - `postgresql://postgres:postgres@172.17.0.1:5432/postgres`

## Update Dump
- docker-composeがあるディレクトリで実行する。
```shell
for table in $(docker compose exec -T postgres psql -U postgres -d postgres -t -c "SELECT tablename FROM pg_tables WHERE schemaname='public';"); do docker compose exec -T postgres pg_dump -U postgres -d postgres -t "$table" --data-only --column-inserts > "spring/sql/insert/${table}_dump.sql"; done
```
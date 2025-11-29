---
applyTo: 'spring/*'
---

## Java Guidelines
- 参照クラスは原則として `import` 文でインポートしてください。
    - クラスブロック内部の実装では、フルパッケージ名（FQCN）を使用しないでください。

## Testing Strategy

### JUnit Testing

#### Service Classes
- サービスクラスのテストでは、`@SpringBootTest` と `@Transactional` を付与してください。

##### 正常系のテスト構成
- `setUp()` で、事前に登録すべきテストデータを登録します。このとき、`service`ではなく、`repository`を使用してください。
- `retrieve()`、`upsert()`、`delete()` が正常系のテストケースです。

## Property Files
- ローカル環境では `application.yml`と `application-test.yml` を使用します。
    - 上記はSpring Bootのデフォルトとなります。
- CI/CD環境では `application-cicd.yml` を使用します。
    - workflowファイルでは下記のように指定します。
    ```yaml
        env:
          SPRING_PROFILES_ACTIVE: cicd
        run: |
          cd spring
          ./gradlew test --no-daemon
    ```
- テストクラスには`@ActiveProfiles`アノテーションを付与**しません**。

## Database
- 本プロジェクトは、Docker コンテナ上の PostgreSQL に接続します。
- ユニットテストも同じ PostgreSQL を使用します。
    - H2 は禁止です。
- プロパティファイルの指定には注意してください。
    - ローカル環境でユニットテストを実行するときは、docker-compose の networks で接続するため、ホストは `postgres` となります。
    - CI/CD環境ででユニットテストを実行する場合は `localhost` に接続します。

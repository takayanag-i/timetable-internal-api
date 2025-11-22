-- Cloud SQL初期化スクリプト

-- PostgreSQL拡張機能の有効化（UUIDを使用するため必須）
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- アプリケーション用ユーザー（appuser）に必要最小限の権限を付与
GRANT CONNECT ON DATABASE postgres TO appuser;
GRANT USAGE ON SCHEMA public TO appuser;

-- 今後作成されるテーブルとシーケンスに対する権限を設定
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO appuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT ON SEQUENCES TO appuser;

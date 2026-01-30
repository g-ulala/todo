# アーキテクチャ

## プロジェクト構造

```
├── src/main/kotlin/com/example/todo/
│   ├── TodoApplication.kt          # Spring Bootエントリーポイント
│   ├── controller/
│   │   └── TodoController.kt       # REST APIコントローラー
│   ├── service/
│   │   └── TodoService.kt         # ビジネスロジック層
│   ├── entity/
│   │   └── TodoItem.kt            # JPAエンティティ
│   └── repository/
│       └── TodoRepository.kt      # データアクセス層
├── frontend/
│   ├── src/main.ts                # アプリロジック・API通信
│   ├── src/style.css              # スタイル
│   └── vite.config.js             # Viteビルド設定（プロキシ設定含む）
├── data/
│   └── todos.mv.db                # H2データベースファイル
└── src/main/resources/
    ├── application.properties      # Spring Boot設定
    └── static/                    # ビルド済みフロントエンド配置先
```

## レイヤー構成

### バックエンド

**レイヤードアーキテクチャ**:
```
Controller → Service → Repository → Database
```

- **controller**: プレゼンテーション層（REST API エンドポイント）
  - `TodoController.kt`: HTTP リクエストの受付とレスポンス返却
  - リクエストパラメータの検証とサービス層の呼び出し

- **service**: ビジネスロジック層
  - `TodoService.kt`: Todo の CRUD ビジネスロジック
  - トランザクション管理（`@Transactional`）
  - ビジネスルールの実装とバリデーション

- **entity**: ドメインモデル層
  - `TodoItem.kt`: Todo データモデル（JPA エンティティ）

- **repository**: データアクセス層
  - `TodoRepository.kt`: データベースアクセスインターフェース
  - Spring Data JPA による自動実装

- **data**: H2 データベース（ファイルベース、永続化）

### フロントエンド
- **main.ts**: DOM 操作、API 通信、状態管理
- **style.css**: UI スタイリング
- **vite.config.js**: 開発サーバー設定、プロキシ設定

## デプロイ

本番環境では、Vite でビルドしたフロントエンドを `src/main/resources/static/` に配置し、Spring Boot が静的ファイルとして配信します。

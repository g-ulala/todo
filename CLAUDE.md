# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

Kotlin (Spring Boot) と TypeScript (Vite) で構築されたフルスタックTodoアプリケーション。データはメモリ上に保存され、サーバー再起動で消失する。

## 開発コマンド

### バックエンド (Kotlin/Spring Boot)
```bash
./gradlew build          # ビルド
./gradlew test           # テスト実行
```
IntelliJ IDEAで `TodoApplication.kt` を実行してサーバー起動（ポート8080）

### フロントエンド (TypeScript/Vite)
```bash
cd frontend
npm install              # 依存関係インストール
npm run dev              # 開発サーバー起動（ポート5173、ホットリロード有効）
npm run build            # 本番ビルド（出力先: src/main/resources/static）
```

### 開発時のワークフロー
1. バックエンドをポート8080で起動
2. `frontend/` で `npm run dev` を実行
3. http://localhost:5173 にアクセス（APIリクエストは自動的に8080へプロキシ）

## アーキテクチャ

```
├── src/main/kotlin/com/example/todo/
│   ├── TodoApplication.kt    # Spring Bootエントリーポイント
│   └── TodoController.kt     # REST API実装（/api/todos）
├── frontend/
│   ├── src/main.ts           # アプリロジック・API通信
│   ├── src/style.css         # スタイル
│   └── vite.config.js        # Viteビルド設定（プロキシ設定含む）
└── src/main/resources/static/ # ビルド済みフロントエンド配置先
```

## API

| メソッド | パス | 説明 |
|:---|:---|:---|
| GET | `/api/todos` | 全Todo取得 |
| POST | `/api/todos` | Todo追加（body: `{"text": "..."}`) |
| PUT | `/api/todos/{id}` | Todo更新（チェック状態変更） |
| DELETE | `/api/todos/{id}` | Todo削除 |

## 技術スタック

- バックエンド: Kotlin, Spring Boot 3.2.0, Java 17
- フロントエンド: TypeScript 5.2, Vite 5.0
- ビルド: Gradle 8.4, npm

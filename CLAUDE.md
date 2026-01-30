# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

Kotlin (Spring Boot) と TypeScript (Vite) で構築されたフルスタックTodoアプリケーション。データはH2データベースで永続化されます。

## ドキュメント

詳細は以下のドキュメントを参照してください。

- **[開発ガイド](docs/development.md)** - セットアップ、開発コマンド、ワークフロー
- **[API 仕様](docs/api.md)** - REST API エンドポイントとデータモデル
- **[アーキテクチャ](docs/architecture.md)** - プロジェクト構造と技術スタック

## クイックスタート

**バックエンド起動（ポート8080）:**
- IntelliJ IDEA で `TodoApplication.kt` を右クリック → Run

**フロントエンド起動（ポート5173）:**
```bash
cd frontend
npm install
npm run dev
```

http://localhost:5173 にアクセス

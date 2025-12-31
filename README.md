# Todo Web App

Kotlin (Spring Boot) と TypeScript (Vite) で構築されたWeb版Todoアプリケーションです。

## 技術スタック

*   **バックエンド**: Kotlin, Spring Boot 3
*   **フロントエンド**: TypeScript, Vite, CSS (Vanilla)
*   **ビルドツール**: Gradle, npm

## プロジェクト構成

*   `src/main/kotlin/com/example/todo/`
    *   `TodoApplication.kt`: Spring Bootのエントリーポイント
    *   `TodoController.kt`: REST APIの実装
*   `frontend/`: フロントエンドのソースコード
    *   `src/main.ts`: アプリケーションロジック（API通信、DOM操作）
    *   `src/style.css`: スタイル定義
    *   `index.html`: エントリーポイントHTML
    *   `vite.config.js`: Viteビルド設定（プロキシ設定含む）

## 開発環境のセットアップ

1.  **Node.jsのインストール**: [公式サイト](https://nodejs.org/)からLTS版をインストールしてください。
2.  **依存関係のインストール**:
    ```bash
    cd frontend
    npm install
    ```

## 実行方法

### 開発モード (ホットリロード有効)

フロントエンドの変更を即座に反映させながら開発する場合の推奨手順です。

1.  **バックエンド起動**:
    IntelliJ IDEAで `TodoApplication.kt` を実行します (ポート 8080)。
2.  **フロントエンド起動**:
    ターミナルで以下を実行します。
    ```bash
    cd frontend
    npm run dev
    ```
3.  ブラウザで **[http://localhost:5173](http://localhost:5173)** にアクセスします。
    *   APIリクエスト (`/api/...`) はViteによって自動的にバックエンド (`localhost:8080`) へ転送されます。

### 本番ビルドモード (Spring Boot単体実行)

フロントエンドをビルドしてSpring Bootに埋め込み、単一のアプリケーションとして実行する場合の手順です。

1.  **フロントエンドのビルド**:
    ```bash
    cd frontend
    npm run build
    ```
    ビルド成果物が `src/main/resources/static` に出力されます。
2.  **バックエンド起動**:
    IntelliJ IDEAで `TodoApplication.kt` を実行します。
3.  ブラウザで **[http://localhost:8080](http://localhost:8080)** にアクセスします。

## 機能

*   **Todoの追加**: テキストを入力して追加ボタンまたはEnterキーで作成
*   **完了管理**: チェックボックスで完了/未完了を切り替え
*   **削除**: 削除ボタンでタスクを削除（確認ダイアログあり）

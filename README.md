# Todo Web App

Kotlin (Spring Boot) と TypeScript (Vite) で構築されたWeb版Todoアプリケーションです。

## ✨ 主な機能

*   **Todoの追加**: テキストを入力して追加ボタンまたはEnterキーで作成
*   **完了管理**: チェックボックスで完了/未完了を切り替え
*   **削除**: 削除ボタンでタスクを削除（確認ダイアログあり）

## 🛠️ 技術スタック

*   **バックエンド**: Kotlin, Spring Boot 3
*   **フロントエンド**: TypeScript, Vite, CSS (Vanilla)
*   **ビルドツール**: Gradle, npm

## 📂 プロジェクト構成

*   `src/main/kotlin/com/example/todo/`
    *   `TodoApplication.kt`: Spring Bootのエントリーポイント
    *   `TodoController.kt`: REST APIの実装
*   `frontend/`: フロントエンドのソースコード
    *   `src/main.ts`: アプリケーションロジック（API通信、DOM操作）
    *   `src/style.css`: スタイル定義
    *   `index.html`: エントリーポイントHTML
    *   `vite.config.js`: Viteビルド設定（プロキシ設定含む）
*   `delete_old_html.py`: 古いビルド成果物を削除するユーティリティスクリプト

## 🚀 実行方法

### 開発モード (ホットリロード有効)

フロントエンドの変更を即座に反映させながら開発する場合の推奨手順です。

1.  **バックエンド起動**:
    IntelliJ IDEAで `TodoApplication.kt` を実行します (ポート 8080)。
2.  **依存関係のインストール**:
    ```bash
    cd frontend
    npm install
    ```
3.  **フロントエンド起動**:
    ターミナルで以下を実行します。
    ```bash
    cd frontend
    npm run dev
    ```
4.  ブラウザで **[http://localhost:5173](http://localhost:5173)** にアクセスします。
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

## 📝 APIエンドポイント

| メソッド | パス | 説明 |
|:---|:---|:---|
| `GET` | `/api/todos` | すべてのTodo項目を取得します。 |
| `POST` | `/api/todos` | 新しいTodo項目を追加します。リクエストボディ: `{"text": "..."}` |
| `PUT` | `/api/todos/{id}` | 既存のTodo項目を更新します（主にチェック状態の変更）。 |
| `DELETE` | `/api/todos/{id}` | 指定したIDのTodo項目を削除します。 |

## ⚠️ 注意点

*   **データ永続性**: このアプリケーションのTodoデータはサーバーのメモリ上に一時的に保存されるだけです。**サーバーを再起動すると、すべてのデータは失われます。**

## 🔧 ユーティリティ

### `delete_old_html.py`
本番用のフロントエンドビルド (`npm run build`) を実行する前に、`src/main/resources/static` ディレクトリに残っている可能性のある古い `index.html` を削除するためのスクリプトです。これにより、ビルド間のキャッシュ問題を回避できます。

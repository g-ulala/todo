# 開発ガイド

## 開発コマンド

### バックエンド (Kotlin/Spring Boot)

#### IntelliJ IDEA（推奨）
1. プロジェクトを開く
2. Gradle ツールウィンドウ（右サイドバー）から:
   - `Tasks > build > build` でビルド
   - `Tasks > verification > test` でテスト実行
3. `TodoApplication.kt` を右クリック → Run でサーバー起動（ポート8080）

#### コマンドライン
**Windows:**
```powershell
gradlew.bat build        # ビルド
gradlew.bat test         # テスト実行
```

**Linux/Mac:**
```bash
./gradlew build          # ビルド
./gradlew test           # テスト実行
```

**注意**: Gradle Wrapper（`gradlew.bat` / `gradlew`）が存在しない場合は、IntelliJ IDEA の Gradle ツールウィンドウから `Tasks > build > wrapper` を実行して生成してください。

### フロントエンド (TypeScript/Vite)
```bash
cd frontend
npm install              # 依存関係インストール
npm run dev              # 開発サーバー起動（ポート5173、ホットリロード有効）
npm run build            # 本番ビルド（出力先: src/main/resources/static）
```

## 開発時のワークフロー

1. バックエンドをポート8080で起動
2. `frontend/` で `npm run dev` を実行
3. http://localhost:5173 にアクセス（APIリクエストは自動的に8080へプロキシ）

## 技術スタック

- バックエンド: Kotlin, Spring Boot 3.2.0, Java 17
- データベース: H2（ファイルベース、`data/todos.mv.db`）
- フロントエンド: TypeScript 5.2, Vite 5.0
- ビルド: Gradle 8.4, npm

## セットアップ時の注意

初回起動前に `data/` ディレクトリを作成してください:
```bash
mkdir data
```
または、IntelliJ IDEA でプロジェクトルートに `data` フォルダを作成。

## H2 データベースコンソール

開発時にデータを確認したい場合は、H2 コンソールにアクセスできます:
1. バックエンドを起動
2. http://localhost:8080/h2-console にアクセス
3. JDBC URL: `jdbc:h2:file:./data/todos` を入力
4. ユーザー名: `sa`、パスワード: (空欄)
5. Connect をクリック

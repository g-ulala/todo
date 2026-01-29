# SQLite永続化機能の設計

**作成日**: 2026-01-30
**目的**: メモリベースのTodoアプリをSQLiteデータベースで永続化する

## 概要

現在のメモリベース（`MutableList`）のTodoストレージを、Spring Data JPAとSQLiteに置き換えます。既存のREST APIインターフェースは変更せず、フロントエンドへの影響を最小限に抑えます。

## アーキテクチャ

### 技術スタック
- **ORM**: Spring Data JPA (Hibernate)
- **データベース**: SQLite 3.44.1
- **スキーマ管理**: Hibernate自動生成（ddl-auto=update）
- **データ保存先**: `data/todos.db`（gitignoreで除外）

### 依存関係

`build.gradle.kts`に以下を追加：

```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.3.1.Final")
}
```

## データモデル

### TodoItemエンティティ

```kotlin
@Entity
@Table(name = "todos")
data class TodoItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var text: String,

    @Column(nullable = false)
    var isChecked: Boolean = false
)
```

**変更点**:
- JPAアノテーション追加（`@Entity`, `@Id`, `@GeneratedValue`）
- `id`にデフォルト値`0`を設定（新規作成時用）
- SQLiteのAUTOINCREMENTで主キー自動生成

### TodoRepository

```kotlin
interface TodoRepository : JpaRepository<TodoItem, Long>
```

Spring Data JPAが自動実装するメソッド：
- `findAll()`: 全件取得
- `save()`: 作成・更新
- `findById()`: ID検索
- `deleteById()`: 削除

## バックエンド実装

### TodoController

メモリベースの実装から`TodoRepository`を使用するように変更：

```kotlin
@RestController
@RequestMapping("/api/todos")
class TodoController(private val repository: TodoRepository) {

    @GetMapping
    fun getTodos(): List<TodoItem> = repository.findAll()

    @PostMapping
    fun addTodo(@RequestBody request: Map<String, String>): TodoItem {
        val text = request["text"] ?: ""
        val todo = TodoItem(text = text)
        return repository.save(todo)
    }

    @PutMapping("/{id}")
    fun updateTodo(@PathVariable id: Long, @RequestBody todoUpdate: TodoItem): TodoItem {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found")
        }.also {
            it.text = todoUpdate.text
            it.isChecked = todoUpdate.isChecked
            repository.save(it)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long) {
        if (!repository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found")
        }
        repository.deleteById(id)
    }
}
```

**主な変更点**:
- `MutableList`と`AtomicLong`を削除
- コンストラクタで`TodoRepository`を注入
- 存在しないIDへのリクエストに404エラーを返す

### application.properties

`src/main/resources/application.properties`に追加：

```properties
spring.datasource.url=jdbc:sqlite:data/todos.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## フロントエンド実装

### エラーハンドリングとモーダルUI

404エラー時にモダンなモーダルダイアログを表示します。

#### モーダルの特徴
- 半透明の背景オーバーレイ
- 中央配置、シャドウ付きカード
- フェードイン/アウトアニメーション
- OKボタンまたは外側クリックで閉じる

#### main.tsの修正

エラーハンドリング関数を追加：

```typescript
function showErrorModal(message: string): void {
  const overlay = document.createElement('div');
  overlay.className = 'modal-overlay';

  const modal = document.createElement('div');
  modal.className = 'modal';
  modal.innerHTML = `
    <h2>エラー</h2>
    <p>${message}</p>
    <button class="modal-btn">OK</button>
  `;

  overlay.appendChild(modal);
  document.body.appendChild(overlay);

  const closeModal = () => {
    overlay.classList.add('fade-out');
    setTimeout(() => overlay.remove(), 300);
  };

  modal.querySelector('.modal-btn')!.addEventListener('click', closeModal);
  overlay.addEventListener('click', (e) => {
    if (e.target === overlay) closeModal();
  });
}
```

fetch関数でエラーレスポンスをキャッチ：

```typescript
async function toggleTodo(todo: TodoItem): Promise<void> {
  try {
    const response = await fetch(`${API_URL}/${todo.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ ...todo, isChecked: !todo.isChecked })
    });

    if (!response.ok) {
      const error = await response.json();
      showErrorModal(error.message || 'データが見つかりません');
      return;
    }

    fetchTodos();
  } catch (error) {
    console.error('Error toggling todo:', error);
    showErrorModal('通信エラーが発生しました');
  }
}
```

#### style.cssへの追加

```css
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  text-align: center;
}

.modal h2 {
  margin-top: 0;
  color: #e74c3c;
}

.modal-btn {
  margin-top: 1rem;
  padding: 0.5rem 2rem;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.modal-btn:hover {
  background: #2980b9;
}

.fade-out {
  animation: fadeOut 0.3s;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes fadeOut {
  from { opacity: 1; }
  to { opacity: 0; }
}
```

## Git設定

`.gitignore`に追加：

```
# SQLite database
data/
*.db
*.db-journal
```

## 実装手順

1. `build.gradle.kts`に依存関係を追加
2. `application.properties`にデータベース設定を追加
3. `TodoItem.kt`にJPAアノテーションを追加
4. `TodoRepository.kt`を新規作成
5. `TodoController.kt`をリポジトリベースに書き換え
6. `.gitignore`を更新
7. `frontend/src/main.ts`にエラーハンドリングとモーダル関数を追加
8. `frontend/src/style.css`にモーダルスタイルを追加

## テスト手順

### 1. バックエンド起動
IntelliJ IDEAで`TodoApplication.kt`を実行

確認ポイント：
- コンソールに`Hibernate: create table todos ...`のログが表示される
- `data/todos.db`ファイルが生成される

### 2. フロントエンド起動
```bash
cd frontend
npm run dev
```

### 3. 動作確認

**永続性テスト**:
1. ブラウザで http://localhost:5173 にアクセス
2. Todoを複数追加
3. サーバーを停止・再起動
4. データが復元されることを確認

**エラーハンドリングテスト**:
1. ブラウザの開発者ツールでネットワークを監視
2. 存在しないTodoを操作（手動でIDを変更するなど）
3. モーダルでエラーメッセージが表示されることを確認

## 期待される結果

- サーバー再起動後もTodoデータが保持される
- `data/todos.db`にSQLite形式でデータが保存される
- 存在しないデータへのリクエストに対して、ユーザーフレンドリーなエラー表示
- 既存のAPIインターフェースは変更なし（フロントエンドの互換性を維持）

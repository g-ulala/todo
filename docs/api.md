# API 仕様

## エンドポイント

| メソッド | パス | 説明 |
|:---|:---|:---|
| GET | `/api/todos` | 全Todo取得 |
| POST | `/api/todos` | Todo追加（body: `{"text": "..."}`) |
| PUT | `/api/todos/{id}` | Todo更新（チェック状態変更） |
| DELETE | `/api/todos/{id}` | Todo削除 |

## データモデル

```typescript
interface Todo {
  id: number;
  text: string;
  completed: boolean;
}
```

## 注意事項

- データは H2 データベース（`data/todos.mv.db`）に保存され、永続化される
- 初回起動時に `data/` ディレクトリが必要（存在しない場合は作成）
- API のベースパスは `/api`
- 開発時は Vite のプロキシ設定により localhost:5173 → localhost:8080 へ自動転送
- H2 コンソールは http://localhost:8080/h2-console でアクセス可能

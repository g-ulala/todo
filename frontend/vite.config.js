import { defineConfig } from 'vite'

export default defineConfig({
  build: {
    outDir: '../src/main/resources/static',
    emptyOutDir: true, // ビルド前に出力先を空にする
  },
  server: {
    proxy: {
      '/api': 'http://localhost:8080' // 開発中のAPIリクエストをバックエンドに転送
    }
  }
})
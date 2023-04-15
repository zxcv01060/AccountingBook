# 記帳App(Android)

## 開發環境及使用技術
---

* 電腦：Macbook M1 Pro
* IDE：[Android Studio](https://developer.android.com/studio)
* 程式語言：[Kotlin](https://kotlinlang.org/) 1.8.10(JDK 17)
* 主要套件：[Jetpack Compose](https://developer.android.com/jetpack/compose?hl=zh-tw)
* 樣式套件：[Material 3](https://m3.material.io/)
* 資料庫ORM套件：[SQL Delight](https://github.com/cashapp/sqldelight)

## 注意事項

1. 目前是自己用的，畫面完全沒有經過設計
2. 此App的最低版本為Android Oreo(26)

## 螢幕截圖
---

## 功能
---

## 開發指令
---

> 以下指令皆在專案根目錄執行，Windows開頭請改成`./gradlew.bat`

* 查看有哪些Gradle任務可以使用
  ```shell
  ./gradlew :module:domain:tasks
  ```

* 產生當前版本的資料庫結構
  ```shell
  ./gradlew :module:domain:generateMainAccountingBookDatabaseSchema
  ```

* 產生資料庫相關類別
  ```shell
  ./gradlew :module:domain:generateMainAccountingBookDatabaseInterface
  ```

* 驗證Migration
  ```shell
  ./gradlew :module:domain:verifySqlDelightMigration
  ```

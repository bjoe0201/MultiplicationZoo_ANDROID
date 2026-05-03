# 九九乘法動物園 (Multiplication Zoo) — 新專案規劃

## Context

基於現有的「數數動物園」(CountNumberZoo) 專案架構，建立一個全新的 Android 專案「九九乘法動物園」。核心概念從「數動物數量」改為「乘法表練習」，使用柵欄/園區的視覺方式呈現乘法：每個柵欄裡有 N 隻動物 × M 個柵欄 = 總數。

---

## 1. 專案設定

- **新專案目錄**: 另外建立獨立專案（例如 `MultiplicationZoo_Android`）
- **Package**: `com.example.multiplicationzoo`
- **Tech Stack**: 完全相同（Kotlin, Compose, Material3, AGP 9.1.1, Kotlin 2.2.10, Compose BOM 2026.02.01, minSdk 31, targetSdk 36）
- **依賴**: 相同的 `libs.versions.toml`

---

## 2. 專案結構

```
app/src/main/java/com/example/multiplicationzoo/
├── MainActivity.kt                    — Screen enum nav, TTS lifecycle
├── data/
│   ├── Animal.kt                      ← 直接複用 (20 animals)
│   ├── AppStrings.kt                  ← 大幅修改 (乘法相關字串)
│   ├── AppVersion.kt                  ← 新建
│   ├── GameSettings.kt                ← 修改 (selectedGroups 取代 maxCount/layoutMode)
│   ├── GameResult.kt                  ← 直接複用 (LeaderboardEntry)
│   └── LeaderboardRepository.kt       ← 直接複用 (DataStore top-10)
├── game/
│   ├── GameState.kt                   ← 修改 (RoundState 改為乘法欄位)
│   ├── GameViewModel.kt               ← 大幅修改 (乘法出題/計分邏輯)
│   └── MultiplicationQuestion.kt      ← 新建 (題目資料類別 + 題庫生成)
├── tts/
│   └── TtsManager.kt                  ← 小幅修改 (新增 speakMultiplication)
├── ui/
│   ├── screens/
│   │   ├── LanguageScreen.kt          ← 直接複用
│   │   ├── HomeScreen.kt              ← 小幅修改 (標題文字)
│   │   ├── SettingsScreen.kt          ← 修改 (乘法組別選擇器取代 maxCount/layout)
│   │   ├── GameScreen.kt              ← 大幅修改 (算式顯示 + PenGrid)
│   │   └── ResultScreen.kt            ← 小幅修改 (計分標籤)
│   ├── components/
│   │   ├── PenGrid.kt                 ← 新建 (柵欄網格)
│   │   ├── AnimalPen.kt               ← 新建 (單一柵欄元件)
│   │   ├── AnswerButtons.kt           ← 直接複用
│   │   └── AnimatedAnimal.kt          ← 直接複用
│   └── theme/
│       ├── Color.kt                   ← 複用 + 新增 FenceBrown
│       ├── Type.kt                    ← 直接複用
│       └── Theme.kt                   ← 小改 (rename theme)
```

---

## 3. 資料模型變更

### GameSettings.kt
```kotlin
data class GameSettings(
    val rounds: Int = 5,                           // 3-20
    val selectedGroups: Set<Int> = (2..9).toSet(),  // 要測試的乘法組 (2~9)
    val voiceMode: VoiceMode = VoiceMode.NUMBER,   // 4 modes: NONE/NUMBER/NUMBER_WITH_ANIMAL/PEN_COUNT
    val language: AppLanguage = AppLanguage.CHINESE
)
// 移除: maxCount, layoutMode
// 新增: selectedGroups
// 變更: VoiceMode 新增 PEN_COUNT
```

### GameState.kt — RoundState
```kotlin
data class RoundState(
    val animal: Animal,
    val perPen: Int,               // 每個柵欄的動物數 (被乘數)
    val penCount: Int,             // 柵欄數 (乘數)
    val product: Int,              // perPen * penCount = 正確答案
    val answers: List<Int>,        // 5 個答案選項
    val correctAnswer: Int,        // = product
    val tappedIndices: Set<Int>,   // 全域 tap index (0..product-1)
    val selectedAnswer: Int?,
    val isCorrect: Boolean?
)
```

### MultiplicationQuestion.kt — 新建
```kotlin
data class MultiplicationQuestion(
    val multiplicand: Int,  // 每組幾隻 (被乘數)
    val multiplier: Int     // 幾組 (乘數)
)

fun generateQuestionPool(selectedGroups: Set<Int>): List<MultiplicationQuestion> {
    // 每個選定的組 N → 產生 N×1, N×2, ..., N×9
    // 洗牌後回傳
}
```

---

## 4. 遊戲邏輯

### 出題
- 從選定的乘法組產生題庫（如選了 {3,5,7} → 3×1..3×9, 5×1..5×9, 7×1..7×9 共 27 題）
- 洗牌後取 `rounds` 題
- 每題隨機分配一種動物

### 干擾答案生成（乘法專用）
- 鄰近乘法結果：`multiplicand × (multiplier±1)`
- 常見錯誤：`multiplicand + multiplier`（加法混淆）
- 附近數值：`product ± random(1..5)`
- 確保 4 個干擾項 > 0、不重複、≠ 正確答案

### 計分公式
```
每題得分 = 10 × selectedGroups.size
score = 每題得分 × (correctCount - wrongCount), 最低 0
```
- 選 1 組：每題 10 分
- 選 8 組（全選）：每題 80 分
- 鼓勵挑戰更多組

### 點擊計數
- 玩家可點擊柵欄內的動物來數數
- tap index 為全域 flat index：`penIndex * perPen + animalIndexInPen`
- TTS 唸出目前計數（同原版）

---

## 5. UI 元件

### GameScreen 佈局
```
┌──────────────────────────────┐
│  第 3/10 題           [退出]  │  ← 進度條
│  ━━━━━━━━━━━━━━━━━━━━━━━━━   │
│                              │
│       3  ×  7  =  ?         │  ← 算式（大字、置中、簡潔式）
│   每個柵欄 3 隻，共 7 個柵欄   │  ← 小字提示（多語系）
│                              │
│  ┌─────┐ ┌─────┐ ┌─────┐   │  ← PenGrid
│  │🐶🐶🐶│ │🐶🐶🐶│ │🐶🐶🐶│   │
│  └─────┘ └─────┘ └─────┘   │
│  ┌─────┐ ┌─────┐ ┌─────┐   │
│  │🐶🐶🐶│ │🐶🐶🐶│ │🐶🐶🐶│   │
│  └─────┘ └─────┘ └─────┘   │
│  ┌─────┐                     │
│  │🐶🐶🐶│                     │
│  └─────┘                     │
│                              │
│  [12] [18] [21] [24] [27]   │  ← 答案按鈕
└──────────────────────────────┘
```

### PenGrid.kt（新建）
- 使用 `BoxWithConstraints` 自適應大小
- 柵欄排列：penCount → penCols/penRows（如 9 個柵欄 → 3×3）
- 每個柵欄內部動物排列：perPen → mini-grid（如 9 隻 → 3×3）

### AnimalPen.kt（新建）
- `Card` + 棕色邊框（`Color(0xFF8D6E63)`）模擬柵欄
- 淺綠色背景（`Color(0xFFE8F5E9)`）模擬草地
- 左上角小標籤顯示柵欄編號（可選）
- 點擊動物觸發 tap + TTS

### 大數處理（如 9×9=81）
- 9 個柵欄排 3×3，每柵欄 9 隻排 3×3
- emoji 字型縮小至約 16-20sp
- 空間不足時啟用垂直滾動

---

## 6. 設定畫面變更

### 移除
- Max Count 滑桿
- Layout Mode（Grid/Scattered）切換

### 新增：乘法組別選擇器
```
┌─────────────────────────────┐
│ 乘法組別                      │
│                              │
│  [2] [3] [4] [5]            │
│  [6] [7] [8] [9]            │
│                              │
│  [全選/取消全選]               │
│  已選 3 組: 2, 5, 9          │
└─────────────────────────────┘
```
- 每個數字 2~9 為 toggle 按鈕
- 至少選 1 組（防止取消最後一個）
- 全選/取消全選 按鈕

### 保留
- 題數滑桿 (3-20)
- 語言切換
- 清除排行榜

### 語音模式變更（4 種模式）
原版 3 種 VoiceMode 擴充為 4 種：

| 模式 | 點擊動物時 TTS | 說明 |
|------|----------------|------|
| `NONE` | 不發音 | 靜音 |
| `NUMBER` | "1", "2", "3"... | 純數字計數 |
| `NUMBER_WITH_ANIMAL` | "1 隻狗", "2 隻狗"... | 數字+動物名（同原版） |
| `PEN_COUNT` | 每點完一欄自動唸 "第 1 欄，3 隻狗" | 強調乘法結構，點完整欄時觸發 |

```kotlin
enum class VoiceMode { NONE, NUMBER, NUMBER_WITH_ANIMAL, PEN_COUNT }
```

---

## 7. TTS 變更

新增方法：
```kotlin
fun speakMultiplication(multiplicand: Int, multiplier: Int, lang: AppLanguage) {
    // ZH: "3 乘以 7"
    // EN: "3 times 7"
    // JA: "3 かける 7"
}

fun speakPenComplete(penNumber: Int, count: Int, animal: Animal, lang: AppLanguage) {
    // ZH: "第 1 欄，3 隻狗"
    // EN: "Pen 1, 3 dogs"
    // JA: "1ばんめ、3びきの犬"
}
```

保留：`speakGameCount`（點擊計數）、`speakFeedback`（答對/答錯）、`speakAnimalName`（首頁動物發音）

---

## 8. AppStrings 新增字串

| 函數 | 中文 | English | 日本語 |
|------|------|---------|--------|
| `appTitle` | 九九乘法動物園 | Multiplication Zoo | かけ算どうぶつえん |
| `multiplicationGroups` | 乘法組別 | Multiplication Groups | かけ算のグループ |
| `selectAll` | 全選 | Select All | すべて選ぶ |
| `deselectAll` | 取消全選 | Deselect All | すべて外す |
| `selectedGroupsLabel(n)` | 已選 N 組 | N groups selected | N グループ選択中 |
| `penHint(per, count)` | 每個柵欄 %d 隻，共 %d 個柵欄 | %d per pen, %d pens | 1つの囲いに%d匹、%dつの囲い |
| `tapToCount` | 點動物來數數！ | Tap to count! | タップしてかぞえよう！ |
| `groupLabel(n)` | %d 的乘法 | Group %d | %dのだん |
| `atLeastOneGroup` | 至少選擇一組 | At least one group | 少なくとも1つ選んでください |

---

## 9. 設計決策

| 決策 | 說明 |
|------|------|
| 交換律 | 3×7 和 7×3 都可出現，因為視覺不同（3隻/欄×7欄 vs 7隻/欄×3欄），符合課綱 |
| 題庫不足 | 每組 9 題，最少 1 組=9 題；最多 8 組=72 題。rounds 上限 20，不會超出 |
| 柵欄排列 | penCount 1→1col, 2→2col, 3→3col, 4→2col, 5→3col, 6→3col, 7→4col, 8→4col, 9→3col |
| 無 Scattered 模式 | 柵欄一定是結構化排列，不需要散佈模式 |

---

## 10. 可直接複用的檔案

- `data/Animal.kt`
- `data/GameResult.kt`
- `data/LeaderboardRepository.kt`
- `ui/components/AnswerButtons.kt`
- `ui/components/AnimatedAnimal.kt`
- `ui/screens/LanguageScreen.kt`
- `ui/theme/Type.kt`
- `gradle/libs.versions.toml`

---

## 11. 實作順序

1. 建立新專案 + Gradle 設定
2. 複製可直接複用的檔案
3. 建立資料模型：`GameSettings`, `GameState`, `MultiplicationQuestion`
4. 實作 `GameViewModel`（乘法出題 + 計分）
5. 建立 `AnimalPen` 元件（單一柵欄）
6. 建立 `PenGrid` 元件（柵欄網格 + BoxWithConstraints）
7. 修改 `GameScreen`（算式顯示 + PenGrid）
8. 修改 `SettingsScreen`（組別選擇器）
9. 修改 `HomeScreen`（標題）
10. 更新 `AppStrings`（所有新字串）
11. 更新 `TtsManager`（乘法語音）
12. 組裝 `MainActivity`
13. 測試邊界情境（9×9, 2×1, 單組, 全組）

---

## 12. 驗證方式

1. **建置**: `./gradlew clean assembleDebug` 確認編譯通過
2. **功能測試**:
   - 設定只選 1 組 → 確認只出該組題目
   - 設定全選 → 確認各組都有出題
   - 9×9 → 確認 9 個柵欄各 9 隻正常顯示
   - 2×1 → 確認 1 個柵欄 2 隻正常顯示
   - 點擊動物計數 → TTS 正確發音
   - 答題 → 回饋動畫 + TTS
   - 排行榜 → 儲存/顯示正常
3. **三語切換**: 每個畫面切換 ZH/EN/JA 確認文字正確
4. **計分**: 選 3 組答對 1 題 = 30 分；選 8 組答對 1 題 = 80 分

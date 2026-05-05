# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk (signed/installable)

# Run unit tests
./gradlew test

# Run a single unit test
./gradlew :app:testDebugUnitTest --tests "com.example.multiplicationzoo.ExampleUnitTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean

# Install on connected device
./gradlew :app:installDebug
```

## Architecture & Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material3
- **Min SDK**: 31, **Target/Compile SDK**: 36
- **Build system**: Gradle with Kotlin DSL (`.kts`), version catalog at `gradle/libs.versions.toml`
- **AGP**: 9.1.1, **Kotlin**: 2.2.10, **Compose BOM**: 2026.02.01
- **State management**: `GameViewModel` + `StateFlow`
- **Navigation**: Simple `when(screen)` state-based (no Jetpack Navigation library)
- **Persistence**: AndroidX DataStore Preferences (leaderboard)
- **TTS**: Android built-in `TextToSpeech` API

## Dependencies

Defined in `gradle/libs.versions.toml`:

| Alias | Artifact | Purpose |
|-------|----------|---------|
| `androidx.lifecycle.viewmodel.compose` | lifecycle-viewmodel-compose 2.10.0 | ViewModel in Compose |
| `androidx.datastore.preferences` | datastore-preferences 1.1.4 | Leaderboard persistence |
| `androidx.compose.material.icons.extended` | material-icons-extended | Settings / back icons |

## Project Structure

```
app/src/main/java/com/example/multiplicationzoo/
├── MainActivity.kt                  — Screen enum nav host, TTS lifecycle
├── data/
│   ├── Animal.kt                    — 20 animals (emoji + names in ZH/EN/JA)
│   ├── AppStrings.kt                — all UI strings in 3 languages (Unicode escapes only)
│   ├── AppVersion.kt                — VERSION_CODE / VERSION_NAME / BUILD_DATE constants
│   ├── GameSettings.kt              — GameSettings data class (selectedGroups, rounds, voiceMode, language)
│   ├── GameResult.kt                — LeaderboardEntry model
│   └── LeaderboardRepository.kt    — DataStore read/write, top-10
├── game/
│   ├── GameState.kt                 — RoundState (perPen, penCount, tappedIndices, answers…), GameState
│   ├── GameViewModel.kt             — round logic, scoring, distractor generation, tapAnimal (no-toggle)
│   └── MultiplicationQuestion.kt   — question pool generation (multiplicand × multiplier)
├── tts/
│   └── TtsManager.kt               — TTS wrapper: speakGameCount / speakCountWithAnimal /
│                                      speakFeedback / speakAnswerThenFeedback / speakAnimalName /
│                                      speakMultiplication / speakPenComplete
└── ui/
    ├── screens/
    │   ├── LanguageScreen.kt        — first-launch language picker (ZH / EN / JA)
    │   ├── HomeScreen.kt            — animated animals background + nav buttons + TTS cycle
    │   ├── SettingsScreen.kt        — rounds slider, group checkboxes, voice mode, language
    │   ├── GameScreen.kt            — main gameplay + feedback overlay (local state capture fix)
    │   ├── ResultScreen.kt          — score, emoji name picker, inline leaderboard
    │   └── LeaderboardScreen.kt     — top-10 list with TopAppBar
    ├── components/
    │   ├── PenGrid.kt               — BoxWithConstraints bestLayout() for pen cols/rows
    │   ├── AnimalPen.kt             — weight(1f) + inner BoxWithConstraints for dynamic animal size
    │   ├── AnswerButtons.kt         — 5 colored answer buttons with correct/wrong highlight
    │   └── AnimatedAnimal.kt        — AnimatedAnimal (CircleShape highlight) + FloatingAnimal
    └── theme/
        ├── Color.kt                 — child-friendly palette (SkyBlue, GrassGreen, SunshineYellow…)
        ├── Type.kt                  — large SansSerif typography
        └── Theme.kt                 — forced light theme (no dark, no dynamic color)
```

## App Flow (6 Screens)

```
Screen.LANGUAGE → Screen.HOME ⇄ Screen.SETTINGS
                      ↓                  Screen.LANGUAGE
                  Screen.GAME → Screen.RESULT → Screen.HOME
                                              ↗ (Play Again → GAME)
```

- **LANGUAGE**: Full-screen picker; stored in `GameSettings.language`; accessible again via "切換語言" on Home
- **HOME**: 20 floating animated animals; tap animal → TTS cycles through ZH→EN→JA starting from current language
- **SETTINGS**: Rounds (3–15), multiplication groups (2–9, multi-select), voice mode, language switcher; 3-step clear leaderboard confirm
- **GAME**: `PenGrid` fills available space via `BoxWithConstraints`; tap animals (no-toggle, add-only); tap answer → TTS(number) → TTS(feedback); auto-advance after 1.4s + 0.3s exit animation
- **RESULT**: Score + stats; emoji animal name picker (up to 8 emojis, repeatable, backspace); DataStore leaderboard top-10
- **LEADERBOARD**: TopAppBar + card list; accessible from Home and Settings

## Key Logic

### Scoring
`score = correctCount × 10 × selectedGroups.size - wrongCount × …`, net negative clamped to 0.

### Distractor Generation (`GameViewModel.generateAnswers`)
5 answer choices including the correct product:
- `N × (M ± 1)` neighbor products
- Addition-confusion distractor `N + M`
- Random nearby values to fill remaining slots

### Pen Layout (`PenGrid.kt` → `bestLayout()`)
Tries all `cols` from 1 to `penCount`, picks the one whose resulting pen aspect ratio is closest to 1.2 (slightly wider than tall). Works correctly for both portrait and landscape.

### Animal Sizing (`AnimalPen.kt`)
Uses `Column` + `weight(1f)` for the animal area so Compose measures the exact remaining height after the label. Inner `BoxWithConstraints` then tries all `itemsPerRow` from 1 to `perPen`, picks the one that maximises `min(cellWidth, cellHeight)`.

### Feedback Overlay (Bug Fix)
Local `remember(currentIndex)` state (`feedbackIsCorrect`, `feedbackCorrectAnswer`, `showFeedback`) prevents the overlay from reading the new round's `isCorrect = null` during the exit animation. Sequence: show overlay → delay 1400ms → hide → delay 300ms → `onNext()`.

### TTS on Answer Selection (`TtsManager.speakAnswerThenFeedback`)
1. `QUEUE_FLUSH`: speak correct answer number
2. `QUEUE_ADD`: speak "正確/Correct/正解" or "錯誤/Incorrect/不正解"

### Animal Tap — No Toggle
`GameViewModel.tapAnimal`: if index already in `tappedIndices`, return early (no un-tap). Prevents double-counting.

### Language TTS Cycling (HomeScreen)
`LANG_CYCLE = [CHINESE, ENGLISH, JAPANESE]`. `startIdx = LANG_CYCLE.indexOf(language)`. Each tap: speak `LANG_CYCLE[(startIdx + cycleOffset) % 3]`, then `cycleOffset++`. Resets to 0 when `language` changes.

## Multi-Language Support

All UI text goes through `AppStrings` object — **never use Android string resources for UI text**.  
**IMPORTANT**: All non-ASCII (CJK) characters in `.kt` files must be written as `\uXXXX` Unicode escape sequences to avoid encoding corruption by tools.

`AppLanguage` enum: `CHINESE / ENGLISH / JAPANESE`  
TTS locale: ZH → `Locale.SIMPLIFIED_CHINESE`, EN → `Locale.ENGLISH`, JA → `Locale.JAPAN`

## Theme

Forced light mode only — no dark theme, no dynamic color. Child-friendly palette in `Color.kt`:
`SkyBlue`, `GrassGreen`, `SunshineYellow`, `OrangePeel`, `FenceBrown`, `CorrectGreen`, `WrongRed`, `BackgroundColor`, `AnswerColors[]`.

---

## 🔖 Version Update Checklist

每次發布新版本前，**必須同步更新以下 7 個地方**：

| # | 檔案 | 欄位 | 說明 |
|---|------|------|------|
| 1 | `app/build.gradle.kts` | `versionCode` | 整數遞增（1, 2, 3…） |
| 2 | `app/build.gradle.kts` | `versionName` | 語意版本字串（如 "1.0.1"） |
| 3 | `app/src/main/java/com/example/multiplicationzoo/data/AppVersion.kt` | `VERSION_CODE` | 同 versionCode |
| 4 | `app/src/main/java/com/example/multiplicationzoo/data/AppVersion.kt` | `VERSION_NAME` | 同 versionName（顯示於主畫面右下角） |
| 5 | `app/src/main/java/com/example/multiplicationzoo/data/AppVersion.kt` | `BUILD_DATE` | 發布日期（格式 "YYYY-MM-DD"） |
| 6 | `README.md` | 頁首 `**📦 Version：X.X.X**` | 更新顯示版本 |
| 7 | `CHANGELOG.md` | 新增 `## [X.X.X] — YYYY-MM-DD` 區塊 | 記錄本次變更內容 |

### GitHub Releases 發佈流程（含 APK）

> 目標：每次正式發佈只上傳**已簽章、可安裝**的 `app-release.apk`。

#### 0. 發佈前檢查

- 確認已完成上方 7 個版本位置更新。
- 確認 GitHub CLI 已登入：`gh auth status`。
- 確認目前分支是 `main`，且遠端是正確 repo：`git branch --show-current`、`git remote -v`。
- 正式公開發佈建議使用私有 release keystore：
  - 複製 `keystore.properties.example` → `keystore.properties`
  - 填入 `RELEASE_STORE_FILE`、`RELEASE_STORE_PASSWORD`、`RELEASE_KEY_ALIAS`、`RELEASE_KEY_PASSWORD`
  - **不可提交** `keystore.properties`、`.jks`、`.keystore`
- 若未設定正式 keystore，`assembleRelease` 會 fallback 使用 debug keystore 簽章；APK 可側載安裝，但只適合測試/家庭分享，不建議作為正式公開長期簽章。

#### 1. 建置並驗證 APK

Windows PowerShell：

```powershell
# 建置已簽章、可安裝的 release APK
.\gradlew.bat clean assembleRelease -x test

# 產物必須是這個檔案
.\app\build\outputs\apk\release\app-release.apk
```

Windows PowerShell 檢查檔案是否存在：

```powershell
Test-Path .\app\build\outputs\apk\release\app-release.apk
Get-Item .\app\build\outputs\apk\release\app-release.apk
```

簽章驗證（Windows PowerShell）：

```powershell
$sdk = if (Test-Path .\local.properties) {
  Get-Content .\local.properties | Where-Object { $_ -like 'sdk.dir=*' } | ForEach-Object { ($_ -replace '^sdk.dir=', '') -replace '\\:', ':' -replace '\\\\', '\' }
}
if (-not $sdk) { $sdk = $env:ANDROID_HOME }
if (-not $sdk) { $sdk = $env:ANDROID_SDK_ROOT }
if (-not $sdk) { throw 'Android SDK path not found. Set sdk.dir in local.properties or ANDROID_HOME/ANDROID_SDK_ROOT.' }
$apksigner = Get-ChildItem -Path (Join-Path $sdk 'build-tools') -Recurse -Filter 'apksigner.bat' | Sort-Object FullName -Descending | Select-Object -First 1
if (-not $apksigner) { throw 'apksigner.bat not found under Android SDK build-tools.' }
& $apksigner.FullName verify --verbose --print-certs .\app\build\outputs\apk\release\app-release.apk
```

驗證結果至少要看到：

```text
Verifies
Verified using v2 scheme (APK Signature Scheme v2): true
Number of signers: 1
```

⚠️ **不要上傳** `app-release-unsigned.apk`；Android 會顯示「應用程式套件無效」或無法完成安裝。

#### 2. 測試、提交、推送

Windows PowerShell：

```powershell
.\gradlew.bat test

git status
git add -A
git commit -m "release: vX.X.X — <簡短說明>"
git push origin main
```

#### 3. 建立並推送 tag

Windows PowerShell：

```powershell
git fetch origin --tags --prune
git tag --list "vX.X.X"

# 若 tag 不存在才建立
git tag -a vX.X.X -m "Release vX.X.X"
git push origin vX.X.X
```

注意：如果 `vX.X.X` tag 已存在但指向舊 commit，先停止並確認是否要刪除/重建 tag，避免 release 綁到錯誤版本。

確認 tag 指向目前 release commit：

```powershell
git rev-parse HEAD
git rev-list -n 1 vX.X.X
```

#### 4. 建立 GitHub Release 並上傳 APK

> 只上傳 `app/build/outputs/apk/release/app-release.apk`。不要上傳 debug APK、unsigned APK、mapping 或任何 keystore/私鑰檔。

Windows PowerShell：

```powershell
gh release create vX.X.X ".\app\build\outputs\apk\release\app-release.apk" `
  --title "🦁 Multiplication Zoo vX.X.X — <標題>" `
  --notes "<本次更新說明>" `
  --latest
```

若 Release 已存在但要替換 APK：

```powershell
gh release upload vX.X.X ".\app\build\outputs\apk\release\app-release.apk" --clobber
```

> 注意：`gh release upload --clobber` 只替換 asset，不會自動更新 release title/notes/tag 指向；必要時另外使用 `gh release edit`。

發佈後驗證：

```powershell
gh release view vX.X.X --json tagName,name,url,publishedAt,assets
gh release list --limit 100
```

Release asset 必須包含：

```text
app-release.apk
```

#### 5. 刪除舊 GitHub Releases，只保留最新

確認最新 `vX.X.X` 已存在且 asset 包含 `app-release.apk` 後，再刪除舊 Release：

```powershell
gh release list --limit 100
gh release delete v舊版本 --yes
gh release list --limit 100
```

範例：

```powershell
gh release delete v1.0.1 --yes
```

> 預設只刪除 GitHub Release 與其 assets，不刪除 git tag，通常較安全。若確定連舊 tag 也要刪除，才使用 `gh release delete v舊版本 --cleanup-tag --yes` 或手動刪除遠端/本機 tag。

#### 6. 安裝注意事項

- 若手機已安裝同 package name 但不同簽章的舊版 APK，直接更新可能失敗；請先解除安裝舊版再安裝新版。
- 解除安裝通常會清除 App 資料；若需要保留資料，先確認是否有備份策略。
- 若新版 `versionCode` 小於或等於手機已安裝版本，Android 可能拒絕降版安裝；每版必須遞增 `versionCode`。
- 若本版使用 debug fallback 簽章，未來改用正式私有 release keystore 時，已安裝 debug fallback 簽章版本的裝置也會因簽章不同而需要先解除安裝。
- debug fallback 簽章的 `app-release.apk` 可安裝、適合測試/家庭分享；正式公開長期發佈建議使用私有 release keystore 並持續保存同一把 keystore。
- GitHub Release 只上傳 `app-release.apk`，不要上傳 debug APK、unsigned APK、mapping、keystore 或任何私鑰檔。
- 發佈後建議實機/平板下載 GitHub Release asset 安裝一次，確認下載檔案可正常安裝與啟動。


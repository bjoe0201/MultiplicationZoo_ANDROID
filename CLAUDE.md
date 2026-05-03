# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

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

每次發布新版本前，**必須同步更新以下 6 個地方**：

| # | 檔案 | 欄位 | 說明 |
|---|------|------|------|
| 1 | `app/build.gradle.kts` | `versionCode` | 整數遞增（1, 2, 3…） |
| 2 | `app/build.gradle.kts` | `versionName` | 語意版本字串（如 "1.0.1"） |
| 3 | `app/src/main/java/com/example/multiplicationzoo/data/AppVersion.kt` | `VERSION_CODE` | 同 versionCode |
| 4 | `app/src/main/java/com/example/multiplicationzoo/data/AppVersion.kt` | `VERSION_NAME` | 同 versionName（顯示於主畫面右下角） |
| 5 | `app/src/main/java/com/example/multiplicationzoo/data/AppVersion.kt` | `BUILD_DATE` | 發布日期（格式 "YYYY-MM-DD"） |
| 6 | `README.md` | 頁首 `**📦 Version：X.X.X**` | 更新顯示版本 |
| 7 | `CHANGELOG.md` | 新增 `## [X.X.X] — YYYY-MM-DD` 區塊 | 記錄本次變更內容 |

### 發布流程

```bash
# 1. 更新上述 7 個地方後，執行完整建置
./gradlew clean assembleRelease -x test

# 2. 提交 git
git add -A
git commit -m "release: vX.X.X — <簡短說明>"

# 3. 推送到 GitHub
git push origin main

# 4. 建立 git tag
git tag -a vX.X.X -m "Release vX.X.X"
git push origin vX.X.X

# 5. 建立 GitHub Release 並附上 APK
gh release create vX.X.X "app/build/outputs/apk/release/app-release-unsigned.apk" \
  --title "🦁 Multiplication Zoo vX.X.X — <標題>" \
  --notes "<本次更新說明>"
```


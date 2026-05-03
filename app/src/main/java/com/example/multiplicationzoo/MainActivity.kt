package com.example.multiplicationzoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.multiplicationzoo.data.LeaderboardRepository
import com.example.multiplicationzoo.data.VoiceMode
import com.example.multiplicationzoo.game.GameViewModel
import com.example.multiplicationzoo.tts.TtsManager
import com.example.multiplicationzoo.ui.screens.GameScreen
import com.example.multiplicationzoo.ui.screens.HomeScreen
import com.example.multiplicationzoo.ui.screens.LanguageScreen
import com.example.multiplicationzoo.ui.screens.LeaderboardScreen
import com.example.multiplicationzoo.ui.screens.ResultScreen
import com.example.multiplicationzoo.ui.screens.SettingsScreen
import com.example.multiplicationzoo.ui.theme.MultiplicationZooTheme
import kotlinx.coroutines.launch

enum class Screen {
    HOME, GAME, RESULT, SETTINGS, LANGUAGE, LEADERBOARD
}

class MainActivity : ComponentActivity() {

    private val gameViewModel: GameViewModel by viewModels()
    private lateinit var ttsManager: TtsManager
    private lateinit var leaderboardRepository: LeaderboardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ttsManager = TtsManager(this)
        leaderboardRepository = LeaderboardRepository(this)

        setContent {
            MultiplicationZooTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    MainScreen(
                        gameViewModel = gameViewModel,
                        ttsManager = ttsManager,
                        leaderboardRepository = leaderboardRepository
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}

@Composable
fun MainScreen(
    gameViewModel: GameViewModel,
    ttsManager: TtsManager,
    leaderboardRepository: LeaderboardRepository
) {
    val currentScreen = remember { mutableStateOf(Screen.HOME) }
    val gameSettings by gameViewModel.gameSettings.collectAsState()
    val gameState by gameViewModel.gameState.collectAsState()
    val leaderboard by leaderboardRepository.leaderboard.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    when (currentScreen.value) {
        Screen.HOME -> {
            HomeScreen(
                language = gameSettings.language,
                onStartGame = {
                    gameViewModel.initializeGame(gameSettings)
                    currentScreen.value = Screen.GAME
                },
                onSettings = {
                    currentScreen.value = Screen.SETTINGS
                },
                onLeaderboard = {
                    currentScreen.value = Screen.LEADERBOARD
                },
                onLanguage = {
                    currentScreen.value = Screen.LANGUAGE
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Screen.GAME -> {
            if (gameState.currentRoundIndex < gameState.totalRounds && gameState.rounds.isNotEmpty()) {
                val currentRound = gameState.rounds[gameState.currentRoundIndex]
                GameScreen(
                    currentRound = currentRound,
                    currentIndex = gameState.currentRoundIndex,
                    totalRounds = gameState.totalRounds,
                    language = gameSettings.language,
                    onAnimalTap = { animalIndex ->
                        val updatedRound = gameViewModel.tapAnimal(animalIndex) ?: return@GameScreen
                        when (gameSettings.voiceMode) {
                            VoiceMode.NONE -> Unit
                            VoiceMode.NUMBER -> {
                                ttsManager.speakGameCount(updatedRound.currentCount, gameSettings.language)
                            }
                            VoiceMode.NUMBER_WITH_ANIMAL -> {
                                ttsManager.speakCountWithAnimal(
                                    updatedRound.currentCount,
                                    updatedRound.animal,
                                    gameSettings.language
                                )
                            }
                            VoiceMode.PEN_COUNT -> {
                                val penIndex = animalIndex / updatedRound.perPen
                                val countInPen = updatedRound.tappedIndices.count {
                                    it / updatedRound.perPen == penIndex
                                }
                                val isJustSelected = animalIndex in updatedRound.tappedIndices
                                if (isJustSelected && countInPen == updatedRound.perPen) {
                                    ttsManager.speakPenComplete(
                                        penIndex + 1,
                                        updatedRound.perPen,
                                        updatedRound.animal,
                                        gameSettings.language
                                    )
                                }
                            }
                        }
                    },
                    onAnswerSelected = { answer ->
                        val isCorrect = gameViewModel.selectAnswer(answer) ?: return@GameScreen
                        ttsManager.speakFeedback(isCorrect, gameSettings.language)
                    },
                    onRoundPresented = { multiplicand, multiplier ->
                        if (gameSettings.voiceMode != VoiceMode.NONE) {
                            ttsManager.speakMultiplication(multiplicand, multiplier, gameSettings.language)
                        }
                    },
                    onNext = {
                        gameViewModel.nextQuestion()
                    },
                    onQuit = {
                        gameViewModel.quitGame()
                        currentScreen.value = Screen.HOME
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else if (gameState.isGameOver) {
                currentScreen.value = Screen.RESULT
            }
        }

        Screen.RESULT -> {
            val result = gameViewModel.getGameResult()
            ResultScreen(
                score = result.score,
                correctCount = result.correctCount,
                wrongCount = result.wrongCount,
                totalRounds = result.totalRounds,
                language = gameSettings.language,
                onHome = {
                    scope.launch {
                        leaderboardRepository.addScore(result)
                    }
                    currentScreen.value = Screen.HOME
                },
                onPlayAgain = {
                    gameViewModel.initializeGame(gameSettings)
                    currentScreen.value = Screen.GAME
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Screen.SETTINGS -> {
            SettingsScreen(
                settings = gameSettings,
                onSettingsChanged = { newSettings ->
                    gameViewModel.updateGameSettings(newSettings)
                },
                onBack = {
                    currentScreen.value = Screen.HOME
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Screen.LANGUAGE -> {
            LanguageScreen(
                currentLanguage = gameSettings.language,
                onLanguageSelected = { language ->
                    gameViewModel.updateGameSettings(gameSettings.copy(language = language))
                    currentScreen.value = Screen.HOME
                },
                onBack = {
                    currentScreen.value = Screen.HOME
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Screen.LEADERBOARD -> {
            LeaderboardScreen(
                entries = leaderboard,
                language = gameSettings.language,
                onClear = {
                    scope.launch {
                        leaderboardRepository.clearLeaderboard()
                    }
                },
                onBack = {
                    currentScreen.value = Screen.HOME
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

package com.example.multiplicationzoo.game

import androidx.lifecycle.ViewModel
import com.example.multiplicationzoo.data.GameResult
import com.example.multiplicationzoo.data.GameSettings
import com.example.multiplicationzoo.data.getAnimalList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _gameSettings = MutableStateFlow(GameSettings())
    val gameSettings: StateFlow<GameSettings> = _gameSettings.asStateFlow()

    private val animals = getAnimalList()
    private val random = Random(System.currentTimeMillis())

    fun initializeGame(settings: GameSettings) {
        _gameSettings.value = settings

        val questionPool = generateQuestionPool(settings.selectedGroups)
        val selectedQuestions = questionPool.take(settings.rounds)

        val rounds = selectedQuestions.map { question ->
            val animal = animals.random(random)
            val product = question.product
            val answers = generateAnswers(
                multiplicand = question.multiplicand,
                multiplier = question.multiplier
            )

            RoundState(
                animal = animal,
                perPen = question.multiplicand,
                penCount = question.multiplier,
                product = product,
                answers = answers,
                correctAnswer = product
            )
        }

        _gameState.value = GameState(
            rounds = rounds,
            totalRounds = settings.rounds,
            scorePerRound = 10 * settings.selectedGroups.size
        )
    }

    fun tapAnimal(animalIndex: Int): RoundState? {
        val state = _gameState.value
        if (state.currentRoundIndex >= state.rounds.size) return null

        val currentRound = state.rounds[state.currentRoundIndex]
        if (currentRound.selectedAnswer != null) return null

        // 不重複累計：已點擊的動物不再取消，只加入新的
        if (animalIndex in currentRound.tappedIndices) return currentRound
        val newTappedIndices = currentRound.tappedIndices + animalIndex

        val newRound = currentRound.copy(tappedIndices = newTappedIndices)
        val newRounds = state.rounds.toMutableList()
        newRounds[state.currentRoundIndex] = newRound

        _gameState.value = state.copy(rounds = newRounds)
        return newRound
    }

    fun selectAnswer(answer: Int): Boolean? {
        val state = _gameState.value
        if (state.currentRoundIndex >= state.rounds.size) return null

        val currentRound = state.rounds[state.currentRoundIndex]
        val isCorrect = answer == currentRound.correctAnswer

        val newRound = currentRound.copy(
            selectedAnswer = answer,
            isCorrect = isCorrect
        )

        val newRounds = state.rounds.toMutableList()
        newRounds[state.currentRoundIndex] = newRound

        val newCorrectCount = if (isCorrect) state.correctCount + 1 else state.correctCount
        val newWrongCount = if (!isCorrect) state.wrongCount + 1 else state.wrongCount

        _gameState.value = state.copy(
            rounds = newRounds,
            correctCount = newCorrectCount,
            wrongCount = newWrongCount
        )
        return isCorrect
    }

    fun nextQuestion() {
        val state = _gameState.value
        val nextIndex = state.currentRoundIndex + 1

        if (nextIndex >= state.totalRounds) {
            _gameState.value = state.copy(
                currentRoundIndex = nextIndex,
                isGameOver = true
            )
        } else {
            _gameState.value = state.copy(currentRoundIndex = nextIndex)
        }
    }

    fun quitGame() {
        _gameState.value = GameState()
    }

    fun calculateFinalScore(): Int {
        val state = _gameState.value
        val net = state.correctCount - state.wrongCount
        return if (net <= 0) 0 else net * state.scorePerRound
    }

    fun getGameResult(): GameResult {
        val state = _gameState.value
        val settings = _gameSettings.value

        return GameResult(
            score = calculateFinalScore(),
            correctCount = state.correctCount,
            wrongCount = state.wrongCount,
            totalRounds = state.totalRounds,
            selectedGroups = settings.selectedGroups
        )
    }

    fun updateGameSettings(settings: GameSettings) {
        _gameSettings.value = settings
    }

    private fun generateAnswers(multiplicand: Int, multiplier: Int): List<Int> {
        val correct = multiplicand * multiplier
        val answers = mutableSetOf(correct)
        val maxProduct = 81

        // Neighboring multiplication results (N x (M ± 1))
        val lowerNeighbor = multiplicand * (multiplier - 1)
        val upperNeighbor = multiplicand * (multiplier + 1)
        if (lowerNeighbor > 0) answers.add(lowerNeighbor)
        if (upperNeighbor in 1..maxProduct) answers.add(upperNeighbor)

        // Common confusion: addition instead of multiplication
        answers.add((multiplicand + multiplier).coerceIn(1, maxProduct))

        while (answers.size < 5) {
            val offset = random.nextInt(1, 6)
            val candidate = if (random.nextBoolean()) {
                correct + offset
            } else {
                correct - offset
            }
            if (candidate in 1..maxProduct && candidate != correct) {
                answers.add(candidate)
            }
        }

        return answers.toList().shuffled(random)
    }
}

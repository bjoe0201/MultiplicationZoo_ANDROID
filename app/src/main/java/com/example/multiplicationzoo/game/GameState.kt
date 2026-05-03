package com.example.multiplicationzoo.game

import com.example.multiplicationzoo.data.Animal

data class RoundState(
    val animal: Animal,
    val perPen: Int,
    val penCount: Int,
    val product: Int,
    val answers: List<Int>,
    val correctAnswer: Int,
    val tappedIndices: Set<Int> = emptySet(),
    val selectedAnswer: Int? = null,
    val isCorrect: Boolean? = null
) {
    val currentCount: Int get() = tappedIndices.size
}

data class GameState(
    val currentRoundIndex: Int = 0,
    val rounds: List<RoundState> = emptyList(),
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val isGameOver: Boolean = false,
    val totalRounds: Int = 0,
    val scorePerRound: Int = 10
)


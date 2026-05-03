package com.example.multiplicationzoo.game

import com.example.multiplicationzoo.data.GameSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {

    @Test
    fun initializeGame_withSingleGroup_generatesOnlyThatGroup() {
        val viewModel = GameViewModel()
        viewModel.initializeGame(
            GameSettings(
                rounds = 5,
                selectedGroups = setOf(3)
            )
        )

        val rounds = viewModel.gameState.value.rounds
        assertEquals(5, rounds.size)
        assertTrue(rounds.all { it.perPen == 3 })
        assertTrue(rounds.all { it.penCount in 1..9 })
    }

    @Test
    fun initializeGame_answersAreValidAndContainCorrectAnswer() {
        val viewModel = GameViewModel()
        viewModel.initializeGame(
            GameSettings(
                rounds = 5,
                selectedGroups = setOf(2, 4, 7)
            )
        )

        val rounds = viewModel.gameState.value.rounds
        assertEquals(5, rounds.size)

        rounds.forEach { round ->
            assertEquals(5, round.answers.size)
            assertEquals(5, round.answers.toSet().size)
            assertTrue(round.correctAnswer in round.answers)
            assertTrue(round.answers.all { it in 1..81 })
            assertEquals(round.perPen * round.penCount, round.correctAnswer)
        }
    }

    @Test
    fun calculateFinalScore_neverGoesBelowZero() {
        val viewModel = GameViewModel()
        viewModel.initializeGame(
            GameSettings(
                rounds = 3,
                selectedGroups = setOf(2, 3)
            )
        )

        repeat(3) { roundIndex ->
            val round = viewModel.gameState.value.rounds[roundIndex]
            val wrongAnswer = round.answers.first { it != round.correctAnswer }
            viewModel.selectAnswer(wrongAnswer)
            if (roundIndex < 2) {
                viewModel.nextQuestion()
            }
        }

        assertEquals(0, viewModel.calculateFinalScore())
    }

    @Test
    fun initializeGame_withAllGroups_keepsPerPenWithinSelectedGroups() {
        val viewModel = GameViewModel()
        val groups = (2..9).toSet()
        viewModel.initializeGame(
            GameSettings(
                rounds = 10,
                selectedGroups = groups
            )
        )

        val rounds = viewModel.gameState.value.rounds
        assertEquals(10, rounds.size)
        assertTrue(rounds.all { it.perPen in groups })
    }

    @Test
    fun initializeGame_scorePerRound_dependsOnSelectedGroupCount() {
        val viewModel = GameViewModel()
        viewModel.initializeGame(
            GameSettings(
                rounds = 5,
                selectedGroups = setOf(2, 5, 9)
            )
        )

        assertEquals(30, viewModel.gameState.value.scorePerRound)
    }
}


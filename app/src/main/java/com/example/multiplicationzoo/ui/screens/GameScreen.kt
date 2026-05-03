package com.example.multiplicationzoo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.AppLanguage
import com.example.multiplicationzoo.data.AppStrings
import com.example.multiplicationzoo.game.RoundState
import com.example.multiplicationzoo.ui.components.AnswerButtons
import com.example.multiplicationzoo.ui.components.PenGrid
import com.example.multiplicationzoo.ui.theme.BackgroundColor
import com.example.multiplicationzoo.ui.theme.CorrectGreen
import com.example.multiplicationzoo.ui.theme.PrimaryColor
import com.example.multiplicationzoo.ui.theme.WrongRed
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    currentRound: RoundState,
    currentIndex: Int,
    totalRounds: Int,
    language: AppLanguage,
    onAnimalTap: (Int) -> Unit,
    onAnswerSelected: (Int) -> Unit,
    onRoundPresented: (Int, Int) -> Unit,
    onNext: () -> Unit,
    onQuit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showExitDialog by remember { mutableStateOf(false) }

    // Capture feedback data into local state so it doesn't change
    // when onNext() advances to the next round during the exit animation.
    var showFeedback by remember(currentIndex) { mutableStateOf(false) }
    var feedbackIsCorrect by remember(currentIndex) { mutableStateOf(false) }
    var feedbackCorrectAnswer by remember(currentIndex) { mutableIntStateOf(0) }

    LaunchedEffect(currentIndex) {
        onRoundPresented(currentRound.perPen, currentRound.penCount)
    }

    // Auto-advance: capture result first, show overlay, then advance
    LaunchedEffect(currentRound.isCorrect) {
        if (currentRound.isCorrect != null) {
            feedbackIsCorrect = currentRound.isCorrect == true
            feedbackCorrectAnswer = currentRound.correctAnswer
            showFeedback = true
            delay(1400)
            showFeedback = false
            delay(300) // let exit animation finish before advancing
            onNext()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Top section ──────────────────────────────────
            Column {
                // Progress text + Quit button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = AppStrings.question(language, currentIndex + 1, totalRounds),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor
                    )
                    Button(
                        onClick = { showExitDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                        modifier = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text(AppStrings.quit(language), fontSize = 13.sp, color = Color(0xFF555555))
                    }
                }

                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { (currentIndex + 1f) / totalRounds },
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryColor
                )
                Spacer(Modifier.height(12.dp))

                // Multiplication formula display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentRound.perPen.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                    Text(
                        text = " × ",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF555555),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Text(
                        text = currentRound.penCount.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                    Text(
                        text = " = ?",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF555555),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = AppStrings.penHint(language, currentRound.perPen, currentRound.penCount),
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = AppStrings.tapToCount(language),
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // ── Animal Pen Grid ───────────────────────────────
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                PenGrid(
                    animal = currentRound.animal,
                    perPen = currentRound.perPen,
                    penCount = currentRound.penCount,
                    tappedIndices = currentRound.tappedIndices,
                    onAnimalTap = onAnimalTap,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // ── Tapped count + Answer buttons ─────────────────
            Column {
                Text(
                    text = AppStrings.tappedCount(language, currentRound.currentCount),
                    fontSize = 14.sp,
                    color = Color(0xFF444444),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )

                AnswerButtons(
                    answers = currentRound.answers,
                    selectedAnswer = currentRound.selectedAnswer,
                    correctAnswer = currentRound.correctAnswer,
                    onAnswerSelected = onAnswerSelected
                )
            }
        }

        // ── Exit confirmation dialog ───────────────────────────
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = {
                    Text(
                        text = AppStrings.quitGameTitle(language),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = AppStrings.quitGameMessage(language),
                        fontSize = 15.sp
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showExitDialog = false
                        onQuit()
                    }) {
                        Text(AppStrings.quit(language), color = WrongRed, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text(AppStrings.cancel(language), color = PrimaryColor)
                    }
                },
                shape = RoundedCornerShape(20.dp)
            )
        }

        // ── Animated feedback overlay ─────────────────────────
        AnimatedVisibility(
            visible = showFeedback,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (feedbackIsCorrect) CorrectGreen else WrongRed,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 48.dp, vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (feedbackIsCorrect) "\u2B50" else "\uD83D\uDE22",
                        fontSize = 48.sp
                    )
                    Text(
                        text = if (feedbackIsCorrect) AppStrings.correct(language)
                               else AppStrings.wrong(language),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (!feedbackIsCorrect) {
                        Text(
                            text = "$feedbackCorrectAnswer",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


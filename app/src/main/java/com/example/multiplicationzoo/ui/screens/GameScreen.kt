package com.example.multiplicationzoo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.AppLanguage
import com.example.multiplicationzoo.data.AppStrings
import com.example.multiplicationzoo.game.RoundState
import com.example.multiplicationzoo.ui.components.AnswerButtons
import com.example.multiplicationzoo.ui.components.PenGrid

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
    LaunchedEffect(currentIndex) {
        onRoundPresented(currentRound.perPen, currentRound.penCount)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        // Header with progress
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = AppStrings.question(language, currentIndex + 1, totalRounds),
                fontSize = 16.sp
            )
            Button(onClick = onQuit) {
                Text(AppStrings.quit(language), fontSize = 12.sp)
            }
        }
        
        LinearProgressIndicator(
            progress = { (currentIndex + 1f) / totalRounds },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

        // Multiplication formula
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentRound.perPen.toString(),
                fontSize = 32.sp
            )
            Text(
                text = " × ",
                fontSize = 32.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = currentRound.penCount.toString(),
                fontSize = 32.sp
            )
            Text(
                text = " = ?",
                fontSize = 32.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Hint text
        Text(
            text = AppStrings.penHint(language, currentRound.perPen, currentRound.penCount),
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = AppStrings.tapToCount(language),
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        // Pen grid
        PenGrid(
            animal = currentRound.animal,
            perPen = currentRound.perPen,
            penCount = currentRound.penCount,
            tappedIndices = currentRound.tappedIndices,
            onAnimalTap = onAnimalTap,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        
        // Current count display
        Text(
            text = AppStrings.tappedCount(language, currentRound.currentCount),
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Answer buttons
        AnswerButtons(
            answers = currentRound.answers,
            selectedAnswer = currentRound.selectedAnswer,
            onAnswerSelected = onAnswerSelected,
            enabled = currentRound.selectedAnswer == null
        )
        
        // Feedback
        if (currentRound.isCorrect != null) {
            Text(
                text = if (currentRound.isCorrect) {
                    AppStrings.correct(language)
                } else {
                    AppStrings.incorrect(language) + " (${currentRound.correctAnswer})"
                },
                fontSize = 18.sp,
                color = if (currentRound.isCorrect) {
                    Color.Green
                } else {
                    Color.Red
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(AppStrings.next(language))
            }
        }
    }
}



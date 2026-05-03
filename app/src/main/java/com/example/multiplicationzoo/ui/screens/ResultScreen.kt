package com.example.multiplicationzoo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.AppLanguage
import com.example.multiplicationzoo.data.AppStrings

@Composable
fun ResultScreen(
    score: Int,
    correctCount: Int,
    wrongCount: Int,
    totalRounds: Int,
    language: AppLanguage,
    onHome: () -> Unit,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = AppStrings.gameOver(language),
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Card(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = AppStrings.score(language),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = score.toString(),
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Text(
                    text = AppStrings.correctCount(language, correctCount),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = AppStrings.wrongCount(language, wrongCount),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = AppStrings.totalCount(language, totalRounds),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Button(
            onClick = onPlayAgain,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(AppStrings.playAgain(language))
        }

        Button(
            onClick = onHome,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(AppStrings.home(language))
        }
    }
}


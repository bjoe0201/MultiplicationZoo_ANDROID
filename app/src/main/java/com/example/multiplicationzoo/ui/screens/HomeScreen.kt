package com.example.multiplicationzoo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.AppLanguage
import com.example.multiplicationzoo.data.AppStrings

@Composable
fun HomeScreen(
    language: AppLanguage,
    onStartGame: () -> Unit,
    onSettings: () -> Unit,
    onLeaderboard: () -> Unit,
    onLanguage: () -> Unit,
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
            text = AppStrings.appTitle(language),
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onStartGame,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(AppStrings.startGame(language))
        }

        Button(
            onClick = onSettings,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(AppStrings.settings(language))
        }

        Button(
            onClick = onLeaderboard,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(AppStrings.leaderboard(language))
        }

        Button(
            onClick = onLanguage,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(AppStrings.language(language))
        }
    }
}


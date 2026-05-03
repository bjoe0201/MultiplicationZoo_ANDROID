package com.example.multiplicationzoo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.AppLanguage
import com.example.multiplicationzoo.data.AppStrings
import com.example.multiplicationzoo.data.LeaderboardEntry

@Composable
fun LeaderboardScreen(
    entries: List<LeaderboardEntry>,
    language: AppLanguage,
    onClear: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = AppStrings.leaderboard(language),
                fontSize = 24.sp
            )
            Button(onClick = onBack) {
                Text(AppStrings.back(language), fontSize = 12.sp)
            }
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

        if (entries.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(AppStrings.noScoresYet(language))
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(entries) { entry ->
                    LeaderboardEntryCard(entry, language)
                }
            }
        }

        Button(
            onClick = onClear,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(AppStrings.clearLeaderboard(language))
        }
    }
}

@Composable
fun LeaderboardEntryCard(
    entry: LeaderboardEntry,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "#${entry.rank}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Score: ${entry.score}",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    text = "${entry.correctCount}/${entry.totalRounds}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    text = "Groups: ${entry.selectedGroups}",
                    fontSize = 10.sp,
                    modifier = Modifier.padding(2.dp)
                )
            }
            Text(
                text = entry.date,
                fontSize = 12.sp
            )
        }
    }
}


package com.example.multiplicationzoo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.AppLanguage
import com.example.multiplicationzoo.data.AppStrings
import com.example.multiplicationzoo.data.LeaderboardRepository
import com.example.multiplicationzoo.data.GameResult
import com.example.multiplicationzoo.data.getAnimalList
import com.example.multiplicationzoo.ui.theme.BackgroundColor
import com.example.multiplicationzoo.ui.theme.CorrectGreen
import com.example.multiplicationzoo.ui.theme.GrassGreen
import com.example.multiplicationzoo.ui.theme.PrimaryColor
import com.example.multiplicationzoo.ui.theme.SunshineYellow
import com.example.multiplicationzoo.ui.theme.WrongRed
import kotlinx.coroutines.launch

private const val MAX_NAME_EMOJIS = 8
private val ALL_ANIMALS_LIST = getAnimalList()

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ResultScreen(
    gameResult: GameResult,
    language: AppLanguage,
    repository: LeaderboardRepository,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = language
    val entries by repository.leaderboard.collectAsState(initial = emptyList())
    var selectedEmojis by remember { mutableStateOf(listOf<String>()) }
    var scoreSaved by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        Text(text = "\uD83C\uDFC6", fontSize = 64.sp)
        Text(text = AppStrings.score(lang), fontSize = 22.sp, color = Color.Gray)
        Text(
            text = gameResult.score.toString(),
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            StatChip(
                label = AppStrings.correctLabel(lang),
                value = gameResult.correctCount.toString(),
                color = CorrectGreen
            )
            StatChip(
                label = AppStrings.wrongLabel(lang),
                value = gameResult.wrongCount.toString(),
                color = WrongRed
            )
        }

        Spacer(Modifier.height(24.dp))

        // ── Emoji name picker ─────────────────────────────────
        if (!scoreSaved) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        AppStrings.yourName(lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(12.dp))

                    // 8 display slots
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(MAX_NAME_EMOJIS) { idx ->
                            val emoji = selectedEmojis.getOrNull(idx)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (emoji != null) Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
                                    )
                            ) {
                                Text(
                                    text = emoji ?: "…",
                                    fontSize = if (emoji != null) 26.sp else 18.sp,
                                    color = if (emoji != null) Color.Unspecified else Color.LightGray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Backspace
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (selectedEmojis.isNotEmpty()) WrongRed.copy(alpha = 0.15f)
                                    else Color(0xFFEEEEEE)
                                )
                                .clickable(enabled = selectedEmojis.isNotEmpty()) {
                                    selectedEmojis = selectedEmojis.dropLast(1)
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "⌫",
                                fontSize = 20.sp,
                                color = if (selectedEmojis.isNotEmpty()) WrongRed else Color.Gray
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    val full = selectedEmojis.size >= MAX_NAME_EMOJIS
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        maxItemsInEachRow = 5,
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ALL_ANIMALS_LIST.forEach { animal ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (full) Color(0xFFEEEEEE) else Color(0xFFE8F5E9)
                                    )
                                    .clickable(enabled = !full) {
                                        selectedEmojis = selectedEmojis + animal.emoji
                                    }
                            ) {
                                Text(
                                    text = animal.emoji,
                                    fontSize = 28.sp,
                                    color = if (full) Color.Gray else Color.Unspecified
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (selectedEmojis.isNotEmpty()) {
                                scope.launch {
                                    repository.addScore(gameResult, selectedEmojis.joinToString(""))
                                    scoreSaved = true
                                }
                            }
                        },
                        enabled = selectedEmojis.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GrassGreen)
                    ) {
                        Text(AppStrings.saveScore(lang), color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        } else {
            Text(
                text = "✅ ${AppStrings.saveScore(lang)}!",
                color = CorrectGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── Inline leaderboard ────────────────────────────────
        if (entries.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "\uD83C\uDFC6 ${AppStrings.leaderboard(lang)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    entries.forEachIndexed { idx, entry ->
                        if (idx > 0) HorizontalDivider()
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            Arrangement.SpaceBetween,
                            Alignment.CenterVertically
                        ) {
                            Text(
                                "${idx + 1}. ${entry.playerName.ifBlank { "???" }}",
                                fontSize = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                entry.score.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryColor
                            )
                            Text(
                                "  ${entry.date}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = onPlayAgain,
                colors = ButtonDefaults.buttonColors(containerColor = GrassGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).height(56.dp)
            ) {
                Text(AppStrings.playAgain(lang), color = Color.White, fontSize = 16.sp)
            }
            Button(
                onClick = onHome,
                colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).height(56.dp)
            ) {
                Text(AppStrings.home(lang), color = Color(0xFF5D4037), fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun StatChip(label: String, value: String, color: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Column(
            Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 14.sp, color = color)
        }
    }
}

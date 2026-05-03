package com.example.multiplicationzoo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.AppLanguage
import com.example.multiplicationzoo.data.AppStrings
import com.example.multiplicationzoo.data.GameSettings
import com.example.multiplicationzoo.data.LeaderboardRepository
import com.example.multiplicationzoo.data.VoiceMode
import com.example.multiplicationzoo.ui.theme.BackgroundColor
import com.example.multiplicationzoo.ui.theme.OrangePeel
import com.example.multiplicationzoo.ui.theme.PrimaryColor
import com.example.multiplicationzoo.ui.theme.WrongRed
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit,
    onBack: () -> Unit,
    leaderboardRepository: LeaderboardRepository? = null,
    modifier: Modifier = Modifier
) {
    val lang = settings.language
    val scope = rememberCoroutineScope()
    var showAtLeastOneGroupMessage by remember { mutableStateOf(false) }
    // 0 = idle, 1 = first confirm, 2 = second confirm, 3 = done
    var clearState by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        AppStrings.settings(lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Rounds ────────────────────────────────────────
            SettingsCard {
                Text(
                    "${AppStrings.rounds(lang)}: ${settings.rounds}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Slider(
                    value = settings.rounds.toFloat(),
                    onValueChange = { onSettingsChanged(settings.copy(rounds = it.roundToInt())) },
                    valueRange = 3f..20f,
                    steps = 16
                )
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("3", fontSize = 14.sp, color = Color.Gray)
                    Text("20", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // ── Multiplication Groups ─────────────────────────
            SettingsCard {
                Text(
                    AppStrings.multiplicationGroups(lang),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 4,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (2..9).forEach { group ->
                        FilterChip(
                            selected = group in settings.selectedGroups,
                            onClick = {
                                val newGroups = settings.selectedGroups.toMutableSet()
                                if (group in newGroups) {
                                    if (newGroups.size > 1) {
                                        newGroups.remove(group)
                                        showAtLeastOneGroupMessage = false
                                    } else {
                                        showAtLeastOneGroupMessage = true
                                    }
                                } else {
                                    newGroups.add(group)
                                    showAtLeastOneGroupMessage = false
                                }
                                onSettingsChanged(settings.copy(selectedGroups = newGroups))
                            },
                            label = { Text(group.toString()) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onSettingsChanged(settings.copy(selectedGroups = (2..9).toSet()))
                            showAtLeastOneGroupMessage = false
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                    ) {
                        Text(AppStrings.selectAll(lang), color = Color.White, fontSize = 12.sp)
                    }
                    Button(
                        onClick = {
                            onSettingsChanged(settings.copy(selectedGroups = setOf(2)))
                            showAtLeastOneGroupMessage = false
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Text(AppStrings.deselectAll(lang), color = Color.White, fontSize = 12.sp)
                    }
                }
                Text(
                    text = AppStrings.selectedGroupsLabel(lang, settings.selectedGroups.size),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                if (showAtLeastOneGroupMessage) {
                    Text(
                        text = AppStrings.atLeastOneGroup(lang),
                        fontSize = 12.sp,
                        color = WrongRed,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // ── Voice Mode ────────────────────────────────────
            SettingsCard {
                Text(AppStrings.voiceMode(lang), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        VoiceMode.NONE to AppStrings.voiceModeNone(lang),
                        VoiceMode.NUMBER to AppStrings.voiceModeNumber(lang),
                        VoiceMode.NUMBER_WITH_ANIMAL to AppStrings.voiceModeNumberWithAnimal(lang),
                        VoiceMode.PEN_COUNT to AppStrings.voiceModePenCount(lang)
                    ).forEach { (mode, label) ->
                        val selected = settings.voiceMode == mode
                        Button(
                            onClick = { onSettingsChanged(settings.copy(voiceMode = mode)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) PrimaryColor else Color.LightGray
                            )
                        ) {
                            Text(label, color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }

            // ── Clear Leaderboard ─────────────────────────────
            if (leaderboardRepository != null) {
                SettingsCard {
                    Text(
                        AppStrings.clearLeaderboard(lang),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = WrongRed
                    )
                    Spacer(Modifier.height(10.dp))
                    when (clearState) {
                        0 -> Button(
                            onClick = { clearState = 1 },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WrongRed)
                        ) {
                            Text("🗑️  ${AppStrings.clearLeaderboard(lang)}", color = Color.White)
                        }
                        1 -> {
                            Text(AppStrings.clearConfirm1(lang), fontSize = 15.sp, color = Color(0xFF7B0000))
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { clearState = 0 },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                ) { Text(AppStrings.cancel(lang), color = Color.White) }
                                Button(
                                    onClick = { clearState = 2 },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = WrongRed)
                                ) { Text(AppStrings.clearConfirmBtn(lang), color = Color.White) }
                            }
                        }
                        2 -> {
                            Text(
                                AppStrings.clearConfirm2(lang),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7B0000)
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { clearState = 0 },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                ) { Text(AppStrings.cancel(lang), color = Color.White) }
                                Button(
                                    onClick = {
                                        scope.launch {
                                            leaderboardRepository.clearLeaderboard()
                                            clearState = 3
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B0000))
                                ) { Text(AppStrings.clearConfirmBtn(lang), color = Color.White) }
                            }
                        }
                        3 -> {
                            Text(
                                "✅ ${AppStrings.clearDone(lang)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            Spacer(Modifier.height(6.dp))
                            Button(
                                onClick = { clearState = 0 },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                            ) { Text(AppStrings.back(lang), color = Color.White) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

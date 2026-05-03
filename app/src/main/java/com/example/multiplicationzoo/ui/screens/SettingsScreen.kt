package com.example.multiplicationzoo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.AppLanguage
import com.example.multiplicationzoo.data.AppStrings
import com.example.multiplicationzoo.data.GameSettings
import com.example.multiplicationzoo.data.VoiceMode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAtLeastOneGroupMessage by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = AppStrings.settings(settings.language),
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Rounds selector
        Card(modifier = Modifier.padding(8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = AppStrings.rounds(settings.language),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Slider(
                    value = settings.rounds.toFloat(),
                    onValueChange = { newRounds ->
                        onSettingsChanged(settings.copy(rounds = newRounds.toInt()))
                    },
                    valueRange = 3f..20f,
                    steps = 16,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(settings.rounds.toString())
            }
        }

        // Multiplication groups selector
        Card(modifier = Modifier.padding(8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = AppStrings.multiplicationGroups(settings.language),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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

                // Select All / Deselect All buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onSettingsChanged(settings.copy(selectedGroups = (2..9).toSet()))
                            showAtLeastOneGroupMessage = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(AppStrings.selectAll(settings.language), fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            onSettingsChanged(settings.copy(selectedGroups = setOf(2)))
                            showAtLeastOneGroupMessage = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(AppStrings.deselectAll(settings.language), fontSize = 12.sp)
                    }
                }

                Text(
                    text = AppStrings.selectedGroupsLabel(settings.language, settings.selectedGroups.size),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                if (showAtLeastOneGroupMessage) {
                    Text(
                        text = AppStrings.atLeastOneGroup(settings.language),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }

        // Voice mode selector
        Card(modifier = Modifier.padding(8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = AppStrings.voiceMode(settings.language),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                VoiceMode.values().forEach { mode ->
                    FilterChip(
                        selected = settings.voiceMode == mode,
                        onClick = {
                            onSettingsChanged(settings.copy(voiceMode = mode))
                        },
                        label = {
                            Text(when (mode) {
                                VoiceMode.NONE -> AppStrings.voiceModeNone(settings.language)
                                VoiceMode.NUMBER -> AppStrings.voiceModeNumber(settings.language)
                                VoiceMode.NUMBER_WITH_ANIMAL -> AppStrings.voiceModeNumberWithAnimal(settings.language)
                                VoiceMode.PEN_COUNT -> AppStrings.voiceModePenCount(settings.language)
                            })
                        },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

        // Back button
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(AppStrings.back(settings.language))
        }
    }
}



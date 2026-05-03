package com.example.multiplicationzoo.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.AppLanguage
import com.example.multiplicationzoo.data.AppStrings
import com.example.multiplicationzoo.data.AppVersion
import com.example.multiplicationzoo.data.getAnimalList
import com.example.multiplicationzoo.ui.components.FloatingAnimal
import com.example.multiplicationzoo.ui.theme.BackgroundColor
import com.example.multiplicationzoo.ui.theme.GrassGreen
import com.example.multiplicationzoo.ui.theme.OrangePeel
import com.example.multiplicationzoo.ui.theme.PrimaryColor
import com.example.multiplicationzoo.ui.theme.SunshineYellow

private val LANG_CYCLE = listOf(AppLanguage.CHINESE, AppLanguage.ENGLISH, AppLanguage.JAPANESE)
private val ANIMALS = getAnimalList()

@Composable
fun HomeScreen(
    language: AppLanguage,
    onStartGame: () -> Unit,
    onSettings: () -> Unit,
    onLeaderboard: () -> Unit,
    onLanguage: () -> Unit,
    onSpeakAnimal: (com.example.multiplicationzoo.data.Animal, AppLanguage) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val startIdx = remember(language) { LANG_CYCLE.indexOf(language).coerceAtLeast(0) }
    var cycleOffset by remember(language) { mutableIntStateOf(0) }
    var reshuffleKey by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE3F2FD), BackgroundColor, Color(0xFFFFF9C4))
                )
            )
    ) {
        // Floating animated animals in background
        ANIMALS.forEachIndexed { index, animal ->
            FloatingAnimal(
                emoji = animal.emoji,
                index = index,
                reshuffleKey = reshuffleKey,
                onClick = {
                    val langToSpeak = LANG_CYCLE[(startIdx + cycleOffset) % 3]
                    onSpeakAnimal(animal, langToSpeak)
                    cycleOffset = (cycleOffset + 1) % 3
                }
            )
        }

        // Shuffle button – bottom left
        Button(
            onClick = { reshuffleKey++ },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
                .height(36.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.75f)),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
        ) {
            Text(
                "🔀 ${AppStrings.shuffle(language)}",
                fontSize = 12.sp,
                color = Color(0xFF555555)
            )
        }

        // Version – bottom right
        Text(
            text = "v${AppVersion.VERSION_NAME}",
            fontSize = 12.sp,
            color = Color(0xFF888888),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        )

        // Foreground UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = AppStrings.appTitle(language),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                )

                Spacer(Modifier.height(48.dp))

                Button(
                    onClick = onStartGame,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(72.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GrassGreen)
                ) {
                    Text(
                        text = AppStrings.start(language),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = onSettings,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePeel),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
                        Text(
                            " ${AppStrings.settings(language)}",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = onLeaderboard,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text("\uD83C\uDFC6", fontSize = 20.sp)
                        Text(
                            " ${AppStrings.leaderboard(language)}",
                            fontSize = 18.sp,
                            color = Color(0xFF5D4037)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onLanguage,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.height(44.dp)
                ) {
                    Text("\uD83C\uDF0D ", fontSize = 16.sp)
                    Text(
                        AppStrings.changeLanguage(language),
                        fontSize = 16.sp,
                        color = Color(0xFF555555)
                    )
                }
            }

            Text(
                text = AppStrings.tipTapAnimals(language),
                fontSize = 13.sp,
                color = Color(0xFF666666),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

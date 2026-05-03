package com.example.multiplicationzoo.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.ui.theme.PrimaryColor
import kotlin.random.Random

@Composable
fun AnimatedAnimal(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    size: Int = 40
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        label = "Animal Scale"
    )
    Box(
        modifier = Modifier
            .size(size.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                if (isSelected) PrimaryColor.copy(alpha = 0.25f) else Color.Transparent
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) PrimaryColor else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = (size * 0.75f).toInt().sp)
    }
}

@Composable
fun FloatingAnimal(
    emoji: String,
    index: Int,
    reshuffleKey: Int = 0,
    onClick: () -> Unit = {}
) {
    val config = LocalConfiguration.current
    val screenW = config.screenWidthDp
    val screenH = config.screenHeightDp

    val startX  = remember(reshuffleKey) { Random.nextFloat() * screenW }
    val startY  = remember(reshuffleKey) { Random.nextFloat() * screenH }
    val dur     = remember(reshuffleKey) { 3000 + Random.nextInt(3000) }
    val bDur    = remember(reshuffleKey) { 500  + Random.nextInt(500)  }
    val rDur    = remember(reshuffleKey) { 2000 + Random.nextInt(2000) }

    val inf = rememberInfiniteTransition(label = "animal_$index")

    val offsetX by inf.animateFloat(
        initialValue = startX,
        targetValue  = (startX + Random.nextFloat() * 80f - 40f).coerceIn(0f, screenW.toFloat()),
        animationSpec = infiniteRepeatable(tween(dur, easing = LinearEasing), RepeatMode.Reverse),
        label = "ox$index"
    )
    val offsetY by inf.animateFloat(
        initialValue = startY,
        targetValue  = (startY + Random.nextFloat() * 60f - 30f).coerceIn(0f, screenH.toFloat()),
        animationSpec = infiniteRepeatable(tween(bDur, easing = LinearEasing), RepeatMode.Reverse),
        label = "oy$index"
    )
    val rotation by inf.animateFloat(
        initialValue = -15f, targetValue = 15f,
        animationSpec = infiniteRepeatable(tween(rDur), RepeatMode.Reverse),
        label = "rot$index"
    )

    Text(
        text = emoji,
        fontSize = 72.sp,
        modifier = Modifier
            .offset(offsetX.dp, offsetY.dp)
            .rotate(rotation)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    )
}

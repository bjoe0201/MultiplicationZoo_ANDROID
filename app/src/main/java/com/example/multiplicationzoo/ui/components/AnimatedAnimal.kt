package com.example.multiplicationzoo.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedAnimal(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    size: Int = 40
) {
    val scale = animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        label = "Animal Scale"
    )

    Box(
        modifier = Modifier
            .size(size.dp)
            .scale(scale.value)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = (size * 0.8f).toInt().sp,
        )
    }
}



package com.example.multiplicationzoo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.Animal
import com.example.multiplicationzoo.ui.theme.FenceBrown
import com.example.multiplicationzoo.ui.theme.GrassGreen
import kotlin.math.ceil

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimalPen(
    penNumber: Int,
    animal: Animal,
    perPen: Int,
    tappedIndices: Set<Int>,
    penStartIndex: Int,
    onAnimalTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(3.dp, FenceBrown)
            .background(GrassGreen)
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pen number label – Compose measures its actual height naturally
        Text(
            text = (penNumber + 1).toString(),
            fontSize = 10.sp,
            color = FenceBrown,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        // Animal grid: weight(1f) lets Compose allocate the EXACT remaining height
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val availW = maxWidth
            val availH = maxHeight

            // Find itemsPerRow that maximises min(cellW, cellH) – biggest animals
            val itemsPerRow = (1..perPen).maxByOrNull { ipr ->
                val r     = ceil(perPen.toDouble() / ipr).toInt()
                val cellW = availW / ipr
                val cellH = availH / r
                minOf(cellW, cellH)
            } ?: 1

            val rows         = ceil(perPen.toDouble() / itemsPerRow).toInt()
            val cellW        = availW / itemsPerRow
            val cellH        = availH / rows
            val cellSizeDp   = minOf(cellW, cellH).coerceAtLeast(8.dp)
            val density      = LocalDensity.current.density
            val animalSizePx = with(LocalDensity.current) { (cellSizeDp - 4.dp).toPx() }
            val animalSize   = (animalSizePx / density).toInt().coerceAtLeast(6)

            FlowRow(
                modifier = Modifier.fillMaxSize(),
                maxItemsInEachRow = itemsPerRow,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                verticalArrangement   = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                repeat(perPen) { index ->
                    val globalIndex = penStartIndex + index
                    AnimatedAnimal(
                        emoji      = animal.emoji,
                        isSelected = globalIndex in tappedIndices,
                        onClick    = { onAnimalTap(globalIndex) },
                        size       = animalSize
                    )
                }
            }
        }
    }
}

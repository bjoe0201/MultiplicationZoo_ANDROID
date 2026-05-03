package com.example.multiplicationzoo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multiplicationzoo.data.Animal

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PenGrid(
    animal: Animal,
    perPen: Int,
    penCount: Int,
    tappedIndices: Set<Int>,
    onAnimalTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val maxWidth = maxWidth

        // Calculate pen size based on penCount
        val pens = calculatePenLayout(penCount)
        val penItemWidth = (maxWidth - 24.dp) / pens.cols
        val penHeight = when {
            perPen >= 9 -> 156.dp
            perPen >= 7 -> 148.dp
            else -> 124.dp
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .heightOrScroll(penCount, perPen)
                .verticalScroll(rememberScrollState()),
            maxItemsInEachRow = pens.cols,
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(penCount) { penIndex ->
                val penStartIndex = penIndex * perPen

                AnimalPen(
                    penNumber = penIndex,
                    animal = animal,
                    perPen = perPen,
                    tappedIndices = tappedIndices,
                    penStartIndex = penStartIndex,
                    onAnimalTap = onAnimalTap,
                    modifier = Modifier
                        .width(penItemWidth)
                        .height(penHeight)
                )
            }
        }
    }
}

private data class PenLayout(val cols: Int, val rows: Int)

private fun calculatePenLayout(penCount: Int): PenLayout {
    return when {
        penCount == 1 -> PenLayout(1, 1)
        penCount == 2 -> PenLayout(2, 1)
        penCount == 3 -> PenLayout(3, 1)
        penCount == 4 -> PenLayout(2, 2)
        penCount == 5 -> PenLayout(3, 2)
        penCount == 6 -> PenLayout(3, 2)
        penCount == 7 -> PenLayout(4, 2)
        penCount == 8 -> PenLayout(4, 2)
        penCount == 9 -> PenLayout(3, 3)
        else -> PenLayout(4, (penCount + 3) / 4)
    }
}

private fun Modifier.heightOrScroll(penCount: Int, perPen: Int) =
    if (penCount * perPen >= 40) {
        height(430.dp)
    } else {
        this
    }



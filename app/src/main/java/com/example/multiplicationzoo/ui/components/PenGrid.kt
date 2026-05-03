package com.example.multiplicationzoo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.multiplicationzoo.data.Animal
import kotlin.math.ceil

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
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val availableWidth  = maxWidth
        val availableHeight = maxHeight

        // Decide cols/rows based on actual available dimensions
        val (cols, rows) = bestLayout(penCount, availableWidth, availableHeight)

        // Each pen gets an equal slice of available space (minus small gaps)
        val gapDp: Dp = 4.dp
        val penWidth  = (availableWidth  - gapDp * (cols + 1)) / cols
        val penHeight = (availableHeight - gapDp * (rows + 1)) / rows

        val needsScroll = penHeight < 60.dp   // fallback if space is very tight

        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .then(if (needsScroll) Modifier.verticalScroll(rememberScrollState()) else Modifier),
            maxItemsInEachRow = cols,
            horizontalArrangement = Arrangement.spacedBy(gapDp, androidx.compose.ui.Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(gapDp)
        ) {
            repeat(penCount) { penIndex ->
                AnimalPen(
                    penNumber     = penIndex,
                    animal        = animal,
                    perPen        = perPen,
                    tappedIndices = tappedIndices,
                    penStartIndex = penIndex * perPen,
                    onAnimalTap   = onAnimalTap,
                    modifier      = Modifier
                        .width(penWidth)
                        .height(penHeight)
                )
            }
        }
    }
}

/**
 * Choose the best (cols, rows) for [penCount] pens to fill [availW] × [availH].
 * Tries all valid column counts and picks the layout whose pen aspect ratio
 * is closest to the ideal (wide enough for animals, tall enough to see them).
 */
private fun bestLayout(penCount: Int, availW: Dp, availH: Dp): Pair<Int, Int> {
    if (penCount == 1) return Pair(1, 1)

    val wPx = availW.value
    val hPx = availH.value

    var bestCols = 1
    var bestScore = Float.MAX_VALUE

    for (cols in 1..penCount) {
        val rows = ceil(penCount.toDouble() / cols).toInt()
        val penW = wPx / cols
        val penH = hPx / rows
        // Ideal: pens should be roughly square-ish; penalise very tall or very wide pens
        val ratio = if (penH > 0) penW / penH else Float.MAX_VALUE
        // We prefer ratio close to 1.2 (slightly wider than tall)
        val score = Math.abs(ratio - 1.2f)
        if (score < bestScore) {
            bestScore = score
            bestCols  = cols
        }
    }

    val bestRows = ceil(penCount.toDouble() / bestCols).toInt()
    return Pair(bestCols, bestRows)
}


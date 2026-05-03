package com.example.multiplicationzoo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.data.Animal
import com.example.multiplicationzoo.ui.theme.FenceBrown
import com.example.multiplicationzoo.ui.theme.GrassGreen

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
    val itemsPerRow = when {
        perPen <= 3 -> perPen
        perPen <= 6 -> 3
        else -> 3
    }
    val animalSize = when {
        perPen >= 9 -> 18
        perPen >= 7 -> 20
        perPen >= 5 -> 24
        else -> 28
    }

    Column(
        modifier = modifier
            .border(3.dp, FenceBrown)
            .background(GrassGreen)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Optional pen label
        Text(
            text = (penNumber + 1).toString(),
            fontSize = 10.sp,
            color = FenceBrown,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Grid of animals
        FlowRow(
            modifier = Modifier.padding(4.dp),
            maxItemsInEachRow = itemsPerRow
        ) {
            repeat(perPen) { index ->
                val globalIndex = penStartIndex + index
                val isSelected = globalIndex in tappedIndices

                AnimatedAnimal(
                    emoji = animal.emoji,
                    isSelected = isSelected,
                    onClick = { onAnimalTap(globalIndex) },
                    size = animalSize
                )
            }
        }
    }
}


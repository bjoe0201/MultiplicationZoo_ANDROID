package com.example.multiplicationzoo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplicationzoo.ui.theme.AnswerColors
import com.example.multiplicationzoo.ui.theme.CorrectGreen
import com.example.multiplicationzoo.ui.theme.WrongRed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnswerButtons(
    answers: List<Int>,
    selectedAnswer: Int?,
    onAnswerSelected: (Int) -> Unit,
    correctAnswer: Int? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        answers.forEachIndexed { idx, answer ->
            val bgColor = when {
                selectedAnswer == null -> AnswerColors[idx % AnswerColors.size]
                correctAnswer != null && answer == correctAnswer -> CorrectGreen
                answer == selectedAnswer && answer != correctAnswer -> WrongRed
                else -> Color.Gray
            }
            Button(
                onClick = { if (enabled && selectedAnswer == null) onAnswerSelected(answer) },
                modifier = Modifier
                    .height(64.dp)
                    .padding(2.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = bgColor,
                    disabledContainerColor = bgColor
                ),
                enabled = enabled && selectedAnswer == null
            ) {
                Text(
                    text = answer.toString(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

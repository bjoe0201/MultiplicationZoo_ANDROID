package com.example.multiplicationzoo.game

data class MultiplicationQuestion(
    val multiplicand: Int,
    val multiplier: Int
) {
    val product: Int get() = multiplicand * multiplier
}

fun generateQuestionPool(selectedGroups: Set<Int>): List<MultiplicationQuestion> {
    val questions = mutableListOf<MultiplicationQuestion>()

    selectedGroups.forEach { group ->
        (1..9).forEach { multiplier ->
            questions.add(MultiplicationQuestion(group, multiplier))
        }
    }

    return questions.shuffled()
}


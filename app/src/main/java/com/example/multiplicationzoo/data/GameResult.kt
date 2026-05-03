package com.example.multiplicationzoo.data

data class GameResult(
    val score: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val totalRounds: Int,
    val selectedGroups: Set<Int>,
    val timestamp: Long = System.currentTimeMillis()
)

data class LeaderboardEntry(
    val playerName: String = "",   // emoji string
    val score: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val totalRounds: Int,
    val selectedGroups: String,
    val date: String
)


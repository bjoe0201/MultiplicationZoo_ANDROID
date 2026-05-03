package com.example.multiplicationzoo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val LEADERBOARD_DATASTORE = "leaderboard"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LEADERBOARD_DATASTORE
)

class LeaderboardRepository(private val context: Context) {

    private val leaderboardKey = stringPreferencesKey("leaderboard_data")

    val leaderboard: Flow<List<LeaderboardEntry>> = context.dataStore.data.map { preferences ->
        val data = preferences[leaderboardKey] ?: ""
        parseLeaderboardData(data)
    }

    suspend fun addScore(result: GameResult, playerName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[leaderboardKey] ?: ""
            val entries = parseLeaderboardData(current).toMutableList()

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val newEntry = LeaderboardEntry(
                playerName = playerName,
                score = result.score,
                correctCount = result.correctCount,
                wrongCount = result.wrongCount,
                totalRounds = result.totalRounds,
                selectedGroups = result.selectedGroups.sorted().joinToString(","),
                date = dateFormat.format(Date(result.timestamp))
            )

            entries.add(newEntry)
            entries.sortByDescending { it.score }
            entries.take(10)

            preferences[leaderboardKey] = serializeLeaderboardData(entries)
        }
    }

    suspend fun clearLeaderboard() {
        context.dataStore.edit { preferences ->
            preferences[leaderboardKey] = ""
        }
    }

    private fun serializeLeaderboardData(entries: List<LeaderboardEntry>): String =
        entries.joinToString(";") { e ->
            "${e.playerName}~${e.score}~${e.correctCount}~${e.wrongCount}~${e.totalRounds}~${e.selectedGroups}~${e.date}"
        }

    private fun parseLeaderboardData(data: String): List<LeaderboardEntry> {
        if (data.isEmpty()) return emptyList()
        return data.split(";").mapNotNull { line ->
            val p = line.split("~")
            if (p.size == 7) {
                try {
                    LeaderboardEntry(
                        playerName = p[0],
                        score = p[1].toInt(),
                        correctCount = p[2].toInt(),
                        wrongCount = p[3].toInt(),
                        totalRounds = p[4].toInt(),
                        selectedGroups = p[5],
                        date = p[6]
                    )
                } catch (_: Exception) { null }
            } else null
        }
    }
}


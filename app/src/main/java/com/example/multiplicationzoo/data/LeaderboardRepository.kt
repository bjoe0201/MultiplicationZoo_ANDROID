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

    suspend fun addScore(result: GameResult) {
        context.dataStore.edit { preferences ->
            val current = preferences[leaderboardKey] ?: ""
            val entries = parseLeaderboardData(current).toMutableList()

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val newEntry = LeaderboardEntry(
                rank = 0,
                score = result.score,
                correctCount = result.correctCount,
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

    private fun serializeLeaderboardData(entries: List<LeaderboardEntry>): String {
        return entries.mapIndexed { index, entry ->
            "${index + 1}|${entry.score}|${entry.correctCount}|${entry.totalRounds}|${entry.selectedGroups}|${entry.date}"
        }.joinToString(";")
    }

    private fun parseLeaderboardData(data: String): List<LeaderboardEntry> {
        if (data.isEmpty()) return emptyList()

        return data.split(";").mapNotNull { line ->
            val parts = line.split("|")
            if (parts.size == 6) {
                try {
                    LeaderboardEntry(
                        rank = parts[0].toInt(),
                        score = parts[1].toInt(),
                        correctCount = parts[2].toInt(),
                        totalRounds = parts[3].toInt(),
                        selectedGroups = parts[4],
                        date = parts[5]
                    )
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }
}


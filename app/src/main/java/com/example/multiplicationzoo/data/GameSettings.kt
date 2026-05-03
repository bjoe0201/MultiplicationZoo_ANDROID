package com.example.multiplicationzoo.data

data class GameSettings(
    val rounds: Int = 5,
    val selectedGroups: Set<Int> = (2..9).toSet(),
    val voiceMode: VoiceMode = VoiceMode.NUMBER,
    val language: AppLanguage = AppLanguage.CHINESE
) {
    fun isValid(): Boolean {
        return rounds in 3..20 && selectedGroups.isNotEmpty() && selectedGroups.all { it in 2..9 }
    }
}


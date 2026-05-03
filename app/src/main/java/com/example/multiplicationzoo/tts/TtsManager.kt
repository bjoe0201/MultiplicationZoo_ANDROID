package com.example.multiplicationzoo.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.multiplicationzoo.data.Animal
import com.example.multiplicationzoo.data.AppLanguage
import java.util.Locale

class TtsManager(context: Context) {

    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isReady = true
            }
        }
    }

    private fun localeFor(language: AppLanguage): Locale = when (language) {
        AppLanguage.CHINESE -> Locale.SIMPLIFIED_CHINESE
        AppLanguage.ENGLISH -> Locale.ENGLISH
        AppLanguage.JAPANESE -> Locale.JAPAN
    }

    private fun speakText(text: String, language: AppLanguage) {
        if (!isReady) return
        tts?.language = localeFor(language)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "mz_${System.nanoTime()}")
    }

    fun speakGameCount(count: Int, language: AppLanguage) {
        speakText(count.toString(), language)
    }

    fun speakCountWithAnimal(count: Int, animal: Animal, language: AppLanguage) {
        val text = when (language) {
            AppLanguage.CHINESE -> "$count 隻${animal.nameZH}"
            AppLanguage.ENGLISH -> "$count ${if (count > 1) animal.nameEN + "s" else animal.nameEN}"
            AppLanguage.JAPANESE -> "$count 匹の${animal.nameJA}"
        }

        speakText(text, language)
    }

    fun speakFeedback(isCorrect: Boolean, language: AppLanguage) {
        val text = when {
            isCorrect && language == AppLanguage.CHINESE -> "正確"
            isCorrect && language == AppLanguage.ENGLISH -> "Correct"
            isCorrect && language == AppLanguage.JAPANESE -> "正解"
            language == AppLanguage.CHINESE -> "錯誤"
            language == AppLanguage.ENGLISH -> "Incorrect"
            else -> "不正解"
        }

        speakText(text, language)
    }

    /**
     * 先唸正確答案數字，再接著唸 正確/錯誤 反饋
     * (用 QUEUE_ADD 排隊，讓 TTS 引擎依序播放)
     */
    fun speakAnswerThenFeedback(correctAnswer: Int, isCorrect: Boolean, language: AppLanguage) {
        if (!isReady) return
        val locale = localeFor(language)

        // 1. 先唸數字（QUEUE_FLUSH 清掉舊的音頻）
        tts?.language = locale
        tts?.speak(correctAnswer.toString(), TextToSpeech.QUEUE_FLUSH, null, "mz_ans")

        // 2. 再接著唸正確/錯誤（QUEUE_ADD 排在後面）
        val feedbackText = when {
            isCorrect && language == AppLanguage.CHINESE -> "正確"
            isCorrect && language == AppLanguage.ENGLISH -> "Correct"
            isCorrect && language == AppLanguage.JAPANESE -> "正解"
            language == AppLanguage.CHINESE -> "錯誤"
            language == AppLanguage.ENGLISH -> "Incorrect"
            else -> "不正解"
        }
        tts?.speak(feedbackText, TextToSpeech.QUEUE_ADD, null, "mz_fb")
    }

    fun speakAnimalName(animal: Animal, language: AppLanguage) {
        val name = when (language) {
            AppLanguage.CHINESE -> animal.nameZH
            AppLanguage.ENGLISH -> animal.nameEN
            AppLanguage.JAPANESE -> animal.nameJA
        }

        speakText(name, language)
    }

    fun speakMultiplication(multiplicand: Int, multiplier: Int, language: AppLanguage) {
        val text = when (language) {
            AppLanguage.CHINESE -> "$multiplicand 乘以 $multiplier"
            AppLanguage.ENGLISH -> "$multiplicand times $multiplier"
            AppLanguage.JAPANESE -> "$multiplicand かける $multiplier"
        }

        speakText(text, language)
    }

    fun speakPenComplete(penNumber: Int, count: Int, animal: Animal, language: AppLanguage) {
        val text = when (language) {
            AppLanguage.CHINESE -> "第 $penNumber 欄，$count 隻${animal.nameZH}"
            AppLanguage.ENGLISH -> "Pen $penNumber, $count ${if (count > 1) animal.nameEN + "s" else animal.nameEN}"
            AppLanguage.JAPANESE -> "$penNumber ばんめ、$count 匹の${animal.nameJA}"
        }

        speakText(text, language)
    }

    fun shutdown() {
        tts?.shutdown()
    }
}


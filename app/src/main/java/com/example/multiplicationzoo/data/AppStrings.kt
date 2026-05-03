package com.example.multiplicationzoo.data

object AppStrings {
    
    fun appTitle(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "九九乘法動物園"
        AppLanguage.ENGLISH -> "Multiplication Zoo"
        AppLanguage.JAPANESE -> "かけ算どうぶつえん"
    }
    
    fun home(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "首頁"
        AppLanguage.ENGLISH -> "Home"
        AppLanguage.JAPANESE -> "ホーム"
    }
    
    fun startGame(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "開始遊戲"
        AppLanguage.ENGLISH -> "Start Game"
        AppLanguage.JAPANESE -> "ゲーム開始"
    }
    
    fun settings(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "設定"
        AppLanguage.ENGLISH -> "Settings"
        AppLanguage.JAPANESE -> "設定"
    }
    
    fun leaderboard(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "排行榜"
        AppLanguage.ENGLISH -> "Leaderboard"
        AppLanguage.JAPANESE -> "ランキング"
    }
    
    fun language(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "語言"
        AppLanguage.ENGLISH -> "Language"
        AppLanguage.JAPANESE -> "言語"
    }
    
    fun rounds(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "遊戲題數"
        AppLanguage.ENGLISH -> "Rounds"
        AppLanguage.JAPANESE -> "ラウンド数"
    }
    
    fun voiceMode(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "語音模式"
        AppLanguage.ENGLISH -> "Voice Mode"
        AppLanguage.JAPANESE -> "音声モード"
    }
    
    fun voiceModeNone(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "靜音"
        AppLanguage.ENGLISH -> "Silent"
        AppLanguage.JAPANESE -> "サイレント"
    }
    
    fun voiceModeNumber(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "數字計數"
        AppLanguage.ENGLISH -> "Number Count"
        AppLanguage.JAPANESE -> "数字カウント"
    }
    
    fun voiceModeNumberWithAnimal(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "數字+動物名"
        AppLanguage.ENGLISH -> "Number + Animal"
        AppLanguage.JAPANESE -> "数字+動物名"
    }
    
    fun voiceModePenCount(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "柵欄計數"
        AppLanguage.ENGLISH -> "Pen Count"
        AppLanguage.JAPANESE -> "囲いカウント"
    }
    
    fun multiplicationGroups(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "乘法組別"
        AppLanguage.ENGLISH -> "Multiplication Groups"
        AppLanguage.JAPANESE -> "かけ算のグループ"
    }
    
    fun selectAll(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "全選"
        AppLanguage.ENGLISH -> "Select All"
        AppLanguage.JAPANESE -> "すべて選ぶ"
    }
    
    fun deselectAll(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "取消全選"
        AppLanguage.ENGLISH -> "Deselect All"
        AppLanguage.JAPANESE -> "すべて外す"
    }
    
    fun selectedGroupsLabel(language: AppLanguage, count: Int): String = when (language) {
        AppLanguage.CHINESE -> "已選 $count 組"
        AppLanguage.ENGLISH -> "$count groups selected"
        AppLanguage.JAPANESE -> "$count グループ選択中"
    }
    
    fun penHint(language: AppLanguage, per: Int, count: Int): String = when (language) {
        AppLanguage.CHINESE -> "每個柵欄 " + per + " 隻，共 " + count + " 個柵欄"
        AppLanguage.ENGLISH -> per.toString() + " per pen, " + count + " pens"
        AppLanguage.JAPANESE -> "1つの囲いに" + per + "匹、" + count + "つの囲い"
    }
    
    fun tapToCount(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "點動物來數數！"
        AppLanguage.ENGLISH -> "Tap to count!"
        AppLanguage.JAPANESE -> "タップしてかぞえよう！"
    }
    
    fun groupLabel(language: AppLanguage, n: Int): String = when (language) {
        AppLanguage.CHINESE -> "$n 的乘法"
        AppLanguage.ENGLISH -> "Group $n"
        AppLanguage.JAPANESE -> "$n のだん"
    }
    
    fun atLeastOneGroup(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "至少選擇一組"
        AppLanguage.ENGLISH -> "At least one group"
        AppLanguage.JAPANESE -> "少なくとも1つ選んでください"
    }
    
    fun question(language: AppLanguage, current: Int, total: Int): String = when (language) {
        AppLanguage.CHINESE -> "第 $current/$total 題"
        AppLanguage.ENGLISH -> "Question $current/$total"
        AppLanguage.JAPANESE -> "問題 $current/$total"
    }
    
    fun quit(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "退出"
        AppLanguage.ENGLISH -> "Quit"
        AppLanguage.JAPANESE -> "終了"
    }
    
    fun correct(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "正確！"
        AppLanguage.ENGLISH -> "Correct!"
        AppLanguage.JAPANESE -> "正解！"
    }
    
    fun incorrect(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "錯誤"
        AppLanguage.ENGLISH -> "Incorrect"
        AppLanguage.JAPANESE -> "不正解"
    }
    
    fun score(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "得分"
        AppLanguage.ENGLISH -> "Score"
        AppLanguage.JAPANESE -> "スコア"
    }
    
    fun result(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "成績"
        AppLanguage.ENGLISH -> "Result"
        AppLanguage.JAPANESE -> "成績"
    }
    
    fun next(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "下一題"
        AppLanguage.ENGLISH -> "Next"
        AppLanguage.JAPANESE -> "次へ"
    }
    
    fun gameOver(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "遊戲結束"
        AppLanguage.ENGLISH -> "Game Over"
        AppLanguage.JAPANESE -> "ゲーム終了"
    }
    
    fun clearLeaderboard(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "清除排行榜"
        AppLanguage.ENGLISH -> "Clear Leaderboard"
        AppLanguage.JAPANESE -> "ランキングをクリア"
    }
    
    fun confirm(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "確認"
        AppLanguage.ENGLISH -> "Confirm"
        AppLanguage.JAPANESE -> "確認"
    }
    
    fun cancel(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "取消"
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.JAPANESE -> "キャンセル"
    }

    fun tappedCount(language: AppLanguage, count: Int): String = when (language) {
        AppLanguage.CHINESE -> "已點選: $count"
        AppLanguage.ENGLISH -> "Tapped: $count"
        AppLanguage.JAPANESE -> "タップ数: $count"
    }

    fun correctCount(language: AppLanguage, count: Int): String = when (language) {
        AppLanguage.CHINESE -> "答對: $count"
        AppLanguage.ENGLISH -> "Correct: $count"
        AppLanguage.JAPANESE -> "正解: $count"
    }

    fun wrongCount(language: AppLanguage, count: Int): String = when (language) {
        AppLanguage.CHINESE -> "答錯: $count"
        AppLanguage.ENGLISH -> "Wrong: $count"
        AppLanguage.JAPANESE -> "不正解: $count"
    }

    fun totalCount(language: AppLanguage, count: Int): String = when (language) {
        AppLanguage.CHINESE -> "總題數: $count"
        AppLanguage.ENGLISH -> "Total: $count"
        AppLanguage.JAPANESE -> "合計: $count"
    }

    fun playAgain(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "再玩一次"
        AppLanguage.ENGLISH -> "Play Again"
        AppLanguage.JAPANESE -> "もう一度"
    }

    fun back(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "返回"
        AppLanguage.ENGLISH -> "Back"
        AppLanguage.JAPANESE -> "戻る"
    }

    fun noScoresYet(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE -> "目前還沒有分數"
        AppLanguage.ENGLISH -> "No scores yet"
        AppLanguage.JAPANESE -> "まだスコアがありません"
    }
}

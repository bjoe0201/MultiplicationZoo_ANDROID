package com.example.multiplicationzoo.data
object AppStrings {
    fun appTitle(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u4e5d\u4e5d\u4e58\u6cd5\u52d5\u7269\u5712"
        AppLanguage.ENGLISH  -> "Multiplication Zoo"
        AppLanguage.JAPANESE -> "\u4e5d\u4e5d\u306e\u52d5\u7269\u5712"
    }
    fun start(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u958b\u59cb\uff01"
        AppLanguage.ENGLISH  -> "Start!"
        AppLanguage.JAPANESE -> "\u30b9\u30bf\u30fc\u30c8\uff01"
    }
    fun home(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u9996\u9801"
        AppLanguage.ENGLISH  -> "Home"
        AppLanguage.JAPANESE -> "\u30db\u30fc\u30e0"
    }
    fun startGame(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u958b\u59cb\u9047\u6232"
        AppLanguage.ENGLISH  -> "Start Game"
        AppLanguage.JAPANESE -> "\u30b2\u30fc\u30e0\u30b9\u30bf\u30fc\u30c8"
    }
    fun settings(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u8a2d\u5b9a"
        AppLanguage.ENGLISH  -> "Settings"
        AppLanguage.JAPANESE -> "\u8a2d\u5b9a"
    }
    fun leaderboard(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u6392\u884c\u699c"
        AppLanguage.ENGLISH  -> "Leaderboard"
        AppLanguage.JAPANESE -> "\u30e9\u30f3\u30ad\u30f3\u30b0"
    }
    fun language(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u8a9e\u8a00"
        AppLanguage.ENGLISH  -> "Language"
        AppLanguage.JAPANESE -> "\u8a00\u8a9e"
    }
    fun changeLanguage(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u63db\u8a9e\u8a00"
        AppLanguage.ENGLISH  -> "Change Language"
        AppLanguage.JAPANESE -> "\u8a00\u8a9e\u5909\u66f4"
    }
    fun rounds(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u56de\u5408"
        AppLanguage.ENGLISH  -> "Rounds"
        AppLanguage.JAPANESE -> "\u30e9\u30a6\u30f3\u30c9"
    }
    fun voiceMode(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u8a9e\u97f3\u6a21\u5f0f"
        AppLanguage.ENGLISH  -> "Voice Mode"
        AppLanguage.JAPANESE -> "\u97f3\u58f0\u30e2\u30fc\u30c9"
    }
    fun voiceModeNone(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u7121\u97f3"
        AppLanguage.ENGLISH  -> "None"
        AppLanguage.JAPANESE -> "\u306a\u3057"
    }
    fun voiceModeNumber(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u6578\u5b57"
        AppLanguage.ENGLISH  -> "Number"
        AppLanguage.JAPANESE -> "\u6570\u5b57"
    }
    fun voiceModeNumberWithAnimal(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u6578\u5b57+\u52d5\u7269"
        AppLanguage.ENGLISH  -> "Number+Animal"
        AppLanguage.JAPANESE -> "\u6570\u5b57+\u52d5\u7269"
    }
    fun voiceModePenCount(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u570d\u6b04\u8a08\u6578"
        AppLanguage.ENGLISH  -> "Pen Count"
        AppLanguage.JAPANESE -> "\u56f2\u3044\u8a08\u6570"
    }
    fun multiplicationGroups(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u4e58\u6cd5\u7d44\u5225"
        AppLanguage.ENGLISH  -> "Multiplication Groups"
        AppLanguage.JAPANESE -> "\u639b\u3051\u7b97\u30b0\u30eb\u30fc\u30d7"
    }
    fun selectAll(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u5168\u9078"
        AppLanguage.ENGLISH  -> "Select All"
        AppLanguage.JAPANESE -> "\u5168\u9078\u629e"
    }
    fun deselectAll(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u53d6\u6d88\u5168\u9078"
        AppLanguage.ENGLISH  -> "Deselect All"
        AppLanguage.JAPANESE -> "\u9078\u629e\u89e3\u9664"
    }
    fun selectedGroupsLabel(language: AppLanguage, count: Int): String = when (language) {
        AppLanguage.CHINESE  -> "\u5df2\u9078 $count \u7d44"
        AppLanguage.ENGLISH  -> "$count groups selected"
        AppLanguage.JAPANESE -> "$count \u30b0\u30eb\u30fc\u30d7\u9078\u629e\u4e2d"
    }
    fun penHint(language: AppLanguage, per: Int, count: Int): String = when (language) {
        AppLanguage.CHINESE  -> "\u6bcf\u6b04 $per \u96bb\u52d5\u7269\uff0c\u5171 $count \u6b04"
        AppLanguage.ENGLISH  -> "$per per pen x $count pens"
        AppLanguage.JAPANESE -> "1\u56f2\u3044\u306b${per}\u982d x ${count}\u56f2\u3044"
    }
    fun tapToCount(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u9ede\u64ca\u8a08\u6578\uff01"
        AppLanguage.ENGLISH  -> "Tap to count!"
        AppLanguage.JAPANESE -> "\u30bf\u30c3\u30d7\u3057\u3066\u6570\u3048\u3088\u3046\uff01"
    }
    fun groupLabel(language: AppLanguage, n: Int): String = when (language) {
        AppLanguage.CHINESE  -> "$n \u500d"
        AppLanguage.ENGLISH  -> "Group $n"
        AppLanguage.JAPANESE -> "$n \u306e\u6bb5"
    }
    fun atLeastOneGroup(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u81f3\u5c11\u8981\u9078\u4e00\u7d44"
        AppLanguage.ENGLISH  -> "At least one group"
        AppLanguage.JAPANESE -> "\u5c11\u306a\u304f\u3068\u30821\u3064\u9078\u3093\u3067\u304f\u3060\u3055\u3044"
    }
    fun question(language: AppLanguage, current: Int, total: Int): String = when (language) {
        AppLanguage.CHINESE  -> "\u7b2c $current/$total \u984c"
        AppLanguage.ENGLISH  -> "Round $current/$total"
        AppLanguage.JAPANESE -> "\u7b2c $current/$total \u554f"
    }
    fun quit(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u96e2\u958b"
        AppLanguage.ENGLISH  -> "Quit"
        AppLanguage.JAPANESE -> "\u3084\u3081\u308b"
    }
    fun quitGameTitle(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u96e2\u958b\u9047\u6232"
        AppLanguage.ENGLISH  -> "Quit Game"
        AppLanguage.JAPANESE -> "\u30b2\u30fc\u30e0\u3092\u3084\u3081\u308b"
    }
    fun quitGameMessage(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u78ba\u5b9a\u8981\u96e2\u958b\uff1f\n\u76ee\u524d\u9032\u5ea6\u4e0d\u6703\u88ab\u5132\u5b58\u3002"
        AppLanguage.ENGLISH  -> "Quit?\nCurrent progress will not be saved."
        AppLanguage.JAPANESE -> "\u3084\u3081\u307e\u3059\u304b\uff1f\n\u9032\u884c\u72b6\u6cc1\u306f\u4fdd\u5b58\u3055\u308c\u307e\u305b\u3093\u3002"
    }
    fun correct(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u6b63\u78ba\uff01"
        AppLanguage.ENGLISH  -> "Correct!"
        AppLanguage.JAPANESE -> "\u6b63\u89e3\uff01"
    }
    fun wrong(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u932f\u8aa4\uff01"
        AppLanguage.ENGLISH  -> "Wrong!"
        AppLanguage.JAPANESE -> "\u4e0d\u6b63\u89e3\uff01"
    }
    fun score(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u5206\u6578"
        AppLanguage.ENGLISH  -> "Score"
        AppLanguage.JAPANESE -> "\u30b9\u30b3\u30a2"
    }
    fun result(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u7d50\u679c"
        AppLanguage.ENGLISH  -> "Result"
        AppLanguage.JAPANESE -> "\u7d50\u679c"
    }
    fun gameOver(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u9047\u6232\u7d50\u675f"
        AppLanguage.ENGLISH  -> "Game Over"
        AppLanguage.JAPANESE -> "\u30b2\u30fc\u30e0\u30aa\u30fc\u30d0\u30fc"
    }
    fun correctLabel(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u6b63\u78ba"
        AppLanguage.ENGLISH  -> "Correct"
        AppLanguage.JAPANESE -> "\u6b63\u89e3"
    }
    fun wrongLabel(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u932f\u8aa4"
        AppLanguage.ENGLISH  -> "Wrong"
        AppLanguage.JAPANESE -> "\u4e0d\u6b63\u89e3"
    }
    fun yourName(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u4f60\u7684\u540d\u5b57"
        AppLanguage.ENGLISH  -> "Your Name"
        AppLanguage.JAPANESE -> "\u304a\u540d\u524d\u306f\uff1f"
    }
    fun saveScore(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u5132\u5b58\u5206\u6578"
        AppLanguage.ENGLISH  -> "Save Score"
        AppLanguage.JAPANESE -> "\u30b9\u30b3\u30a2\u3092\u4fdd\u5b58"
    }
    fun playAgain(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u518d\u73a9\u4e00\u6b21"
        AppLanguage.ENGLISH  -> "Play Again"
        AppLanguage.JAPANESE -> "\u3082\u3046\u4e00\u5ea6"
    }
    fun back(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u8fd4\u56de"
        AppLanguage.ENGLISH  -> "Back"
        AppLanguage.JAPANESE -> "\u623b\u308b"
    }
    fun noScoresYet(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u5c1a\u7121\u7d00\u9304"
        AppLanguage.ENGLISH  -> "No scores yet"
        AppLanguage.JAPANESE -> "\u307e\u3060\u30b9\u30b3\u30a2\u306a\u3057"
    }
    fun clearLeaderboard(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u6e05\u9664\u6392\u884c\u699c"
        AppLanguage.ENGLISH  -> "Clear Leaderboard"
        AppLanguage.JAPANESE -> "\u30e9\u30f3\u30ad\u30f3\u30b0\u3092\u30af\u30ea\u30a2"
    }
    fun clearConfirm1(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u78ba\u5b9a\u8981\u6e05\u9664\u6240\u6709\u7d00\u9304\u55ce\uff1f"
        AppLanguage.ENGLISH  -> "Clear all records?"
        AppLanguage.JAPANESE -> "\u5168\u8a18\u9332\u3092\u6d88\u3057\u307e\u3059\u304b\uff1f"
    }
    fun clearConfirm2(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u771f\u7684\u78ba\u5b9a\uff1f\u7121\u6cd5\u9084\u539f\uff01"
        AppLanguage.ENGLISH  -> "Really sure? Cannot be undone!"
        AppLanguage.JAPANESE -> "\u672c\u5f53\u306b\u3088\u308d\u3057\u3044\u3067\u3059\u304b\uff1f"
    }
    fun clearConfirmBtn(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u78ba\u5b9a\u6e05\u9664"
        AppLanguage.ENGLISH  -> "Yes, Clear"
        AppLanguage.JAPANESE -> "\u306f\u3044\u3001\u30af\u30ea\u30a2"
    }
    fun cancel(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u53d6\u6d88"
        AppLanguage.ENGLISH  -> "Cancel"
        AppLanguage.JAPANESE -> "\u30ad\u30e3\u30f3\u30bb\u30eb"
    }
    fun clearDone(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u6e05\u9664\u5b8c\u6210\uff01"
        AppLanguage.ENGLISH  -> "Cleared!"
        AppLanguage.JAPANESE -> "\u30af\u30ea\u30a2\u5b8c\u4e86\uff01"
    }
    fun shuffle(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\u91cd\u65b0\u6392\u5217"
        AppLanguage.ENGLISH  -> "Shuffle"
        AppLanguage.JAPANESE -> "\u30b7\u30e3\u30c3\u30d5\u30eb"
    }
    fun tipTapAnimals(language: AppLanguage): String = when (language) {
        AppLanguage.CHINESE  -> "\uD83D\uDC46 \u9ede\u64ca\u52d5\u7269\u807d\u767c\u97f3"
        AppLanguage.ENGLISH  -> "\uD83D\uDC46 Tap animals to hear pronunciation"
        AppLanguage.JAPANESE -> "\uD83D\uDC46 \u52d5\u7269\u3092\u30bf\u30c3\u30d7\u3057\u3066\u767a\u97f3\u3092\u8074\u3053\u3046"
    }
    fun tappedCount(language: AppLanguage, count: Int): String = when (language) {
        AppLanguage.CHINESE  -> "\u5df2\u9ede\u64ca: $count"
        AppLanguage.ENGLISH  -> "Tapped: $count"
        AppLanguage.JAPANESE -> "\u30bf\u30c3\u30d7\u6570: $count"
    }
    fun correctCount(language: AppLanguage, count: Int): String = when (language) {
        AppLanguage.CHINESE  -> "\u6b63\u78ba: $count"
        AppLanguage.ENGLISH  -> "Correct: $count"
        AppLanguage.JAPANESE -> "\u6b63\u89e3: $count"
    }
    fun wrongCount(language: AppLanguage, count: Int): String = when (language) {
        AppLanguage.CHINESE  -> "\u932f\u8aa4: $count"
        AppLanguage.ENGLISH  -> "Wrong: $count"
        AppLanguage.JAPANESE -> "\u4e0d\u6b63\u89e3: $count"
    }
    fun totalCount(language: AppLanguage, count: Int): String = when (language) {
        AppLanguage.CHINESE  -> "\u7e3d\u984c\u6578: $count"
        AppLanguage.ENGLISH  -> "Total: $count"
        AppLanguage.JAPANESE -> "\u5408\u8a08: $count"
    }
}
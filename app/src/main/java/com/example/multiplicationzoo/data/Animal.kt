package com.example.multiplicationzoo.data

import com.example.multiplicationzoo.data.AppLanguage

data class Animal(
    val id: Int,
    val name: String,
    val emoji: String,
    val nameZH: String,
    val nameEN: String,
    val nameJA: String
)

fun getAnimalList(): List<Animal> = listOf(
    Animal(1, "dog", "🐶", "狗", "Dog", "犬"),
    Animal(2, "cat", "🐱", "貓", "Cat", "猫"),
    Animal(3, "rabbit", "🐰", "兔子", "Rabbit", "ウサギ"),
    Animal(4, "fox", "🦊", "狐狸", "Fox", "キツネ"),
    Animal(5, "panda", "🐼", "熊貓", "Panda", "パンダ"),
    Animal(6, "koala", "🐨", "無尾熊", "Koala", "コアラ"),
    Animal(7, "tiger", "🐯", "老虎", "Tiger", "トラ"),
    Animal(8, "lion", "🦁", "獅子", "Lion", "ライオン"),
    Animal(9, "cow", "🐄", "牛", "Cow", "牛"),
    Animal(10, "pig", "🐷", "豬", "Pig", "豚"),
    Animal(11, "sheep", "🐑", "羊", "Sheep", "羊"),
    Animal(12, "chicken", "🐔", "雞", "Chicken", "鶏"),
    Animal(13, "penguin", "🐧", "企鵝", "Penguin", "ペンギン"),
    Animal(14, "duck", "🦆", "鴨子", "Duck", "アヒル"),
    Animal(15, "frog", "🐸", "青蛙", "Frog", "カエル"),
    Animal(16, "elephant", "🐘", "象", "Elephant", "象"),
    Animal(17, "giraffe", "🦒", "長頸鹿", "Giraffe", "キリン"),
    Animal(18, "zebra", "🦓", "斑馬", "Zebra", "シマウマ"),
    Animal(19, "monkey", "🐵", "猴子", "Monkey", "猿"),
    Animal(20, "bear", "🐻", "熊", "Bear", "熊")
)

fun Animal.displayName(language: AppLanguage): String = when (language) {
    AppLanguage.CHINESE -> nameZH
    AppLanguage.ENGLISH -> nameEN
    AppLanguage.JAPANESE -> nameJA
}


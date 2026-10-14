package com.example.data.model

data class Surah(
    val number: Int,
    val arabicName: String,
    val transliteration: String,
    val versesCount: Int,
    val revelationType: RevelationType,
    val startPage: Int
)

enum class RevelationType(val arabicLabel: String) {
    MECCAN("مكية"),
    MEDINAN("مدنية")
}

fun Surah.toUiModel(): SurahUiModel {
    return SurahUiModel(
        number = number,
        nameArabic = arabicName,
        ayahCount = versesCount,
        revelationType = revelationType.arabicLabel,
        startPage = startPage
    )
}

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val textUthmani: String,
    val pageNumber: Int,
    val juzNumber: Int,
    val isSajda: Boolean = false
)

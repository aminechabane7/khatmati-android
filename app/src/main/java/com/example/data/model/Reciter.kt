package com.example.data.model

data class Reciter(
    val id: String,
    val arabicName: String,
    val style: String = "حفص عن عاصم - مرتل",
    val isSelected: Boolean = false
)

data class ReadingPosition(
    val surahNumber: Int = 1,
    val surahName: String = "سورة الفاتحة",
    val ayahNumber: Int = 1,
    val pageNumber: Int = 1,
    val lastReadFormatted: String = "اليوم"
)

data class Bookmark(
    val id: String,
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val pageNumber: Int,
    val dateFormatted: String
)

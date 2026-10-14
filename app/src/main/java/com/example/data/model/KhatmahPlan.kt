package com.example.data.model

data class KhatmahPlan(
    val id: String = "default_plan",
    val title: String = "ختمة ٣٠ يوماً",
    val totalDays: Int = 30,
    val currentDay: Int = 8,
    val totalPages: Int = 604,
    val completedPages: Int = 156,
    val currentPage: Int = 150, // currently reading at page 150
    val todayStartPage: Int = 142,
    val todayEndPage: Int = 161,
    val todayTargetPages: Int = 20,
    val todayCompletedPages: Int = 8,
    val isPrayerDivisionEnabled: Boolean = true,
    val prayerProgress: List<PrayerWirdProgress> = defaultPrayerWirds()
) {
    val progressPercent: Float
        get() = if (totalPages > 0) (completedPages.toFloat() / totalPages.toFloat()) * 100f else 0f

    val todayProgressPercent: Float
        get() = if (todayTargetPages > 0) (todayCompletedPages.toFloat() / todayTargetPages.toFloat()) * 100f else 0f

    val remainingPages: Int
        get() = (totalPages - completedPages).coerceAtLeast(0)

    val remainingDays: Int
        get() = (totalDays - currentDay).coerceAtLeast(1)

    // Mathematically recalculate daily target if missed
    val calculatedRemainingDailyTarget: Int
        get() = (remainingPages + remainingDays - 1) / remainingDays
}

data class PrayerWirdProgress(
    val prayerName: String,
    val pagesCount: Int,
    val startPage: Int,
    val endPage: Int,
    val isCompleted: Boolean
)

fun defaultPrayerWirds(): List<PrayerWirdProgress> = listOf(
    PrayerWirdProgress(prayerName = "الفجر", pagesCount = 4, startPage = 142, endPage = 145, isCompleted = true),
    PrayerWirdProgress(prayerName = "الظهر", pagesCount = 4, startPage = 146, endPage = 149, isCompleted = true),
    PrayerWirdProgress(prayerName = "العصر", pagesCount = 4, startPage = 150, endPage = 153, isCompleted = false),
    PrayerWirdProgress(prayerName = "المغرب", pagesCount = 4, startPage = 154, endPage = 157, isCompleted = false),
    PrayerWirdProgress(prayerName = "العشاء", pagesCount = 4, startPage = 158, endPage = 161, isCompleted = false)
)

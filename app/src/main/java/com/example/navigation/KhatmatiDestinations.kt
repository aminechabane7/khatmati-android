package com.example.navigation

object KhatmatiDestinations {
    const val ONBOARDING = "onboarding"
    const val MAIN = "main"
    const val QURAN_READER = "quranReader/{surahNumber}"
    const val CREATE_KHATMAH = "create_khatmah"
    const val DHIKR_READER = "dhikr_reader/{categoryId}"
    const val PRAYER_TIMES = "prayer_times"
    const val RECITERS = "reciters"

    fun quranReaderRoute(surahNumber: Int = 1): String {
        return "quranReader/$surahNumber"
    }

    fun dhikrReaderRoute(categoryId: String): String {
        return "dhikr_reader/$categoryId"
    }
}

enum class MainBottomTab(
    val route: String,
    val titleArabic: String
) {
    HOME("home", "الرئيسية"),
    QURAN("quran", "القرآن"),
    KHATMATI("khatmati", "ختمتي"),
    ADHKAR("adhkar", "الأذكار"),
    MORE("more", "المزيد")
}

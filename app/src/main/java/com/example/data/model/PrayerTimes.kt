package com.example.data.model

data class PrayerTimeItem(
    val id: String,
    val arabicName: String,
    val timeFormatted: String, // e.g. "٠٤:٥٢ ص"
    val isNext: Boolean = false,
    val isCurrent: Boolean = false,
    val isPast: Boolean = false
)

data class PrayerSchedule(
    val cityName: String = "مكة المكرمة",
    val locationName: String = "مكة المكرمة (موقع افتراضي)",
    val isMockLocation: Boolean = true,
    val currentTimeFormatted: String = "",
    val hijriDate: String = "",
    val gregorianDate: String = "",
    val nextPrayerName: String = "العصر",
    val nextPrayerCountdown: String = "٠٠:٠٠:٠٠",
    val calculationMethod: String = "أم القرى - مكة المكرمة (افتراضي)",
    val prayers: List<PrayerTimeItem> = emptyList()
)

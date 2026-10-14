package com.example.data.repository

import com.example.core.util.DateTimeHelper
import com.example.core.util.DefaultTimeProvider
import com.example.core.util.TimeProvider
import com.example.data.model.PrayerSchedule
import com.example.data.model.PrayerTimeItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime

class PrayerRepository(
    private val timeProvider: TimeProvider = DefaultTimeProvider()
) {

    // Isolated Mock/Default Location source for Phase 1
    val availableCities = listOf(
        "مكة المكرمة",
        "المدينة المنورة",
        "الرياض",
        "جدة",
        "القاهرة",
        "دبي",
        "أبوظبي",
        "الدوحة",
        "الكويت",
        "عمان"
    )

    val calculationMethods = listOf(
        "أم القرى - مكة المكرمة",
        "رابطة العالم الإسلامي",
        "الهيئة العامة المصرية للمساحة",
        "جامعة العلوم الإسلامية بكراتشي",
        "الاتحاد الإسلامي بأمريكا الشمالية (ISNA)"
    )

    private val _selectedCity = MutableStateFlow("مكة المكرمة")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    private val _calculationMethod = MutableStateFlow("أم القرى - مكة المكرمة")

    // Mock Prayer Times (LocalTime) for Phase 1
    private val fajrTime = LocalTime.of(4, 52)
    private val shurooqTime = LocalTime.of(6, 11)
    private val dhuhrTime = LocalTime.of(12, 18)
    private val asrTime = LocalTime.of(15, 41)
    private val maghribTime = LocalTime.of(18, 5)
    private val ishaTime = LocalTime.of(19, 35)

    /**
     * Ticker Flow emitting real-time dynamically computed prayer schedule every second.
     * Never requires app restart.
     */
    val scheduleFlow: Flow<PrayerSchedule> = flow {
        while (true) {
            emit(computeCurrentSchedule())
            delay(1000)
        }
    }

    fun updateCity(city: String) {
        _selectedCity.value = city
    }

    fun updateCalculationMethod(method: String) {
        _calculationMethod.value = method
    }

    fun computeCurrentSchedule(): PrayerSchedule {
        val nowZoned = timeProvider.nowZoned()
        val nowDateTime = nowZoned.toLocalDateTime()
        val today = nowZoned.toLocalDate()
        val nowTime = nowZoned.toLocalTime()

        // Determine current and next prayer based on actual device time
        data class PrayerSpec(val id: String, val name: String, val time: LocalTime, val isActualPrayer: Boolean = true)

        val prayerSpecs = listOf(
            PrayerSpec("fajr", "الفجر", fajrTime),
            PrayerSpec("shurooq", "الشروق", shurooqTime, isActualPrayer = false),
            PrayerSpec("dhuhr", "الظهر", dhuhrTime),
            PrayerSpec("asr", "العصر", asrTime),
            PrayerSpec("maghrib", "المغرب", maghribTime),
            PrayerSpec("isha", "العشاء", ishaTime)
        )

        val nextPrayerSpec: PrayerSpec
        val targetDateTime: LocalDateTime

        if (nowTime.isBefore(fajrTime)) {
            nextPrayerSpec = prayerSpecs[0] // Fajr
            targetDateTime = today.atTime(fajrTime)
        } else if (nowTime.isBefore(shurooqTime)) {
            nextPrayerSpec = prayerSpecs[1] // Shurooq
            targetDateTime = today.atTime(shurooqTime)
        } else if (nowTime.isBefore(dhuhrTime)) {
            nextPrayerSpec = prayerSpecs[2] // Dhuhr
            targetDateTime = today.atTime(dhuhrTime)
        } else if (nowTime.isBefore(asrTime)) {
            nextPrayerSpec = prayerSpecs[3] // Asr
            targetDateTime = today.atTime(asrTime)
        } else if (nowTime.isBefore(maghribTime)) {
            nextPrayerSpec = prayerSpecs[4] // Maghrib
            targetDateTime = today.atTime(maghribTime)
        } else if (nowTime.isBefore(ishaTime)) {
            nextPrayerSpec = prayerSpecs[5] // Isha
            targetDateTime = today.atTime(ishaTime)
        } else {
            // After Isha: Transition to tomorrow's Fajr smoothly
            nextPrayerSpec = prayerSpecs[0] // Fajr
            targetDateTime = today.plusDays(1).atTime(fajrTime)
        }

        val secondsUntilNext = Duration.between(nowDateTime, targetDateTime).seconds.coerceAtLeast(0)
        val countdownFormatted = DateTimeHelper.formatCountdown(secondsUntilNext)

        val prayerItems = prayerSpecs.map { spec ->
            val isNext = spec.id == nextPrayerSpec.id
            val isPast = if (nowTime.isAfter(ishaTime)) {
                true
            } else {
                nowTime.isAfter(spec.time)
            }
            val isCurrent = when (nextPrayerSpec.id) {
                "fajr" -> if (nowTime.isAfter(ishaTime)) spec.id == "isha" else false
                "shurooq" -> spec.id == "fajr"
                "dhuhr" -> spec.id == "shurooq"
                "asr" -> spec.id == "dhuhr"
                "maghrib" -> spec.id == "asr"
                "isha" -> spec.id == "maghrib"
                else -> false
            }

            PrayerTimeItem(
                id = spec.id,
                arabicName = spec.name,
                timeFormatted = DateTimeHelper.formatTimeArabic(spec.time),
                isNext = isNext,
                isCurrent = isCurrent,
                isPast = isPast
            )
        }

        val currentCity = _selectedCity.value
        val locationDisplay = "$currentCity (موقع افتراضي)"
        val currentTimeFormatted = DateTimeHelper.formatCurrentTime(nowTime)

        return PrayerSchedule(
            cityName = currentCity,
            locationName = locationDisplay,
            isMockLocation = true,
            currentTimeFormatted = currentTimeFormatted,
            hijriDate = DateTimeHelper.formatCurrentHijriDate(today),
            gregorianDate = DateTimeHelper.formatCurrentGregorianDate(today),
            nextPrayerName = nextPrayerSpec.name,
            nextPrayerCountdown = countdownFormatted,
            calculationMethod = "${_calculationMethod.value} (افتراضي)",
            prayers = prayerItems
        )
    }
}

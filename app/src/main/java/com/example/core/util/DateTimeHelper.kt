package com.example.core.util

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.util.Locale

object DateTimeHelper {

    private val ARABIC_DIGITS = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')

    private val GREGORIAN_MONTHS_AR = listOf(
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
    )

    private val HIJRI_MONTHS_AR = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    fun toArabicDigits(input: String): String {
        val sb = java.lang.StringBuilder()
        for (ch in input) {
            if (ch in '0'..'9') {
                sb.append(ARABIC_DIGITS[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun toArabicDigits(number: Int): String {
        return toArabicDigits(number.toString())
    }

    var timeProvider: TimeProvider = DefaultTimeProvider()

    fun getDeviceZoneId(): ZoneId {
        return timeProvider.zoneId
    }

    /**
     * Formats the device's actual Gregorian date into Arabic.
     * e.g., "٧ أكتوبر ٢٠٢٦"
     */
    fun formatCurrentGregorianDate(date: LocalDate = timeProvider.nowLocalDate()): String {
        val dayArabic = toArabicDigits(date.dayOfMonth)
        val monthArabic = GREGORIAN_MONTHS_AR.getOrElse(date.monthValue - 1) { "" }
        val yearArabic = toArabicDigits(date.year)
        return "$dayArabic $monthArabic $yearArabic"
    }

    /**
     * Computes the current Hijri date from the device's date using Java time chrono HijrahDate.
     * Isolated and calculated dynamically without hardcoding.
     */
    fun formatCurrentHijriDate(date: LocalDate = timeProvider.nowLocalDate()): String {
        return try {
            val hijrahDate = HijrahDate.from(date)
            val day = hijrahDate.get(ChronoField.DAY_OF_MONTH)
            val month = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
            val year = hijrahDate.get(ChronoField.YEAR)

            val dayArabic = toArabicDigits(day)
            val monthArabic = HIJRI_MONTHS_AR.getOrElse(month - 1) { "هجري" }
            val yearArabic = toArabicDigits(year)
            "$dayArabic $monthArabic $yearArabic هـ"
        } catch (e: Exception) {
            // Isolated safe fallback if chrono is unavailable on edge environment
            "التقويم الهجري متاح"
        }
    }

    /**
     * Formats duration between current device time and target prayer time into dynamic HH:mm:ss
     */
    fun formatCountdown(remainingSeconds: Long): String {
        val safeSeconds = remainingSeconds.coerceAtLeast(0)
        val hours = safeSeconds / 3600
        val minutes = (safeSeconds % 3600) / 60
        val seconds = safeSeconds % 60
        val raw = String.format(Locale.ROOT, "%02d:%02d:%02d", hours, minutes, seconds)
        return toArabicDigits(raw)
    }

    /**
     * Formats local time to Arabic 12-hour format e.g. "٠٤:٥٢ ص" or "٠٣:٤١ م"
     */
    fun formatTimeArabic(time: LocalTime): String {
        val hour12 = when (val h = time.hour % 12) {
            0 -> 12
            else -> h
        }
        val amPm = if (time.hour < 12) "ص" else "م"
        val raw = String.format(Locale.ROOT, "%02d:%02d", hour12, time.minute)
        return "${toArabicDigits(raw)} $amPm"
    }

    /**
     * Returns current device time formatted in Arabic.
     */
    fun formatCurrentTime(time: LocalTime = timeProvider.nowLocalTime()): String {
        return formatTimeArabic(time)
    }
}

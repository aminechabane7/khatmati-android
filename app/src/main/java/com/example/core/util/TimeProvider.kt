package com.example.core.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Single source of truth for the application's clock and timezone.
 * Uses the device's actual timezone via ZoneId.systemDefault().
 */
interface TimeProvider {
    val zoneId: ZoneId
    fun nowZoned(): ZonedDateTime
    fun nowLocalTime(): LocalTime
    fun nowLocalDate(): LocalDate
}

class DefaultTimeProvider(
    private val zoneProvider: () -> ZoneId = { ZoneId.systemDefault() }
) : TimeProvider {

    override val zoneId: ZoneId
        get() = zoneProvider()

    override fun nowZoned(): ZonedDateTime {
        return ZonedDateTime.now(zoneId)
    }

    override fun nowLocalTime(): LocalTime {
        return nowZoned().toLocalTime()
    }

    override fun nowLocalDate(): LocalDate {
        return nowZoned().toLocalDate()
    }
}

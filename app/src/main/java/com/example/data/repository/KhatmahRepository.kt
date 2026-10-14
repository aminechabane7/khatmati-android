package com.example.data.repository

import com.example.data.model.KhatmahPlan
import com.example.data.model.PrayerWirdProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class KhatmahRepository {

    private val _currentPlan = MutableStateFlow(
        KhatmahPlan(
            id = "plan_1",
            title = "ختمة ٣٠ يوماً",
            totalDays = 30,
            currentDay = 8,
            totalPages = 604,
            completedPages = 156,
            currentPage = 150,
            todayStartPage = 142,
            todayEndPage = 161,
            todayTargetPages = 20,
            todayCompletedPages = 8,
            isPrayerDivisionEnabled = true
        )
    )
    val currentPlan: StateFlow<KhatmahPlan> = _currentPlan.asStateFlow()

    fun updatePrayerCompletion(prayerName: String, isCompleted: Boolean) {
        val plan = _currentPlan.value
        val updatedPrayers = plan.prayerProgress.map { prayer ->
            if (prayer.prayerName == prayerName) {
                prayer.copy(isCompleted = isCompleted)
            } else {
                prayer
            }
        }
        val completedCount = updatedPrayers.filter { it.isCompleted }.sumOf { it.pagesCount }
        val newTodayCompleted = completedCount.coerceAtMost(plan.todayTargetPages)
        val previouslyCompletedBeforeToday = plan.completedPages - plan.todayCompletedPages
        val newOverallCompleted = (previouslyCompletedBeforeToday + newTodayCompleted).coerceAtMost(plan.totalPages)

        _currentPlan.value = plan.copy(
            prayerProgress = updatedPrayers,
            todayCompletedPages = newTodayCompleted,
            completedPages = newOverallCompleted,
            currentPage = (plan.todayStartPage + newTodayCompleted).coerceAtMost(plan.totalPages)
        )
    }

    fun togglePrayerDivision(enabled: Boolean) {
        _currentPlan.value = _currentPlan.value.copy(isPrayerDivisionEnabled = enabled)
    }

    fun createNewPlan(title: String, days: Int, divideByPrayers: Boolean) {
        val totalPages = 604
        val basePagesPerDay = totalPages / days
        val remainder = totalPages % days
        val firstDayTarget = basePagesPerDay + if (remainder > 0) 1 else 0

        val startPage = 1
        val endPage = (startPage + firstDayTarget - 1).coerceAtMost(totalPages)

        val prayerWirds = if (divideByPrayers) {
            calculatePrayerDistribution(firstDayTarget, startPage)
        } else {
            emptyList()
        }

        _currentPlan.value = KhatmahPlan(
            id = System.currentTimeMillis().toString(),
            title = title,
            totalDays = days,
            currentDay = 1,
            totalPages = totalPages,
            completedPages = 0,
            currentPage = 1,
            todayStartPage = startPage,
            todayEndPage = endPage,
            todayTargetPages = firstDayTarget,
            todayCompletedPages = 0,
            isPrayerDivisionEnabled = divideByPrayers,
            prayerProgress = prayerWirds
        )
    }

    // Distributes daily pages across the 5 prayers with remainder distributed evenly to earlier prayers
    fun calculatePrayerDistribution(dailyPages: Int, startPage: Int): List<PrayerWirdProgress> {
        val prayers = listOf("الفجر", "الظهر", "العصر", "المغرب", "العشاء")
        val base = dailyPages / 5
        val rem = dailyPages % 5

        var currentStart = startPage
        return prayers.mapIndexed { index, name ->
            val count = base + if (index < rem) 1 else 0
            val currentEnd = (currentStart + count - 1).coerceAtMost(604)
            val item = PrayerWirdProgress(
                prayerName = name,
                pagesCount = count,
                startPage = currentStart,
                endPage = currentEnd,
                isCompleted = false
            )
            currentStart = currentEnd + 1
            item
        }
    }
}

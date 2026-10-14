package com.example.data.model

data class DhikrCategory(
    val id: String,
    val arabicTitle: String,
    val subtitle: String,
    val itemsCount: Int,
    val completedItems: Int = 0
)

data class DhikrItem(
    val id: String,
    val categoryId: String,
    val textArabic: String,
    val referenceOrFadl: String,
    val targetCount: Int,
    val currentCount: Int = 0
) {
    val isCompleted: Boolean get() = currentCount >= targetCount
    val remainingCount: Int get() = (targetCount - currentCount).coerceAtLeast(0)
}

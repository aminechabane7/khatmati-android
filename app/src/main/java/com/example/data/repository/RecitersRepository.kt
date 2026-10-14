package com.example.data.repository

import com.example.data.model.Reciter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecitersRepository {

    private val defaultReciters = listOf(
        Reciter("alafasy", "مشاري راشد العفاسي", "حفص عن عاصم - مرتل", isSelected = true),
        Reciter("almuaiqly", "ماهر المعيقلي", "حفص عن عاصم - مرتل", isSelected = false),
        Reciter("aldossari", "ياسر الدوسري", "حفص عن عاصم - مرتل", isSelected = false),
        Reciter("alsudais", "عبد الرحمن السديس", "حفص عن عاصم - مرتل", isSelected = false),
        Reciter("alghamdi", "سعد الغامدي", "حفص عن عاصم - مرتل", isSelected = false)
    )

    private val _reciters = MutableStateFlow(defaultReciters)
    val reciters: StateFlow<List<Reciter>> = _reciters.asStateFlow()

    fun selectReciter(id: String) {
        _reciters.value = _reciters.value.map {
            it.copy(isSelected = it.id == id)
        }
    }

    fun getSelectedReciter(): Reciter {
        return _reciters.value.firstOrNull { it.isSelected } ?: defaultReciters.first()
    }
}

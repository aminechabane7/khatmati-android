package com.example.feature.quranreader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.model.Ayah
import com.example.data.model.Surah
import com.example.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class QuranReaderUiState(
    val surah: Surah,
    val verses: List<Ayah>,
    val currentPage: Int,
    val isBookmarked: Boolean = false,
    val readerMode: ReaderMode = ReaderMode.MUSHAF,
    val selectedAyah: Ayah? = null,
    val fontSizeSp: Float = 24f
)

class QuranReaderViewModel(
    savedStateHandle: SavedStateHandle,
    private val quranRepository: QuranRepository
) : ViewModel() {

    // Secondary constructor for callers or tests providing only repository
    constructor(quranRepository: QuranRepository) : this(SavedStateHandle(), quranRepository)

    // Source of truth: retrieve the selected Surah number from navigation arguments
    val surahNumber: Int = savedStateHandle.get<Int>("surahNumber")
        ?: savedStateHandle.get<String>("surahNumber")?.toIntOrNull()
        ?: 1

    private val allSurahs = quranRepository.getAllSurahs()

    private val initialSurah = quranRepository.getSurah(surahNumber) ?: allSurahs.first()

    private val _uiState = MutableStateFlow(
        QuranReaderUiState(
            surah = initialSurah,
            verses = quranRepository.getVersesForSurah(initialSurah.number),
            currentPage = initialSurah.startPage,
            isBookmarked = quranRepository.isBookmarked(initialSurah.number, 1)
        )
    )
    val uiState: StateFlow<QuranReaderUiState> = _uiState.asStateFlow()

    init {
        // Automatically register reading position for the selected Surah
        quranRepository.updateReadingPosition(initialSurah.number, 1, initialSurah.startPage)
    }

    fun loadSurah(surahNumber: Int, targetPage: Int? = null) {
        val selectedSurah = quranRepository.getSurah(surahNumber) ?: allSurahs.first()
        val verses = quranRepository.getVersesForSurah(selectedSurah.number)
        val initialPage = targetPage ?: selectedSurah.startPage
        val bookmarked = quranRepository.isBookmarked(selectedSurah.number, 1)

        _uiState.update { current ->
            current.copy(
                surah = selectedSurah,
                verses = verses,
                currentPage = initialPage,
                isBookmarked = bookmarked,
                selectedAyah = null
            )
        }

        quranRepository.updateReadingPosition(selectedSurah.number, 1, initialPage)
    }

    fun setPage(page: Int) {
        _uiState.update { current ->
            current.copy(currentPage = page.coerceIn(1, 604))
        }
        val currentSurah = _uiState.value.surah
        quranRepository.updateReadingPosition(currentSurah.number, 1, _uiState.value.currentPage)
    }

    fun setReaderMode(mode: ReaderMode) {
        _uiState.update { it.copy(readerMode = mode, selectedAyah = null) }
    }

    fun selectAyah(ayah: Ayah?) {
        _uiState.update { current ->
            val newSelected = if (current.selectedAyah?.ayahNumber == ayah?.ayahNumber) null else ayah
            current.copy(selectedAyah = newSelected)
        }
        if (ayah != null) {
            val currentSurah = _uiState.value.surah
            quranRepository.updateReadingPosition(currentSurah.number, ayah.ayahNumber, _uiState.value.currentPage)
        }
    }

    fun toggleBookmark() {
        val current = _uiState.value
        val ayahNum = current.selectedAyah?.ayahNumber ?: 1
        quranRepository.toggleBookmark(current.surah.number, ayahNum, current.currentPage)
        val updated = quranRepository.isBookmarked(current.surah.number, ayahNum)
        _uiState.update { it.copy(isBookmarked = updated) }
    }

    fun increaseFontSize() {
        _uiState.update { it.copy(fontSizeSp = (it.fontSizeSp + 2f).coerceAtMost(36f)) }
    }

    fun decreaseFontSize() {
        _uiState.update { it.copy(fontSizeSp = (it.fontSizeSp - 2f).coerceAtLeast(18f)) }
    }
}

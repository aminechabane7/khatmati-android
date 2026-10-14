package com.example

import androidx.lifecycle.SavedStateHandle
import com.example.core.util.DateTimeHelper
import com.example.data.repository.PrayerRepository
import com.example.data.repository.QuranRepository
import com.example.feature.quranreader.QuranReaderViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalTime

class ExampleUnitTest {

    @Test
    fun `test all 114 surahs exist and have valid SurahUiModels`() {
        val repo = QuranRepository()
        val models = repo.getAllSurahUiModels()
        assertEquals(114, models.size)
        assertEquals(1, models.first().number)
        assertEquals("الفاتحة", models.first().nameArabic)
        assertEquals(114, models.last().number)
        assertEquals("الناس", models.last().nameArabic)

        // Verify all Surahs have unique numbers from 1 to 114
        val numbers = models.map { it.number }
        assertEquals((1..114).toList(), numbers)
    }

    @Test
    fun `test QuranReaderViewModel extracts surahNumber from SavedStateHandle correctly`() {
        val repo = QuranRepository()

        // 1. Surah 1 (Al-Fatihah)
        val handle1 = SavedStateHandle(mapOf("surahNumber" to 1))
        val vm1 = QuranReaderViewModel(handle1, repo)
        assertEquals(1, vm1.surahNumber)
        assertEquals("الفاتحة", vm1.uiState.value.surah.arabicName)
        assertEquals(7, vm1.uiState.value.verses.size)

        // 2. Surah 3 (Aal-Imran)
        val handle3 = SavedStateHandle(mapOf("surahNumber" to 3))
        val vm3 = QuranReaderViewModel(handle3, repo)
        assertEquals(3, vm3.surahNumber)
        assertEquals("آل عمران", vm3.uiState.value.surah.arabicName)

        // 3. Surah 112 (Al-Ikhlas)
        val handle112 = SavedStateHandle(mapOf("surahNumber" to 112))
        val vm112 = QuranReaderViewModel(handle112, repo)
        assertEquals(112, vm112.surahNumber)
        assertEquals("الإخلاص", vm112.uiState.value.surah.arabicName)
        assertEquals(4, vm112.uiState.value.verses.size)

        // 4. Surah 114 (An-Nas)
        val handle114 = SavedStateHandle(mapOf("surahNumber" to 114))
        val vm114 = QuranReaderViewModel(handle114, repo)
        assertEquals(114, vm114.surahNumber)
        assertEquals("الناس", vm114.uiState.value.surah.arabicName)
        assertEquals(6, vm114.uiState.value.verses.size)
    }

    @Test
    fun `test QuranReaderViewModel loadSurah dynamically updates uiState`() {
        val repo = QuranRepository()
        val handle = SavedStateHandle(mapOf("surahNumber" to 1))
        val vm = QuranReaderViewModel(handle, repo)

        assertEquals(1, vm.uiState.value.surah.number)

        // Dynamically load Surah 18 (Al-Kahf)
        vm.loadSurah(18)
        assertEquals(18, vm.uiState.value.surah.number)
        assertEquals("الكهف", vm.uiState.value.surah.arabicName)
    }

    @Test
    fun `test formatTimeArabic produces valid 12-hour Arabic time`() {
        val morning = LocalTime.of(4, 52)
        val morningStr = DateTimeHelper.formatTimeArabic(morning)
        assertEquals("٠٤:٥٢ ص", morningStr)

        val afternoon = LocalTime.of(15, 41)
        val afternoonStr = DateTimeHelper.formatTimeArabic(afternoon)
        assertEquals("٠٣:٤١ م", afternoonStr)

        val midnight = LocalTime.of(0, 0)
        val midnightStr = DateTimeHelper.formatTimeArabic(midnight)
        assertEquals("١٢:٠٠ ص", midnightStr)

        val noon = LocalTime.of(12, 0)
        val noonStr = DateTimeHelper.formatTimeArabic(noon)
        assertEquals("١٢:٠٠ م", noonStr)
    }

    @Test
    fun `test PrayerRepository calculates current time and countdown dynamically`() {
        val prayerRepo = PrayerRepository()
        val schedule = prayerRepo.computeCurrentSchedule()

        assertNotNull(schedule.currentTimeFormatted)
        assertTrue(schedule.currentTimeFormatted.isNotBlank())
        assertTrue(schedule.currentTimeFormatted.contains("ص") || schedule.currentTimeFormatted.contains("م"))
        assertNotNull(schedule.nextPrayerName)
        assertTrue(schedule.nextPrayerCountdown.isNotBlank())
        assertEquals("مكة المكرمة", schedule.cityName)
    }
}

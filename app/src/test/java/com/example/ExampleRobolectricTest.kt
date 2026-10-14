package com.example

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import com.example.core.util.DefaultTimeProvider
import com.example.core.util.DateTimeHelper
import com.example.core.util.TimeProvider
import com.example.data.model.SurahUiModel
import com.example.data.repository.PrayerRepository
import com.example.data.repository.QuranRepository
import com.example.feature.quranreader.QuranReaderViewModel
import com.example.navigation.KhatmatiDestinations
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ختمتي", appName)
    }

    @Test
    fun `surah repository and viewmodel navigation sequence test`() {
        val repo = QuranRepository()

        // 1. Repository getSurah(id) verification
        val fatiha = repo.getSurah(1)
        assertNotNull(fatiha)
        assertEquals("الفاتحة", fatiha!!.arabicName)
        assertEquals(7, fatiha.versesCount)

        val baqarah = repo.getSurah(2)
        assertNotNull(baqarah)
        assertEquals("البقرة", baqarah!!.arabicName)
        assertEquals(286, baqarah.versesCount)

        val aalImran = repo.getSurah(3)
        assertNotNull(aalImran)
        assertEquals("آل عمران", aalImran!!.arabicName)
        assertEquals(200, aalImran.versesCount)

        val nisa = repo.getSurah(4)
        assertNotNull(nisa)
        assertEquals("النساء", nisa!!.arabicName)
        assertEquals(176, nisa.versesCount)

        // 2. Navigation routes
        assertEquals("quranReader/1", KhatmatiDestinations.quranReaderRoute(1))
        assertEquals("quranReader/2", KhatmatiDestinations.quranReaderRoute(2))
        assertEquals("quranReader/3", KhatmatiDestinations.quranReaderRoute(3))
        assertEquals("quranReader/4", KhatmatiDestinations.quranReaderRoute(4))

        // 3. ViewModel state test with SavedStateHandle
        val handle1 = SavedStateHandle(mapOf("surahNumber" to 1))
        val vm1 = QuranReaderViewModel(handle1, repo)
        assertEquals(1, vm1.uiState.value.surah.number)
        assertEquals("الفاتحة", vm1.uiState.value.surah.arabicName)

        val handle2 = SavedStateHandle(mapOf("surahNumber" to 2))
        val vm2 = QuranReaderViewModel(handle2, repo)
        assertEquals(2, vm2.uiState.value.surah.number)
        assertEquals("البقرة", vm2.uiState.value.surah.arabicName)

        val handle3 = SavedStateHandle(mapOf("surahNumber" to 3))
        val vm3 = QuranReaderViewModel(handle3, repo)
        assertEquals(3, vm3.uiState.value.surah.number)
        assertEquals("آل عمران", vm3.uiState.value.surah.arabicName)

        val handle4 = SavedStateHandle(mapOf("surahNumber" to 4))
        val vm4 = QuranReaderViewModel(handle4, repo)
        assertEquals(4, vm4.uiState.value.surah.number)
        assertEquals("النساء", vm4.uiState.value.surah.arabicName)

        // 4. Switching Surahs on same ViewModel (loadSurah)
        vm1.loadSurah(2)
        assertEquals(2, vm1.uiState.value.surah.number)
        assertEquals("البقرة", vm1.uiState.value.surah.arabicName)

        vm1.loadSurah(1)
        assertEquals(1, vm1.uiState.value.surah.number)
        assertEquals("الفاتحة", vm1.uiState.value.surah.arabicName)
    }

    @Test
    fun `time provider and prayer countdown test`() {
        val testZone = ZoneId.systemDefault()
        val mockTime = LocalTime.of(10, 0) // Before Dhuhr (12:18)
        val mockDate = LocalDate.of(2026, 10, 7)

        val testProvider = object : TimeProvider {
            override val zoneId: ZoneId = testZone
            override fun nowZoned(): ZonedDateTime = ZonedDateTime.of(mockDate, mockTime, testZone)
            override fun nowLocalTime(): LocalTime = mockTime
            override fun nowLocalDate(): LocalDate = mockDate
        }

        val prayerRepo = PrayerRepository(timeProvider = testProvider)
        val schedule = prayerRepo.computeCurrentSchedule()

        // Verify next prayer is Dhuhr (الظهر)
        assertEquals("الظهر", schedule.nextPrayerName)
        assertTrue(schedule.nextPrayerCountdown.isNotBlank())
        assertTrue(schedule.isMockLocation)
        assertEquals("مكة المكرمة", schedule.cityName)

        // Test after Isha transition to next day's Fajr
        val lateNightTime = LocalTime.of(22, 0) // After Isha (19:35)
        val lateNightProvider = object : TimeProvider {
            override val zoneId: ZoneId = testZone
            override fun nowZoned(): ZonedDateTime = ZonedDateTime.of(mockDate, lateNightTime, testZone)
            override fun nowLocalTime(): LocalTime = lateNightTime
            override fun nowLocalDate(): LocalDate = mockDate
        }

        val lateNightRepo = PrayerRepository(timeProvider = lateNightProvider)
        val lateSchedule = lateNightRepo.computeCurrentSchedule()

        assertEquals("الفجر", lateSchedule.nextPrayerName)
        assertTrue(lateSchedule.nextPrayerCountdown.isNotBlank())
    }
}

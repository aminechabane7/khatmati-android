package com.example

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.repository.AdhkarRepository
import com.example.data.repository.KhatmahRepository
import com.example.data.repository.PrayerRepository
import com.example.data.repository.QuranRepository
import com.example.data.repository.RecitersRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.feature.adhkar.DhikrReaderScreen
import com.example.feature.khatmah.CreateKhatmahScreen
import com.example.feature.main.MainScreen
import com.example.feature.onboarding.OnboardingScreen
import com.example.feature.prayer.PrayerTimesScreen
import com.example.feature.quranreader.QuranReaderScreen
import com.example.feature.reciters.RecitersScreen
import com.example.navigation.KhatmatiDestinations
import com.example.ui.theme.KhatmatiTheme
import kotlinx.coroutines.launch

@Composable
fun KhatmatiApp() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Repositories
    val preferencesRepo = remember { UserPreferencesRepository(context) }
    val quranRepo = remember { QuranRepository() }
    val khatmahRepo = remember { KhatmahRepository() }
    val adhkarRepo = remember { AdhkarRepository() }
    val prayerRepo = remember { PrayerRepository() }
    val recitersRepo = remember { RecitersRepository() }

    // State Collection
    val isOnboardingCompleted by preferencesRepo.isOnboardingCompleted.collectAsState(initial = false)
    val quranFontSize by preferencesRepo.quranFontSize.collectAsState(initial = 24f)
    val quranReadingMode by preferencesRepo.quranReadingMode.collectAsState(initial = "mushaf")
    val themeMode by preferencesRepo.themeMode.collectAsState(initial = "system")

    val readingPosition by quranRepo.readingPosition.collectAsState()
    val bookmarks by quranRepo.bookmarks.collectAsState()
    val surahs = remember { quranRepo.getAllSurahUiModels() }

    val currentPlan by khatmahRepo.currentPlan.collectAsState()
    val adhkarCategories = remember { adhkarRepo.categories }
    val dhikrItems by adhkarRepo.items.collectAsState()

    val prayerSchedule by prayerRepo.scheduleFlow.collectAsState(initial = prayerRepo.computeCurrentSchedule())
    val availableCities = remember { prayerRepo.availableCities }

    val reciters by recitersRepo.reciters.collectAsState()
    val selectedReciter = remember(reciters) { recitersRepo.getSelectedReciter() }

    val isDark = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    KhatmatiTheme(darkTheme = isDark) {
        val navController = rememberNavController()
        val startDestination = if (isOnboardingCompleted) {
            KhatmatiDestinations.MAIN
        } else {
            KhatmatiDestinations.ONBOARDING
        }

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            // Onboarding
            composable(KhatmatiDestinations.ONBOARDING) {
                OnboardingScreen(
                    onFinishOnboarding = {
                        coroutineScope.launch {
                            preferencesRepo.setOnboardingCompleted(true)
                            navController.navigate(KhatmatiDestinations.MAIN) {
                                popUpTo(KhatmatiDestinations.ONBOARDING) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // Main (Home, Quran Index, Khatmati, Adhkar Index, More)
            composable(KhatmatiDestinations.MAIN) {
                MainScreen(
                    prayerSchedule = prayerSchedule,
                    readingPosition = readingPosition,
                    khatmahPlan = currentPlan,
                    surahs = surahs,
                    adhkarCategories = adhkarCategories,
                    selectedReciter = selectedReciter,
                    quranFontSize = quranFontSize,
                    quranReadingMode = quranReadingMode,
                    themeMode = themeMode,
                    onOpenQuranReader = { surahNumber ->
                        navController.navigate(KhatmatiDestinations.quranReaderRoute(surahNumber))
                    },
                    onCreateKhatmahClick = {
                        navController.navigate(KhatmatiDestinations.CREATE_KHATMAH)
                    },
                    onOpenPrayerTimesClick = {
                        navController.navigate(KhatmatiDestinations.PRAYER_TIMES)
                    },
                    onOpenRecitersClick = {
                        navController.navigate(KhatmatiDestinations.RECITERS)
                    },
                    onOpenDhikrReader = { categoryId ->
                        navController.navigate(KhatmatiDestinations.dhikrReaderRoute(categoryId))
                    },
                    onPrayerToggle = { prayerName, isCompleted ->
                        khatmahRepo.updatePrayerCompletion(prayerName, isCompleted)
                    },
                    onTogglePrayerDivision = { enabled ->
                        khatmahRepo.togglePrayerDivision(enabled)
                    },
                    onUpdateFontSize = { size ->
                        coroutineScope.launch { preferencesRepo.setQuranFontSize(size) }
                    },
                    onUpdateReadingMode = { mode ->
                        coroutineScope.launch { preferencesRepo.setQuranReadingMode(mode) }
                    },
                    onUpdateThemeMode = { mode ->
                        coroutineScope.launch { preferencesRepo.setThemeMode(mode) }
                    }
                )
            }

            // Fullscreen Quran Reader
            composable(
                route = KhatmatiDestinations.QURAN_READER,
                arguments = listOf(
                    navArgument("surahNumber") { type = NavType.IntType; defaultValue = 1 }
                )
            ) { backStackEntry ->
                val surahNumber = backStackEntry.arguments?.getInt("surahNumber") ?: 1
                val viewModel = remember(surahNumber) {
                    val handle = backStackEntry.savedStateHandle.apply {
                        set("surahNumber", surahNumber)
                    }
                    com.example.feature.quranreader.QuranReaderViewModel(handle, quranRepo)
                }

                LaunchedEffect(surahNumber) {
                    viewModel.loadSurah(surahNumber)
                }

                QuranReaderScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Create Khatmah
            composable(KhatmatiDestinations.CREATE_KHATMAH) {
                CreateKhatmahScreen(
                    onSavePlan = { title, days, divideByPrayers ->
                        khatmahRepo.createNewPlan(title, days, divideByPrayers)
                        navController.popBackStack()
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Dhikr Reader & Counter
            composable(
                route = KhatmatiDestinations.DHIKR_READER,
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "morning"
                val category = adhkarCategories.find { it.id == categoryId }
                val categoryTitle = category?.arabicTitle ?: "الأذكار"
                val items = adhkarRepo.getItemsForCategory(categoryId)

                DhikrReaderScreen(
                    categoryTitle = categoryTitle,
                    items = items,
                    onIncrementDhikr = { adhkarRepo.incrementDhikr(it) },
                    onResetDhikr = { adhkarRepo.resetDhikr(it) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Prayer Times Screen
            composable(KhatmatiDestinations.PRAYER_TIMES) {
                PrayerTimesScreen(
                    schedule = prayerSchedule,
                    availableCities = availableCities,
                    onSelectCity = { prayerRepo.updateCity(it) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Reciters Selector Screen
            composable(KhatmatiDestinations.RECITERS) {
                RecitersScreen(
                    reciters = reciters,
                    onSelectReciter = { recitersRepo.selectReciter(it) },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

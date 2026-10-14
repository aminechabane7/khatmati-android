package com.example.feature.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Mosque
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DhikrCategory
import com.example.data.model.KhatmahPlan
import com.example.data.model.PrayerSchedule
import com.example.data.model.ReadingPosition
import com.example.data.model.Reciter
import com.example.data.model.SurahUiModel
import com.example.feature.adhkar.AdhkarIndexScreen
import com.example.feature.home.HomeScreen
import com.example.feature.khatmah.KhatmatiScreen
import com.example.feature.quran.QuranIndexScreen
import com.example.feature.settings.SettingsScreen
import com.example.navigation.MainBottomTab
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.WarmBorder

@Composable
fun MainScreen(
    // Home & Quran data
    prayerSchedule: PrayerSchedule,
    readingPosition: ReadingPosition,
    khatmahPlan: KhatmahPlan,
    surahs: List<SurahUiModel>,
    adhkarCategories: List<DhikrCategory>,
    selectedReciter: Reciter,
    quranFontSize: Float,
    quranReadingMode: String,
    themeMode: String,

    // Navigation triggers
    onOpenQuranReader: (Int) -> Unit, // surahNumber
    onCreateKhatmahClick: () -> Unit,
    onOpenPrayerTimesClick: () -> Unit,
    onOpenRecitersClick: () -> Unit,
    onOpenDhikrReader: (String) -> Unit,
    onPrayerToggle: (String, Boolean) -> Unit,
    onTogglePrayerDivision: (Boolean) -> Unit,
    onUpdateFontSize: (Float) -> Unit,
    onUpdateReadingMode: (String) -> Unit,
    onUpdateThemeMode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(MainBottomTab.HOME) }

    BackHandler(enabled = selectedTab != MainBottomTab.HOME) {
        selectedTab = MainBottomTab.HOME
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            KhatmatiBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainBottomTab.HOME -> {
                    HomeScreen(
                        prayerSchedule = prayerSchedule,
                        readingPosition = readingPosition,
                        khatmahPlan = khatmahPlan,
                        onContinueReadingClick = onOpenQuranReader,
                        onNavigateToKhatmati = { selectedTab = MainBottomTab.KHATMATI },
                        onNavigateToPrayerTimes = onOpenPrayerTimesClick,
                        onNavigateToAdhkarCategory = onOpenDhikrReader
                    )
                }

                MainBottomTab.QURAN -> {
                    QuranIndexScreen(
                        surahs = surahs,
                        readingPosition = readingPosition,
                        onSurahClick = onOpenQuranReader,
                        onContinueReadingClick = {
                            onOpenQuranReader(readingPosition.surahNumber)
                        }
                    )
                }

                MainBottomTab.KHATMATI -> {
                    KhatmatiScreen(
                        plan = khatmahPlan,
                        onContinueWirdClick = onOpenQuranReader,
                        onPrayerToggle = onPrayerToggle,
                        onTogglePrayerDivision = onTogglePrayerDivision,
                        onCreateNewKhatmahClick = onCreateKhatmahClick
                    )
                }

                MainBottomTab.ADHKAR -> {
                    AdhkarIndexScreen(
                        categories = adhkarCategories,
                        onCategoryClick = onOpenDhikrReader
                    )
                }

                MainBottomTab.MORE -> {
                    SettingsScreen(
                        selectedReciter = selectedReciter,
                        quranFontSize = quranFontSize,
                        quranReadingMode = quranReadingMode,
                        themeMode = themeMode,
                        onNavigateToReciters = onOpenRecitersClick,
                        onNavigateToPrayerTimes = onOpenPrayerTimesClick,
                        onUpdateFontSize = onUpdateFontSize,
                        onUpdateReadingMode = onUpdateReadingMode,
                        onUpdateThemeMode = onUpdateThemeMode
                    )
                }
            }
        }
    }
}

@Composable
fun KhatmatiBottomBar(
    selectedTab: MainBottomTab,
    onTabSelected: (MainBottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. الرئيسية
            BottomNavItem(
                title = "الرئيسية",
                iconSelected = Icons.Filled.Home,
                iconUnselected = Icons.Outlined.Home,
                isSelected = selectedTab == MainBottomTab.HOME,
                onClick = { onTabSelected(MainBottomTab.HOME) }
            )

            // 2. القرآن
            BottomNavItem(
                title = "القرآن",
                iconSelected = Icons.AutoMirrored.Filled.MenuBook,
                iconUnselected = Icons.AutoMirrored.Outlined.MenuBook,
                isSelected = selectedTab == MainBottomTab.QURAN,
                onClick = { onTabSelected(MainBottomTab.QURAN) }
            )

            // 3. ختمتي (Distinctive Center Prominent Position)
            KhatmatiCenterNavItem(
                title = "ختمتي",
                isSelected = selectedTab == MainBottomTab.KHATMATI,
                onClick = { onTabSelected(MainBottomTab.KHATMATI) }
            )

            // 4. الأذكار
            BottomNavItem(
                title = "الأذكار",
                iconSelected = Icons.Filled.Mosque,
                iconUnselected = Icons.Outlined.Mosque,
                isSelected = selectedTab == MainBottomTab.ADHKAR,
                onClick = { onTabSelected(MainBottomTab.ADHKAR) }
            )

            // 5. المزيد
            BottomNavItem(
                title = "المزيد",
                iconSelected = Icons.Filled.MoreHoriz,
                iconUnselected = Icons.Outlined.MoreHoriz,
                isSelected = selectedTab == MainBottomTab.MORE,
                onClick = { onTabSelected(MainBottomTab.MORE) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    title: String,
    iconSelected: ImageVector,
    iconUnselected: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("nav_item_$title"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) iconSelected else iconUnselected,
            contentDescription = title,
            tint = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun KhatmatiCenterNavItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = 6.dp, vertical = 2.dp)
            .testTag("nav_center_item_khatmati"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Distinctive raised circular container with Emerald and Gold
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(if (isSelected) EmeraldPrimary else EmeraldContainer)
                .shadow(elevation = if (isSelected) 4.dp else 1.dp, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AutoStories,
                contentDescription = title,
                tint = if (isSelected) GoldAccent else EmeraldPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

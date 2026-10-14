package com.example.feature.quranreader

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.util.DateTimeHelper
import com.example.data.model.Ayah
import com.example.data.model.Surah
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldOnContainer
import com.example.ui.theme.QuranTextStyle
import com.example.ui.theme.WarmBackground
import com.example.ui.theme.WarmBorder
import kotlinx.coroutines.launch

enum class ReaderMode(val label: String) {
    MUSHAF("وضع المصحف"),
    AYAH("وضع الآيات")
}

@Composable
fun QuranReaderScreen(
    viewModel: QuranReaderViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    QuranReaderScreen(
        surah = uiState.surah,
        verses = uiState.verses,
        currentPage = uiState.currentPage,
        isBookmarked = uiState.isBookmarked,
        readerMode = uiState.readerMode,
        selectedAyah = uiState.selectedAyah,
        fontSizeSp = uiState.fontSizeSp,
        onModeChange = viewModel::setReaderMode,
        onSelectAyah = viewModel::selectAyah,
        onPageChange = viewModel::setPage,
        onFontSizeDecrease = viewModel::decreaseFontSize,
        onFontSizeIncrease = viewModel::increaseFontSize,
        onToggleBookmark = viewModel::toggleBookmark,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun QuranReaderScreen(
    surah: Surah,
    verses: List<Ayah>,
    currentPage: Int,
    isBookmarked: Boolean,
    readerMode: ReaderMode,
    selectedAyah: Ayah?,
    fontSizeSp: Float,
    onModeChange: (ReaderMode) -> Unit,
    onSelectAyah: (Ayah?) -> Unit,
    onPageChange: (Int) -> Unit,
    onFontSizeDecrease: () -> Unit,
    onFontSizeIncrease: () -> Unit,
    onToggleBookmark: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackClick() }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Top Controls Bar
            ReaderTopBar(
                surah = surah,
                currentPage = currentPage,
                readerMode = readerMode,
                fontSize = fontSizeSp,
                isBookmarked = isBookmarked,
                onModeChange = onModeChange,
                onFontSizeDecrease = onFontSizeDecrease,
                onFontSizeIncrease = onFontSizeIncrease,
                onBookmarkClick = onToggleBookmark,
                onBackClick = onBackClick
            )

            // 2. Reading Content (Mushaf vs Ayah)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (readerMode) {
                    ReaderMode.MUSHAF -> {
                        MushafModeView(
                            surah = surah,
                            verses = verses,
                            fontSizeSp = fontSizeSp,
                            currentPage = currentPage,
                            onPageChange = onPageChange
                        )
                    }
                    ReaderMode.AYAH -> {
                        AyahModeView(
                            surah = surah,
                            verses = verses,
                            selectedAyah = selectedAyah,
                            fontSizeSp = fontSizeSp,
                            onSelectAyah = { ayah ->
                                onSelectAyah(if (selectedAyah?.ayahNumber == ayah.ayahNumber) null else ayah)
                            }
                        )
                    }
                }
            }
        }

        // 3. Contextual Action Bar for Selected Ayah (Ayah Mode)
        AnimatedVisibility(
            visible = readerMode == ReaderMode.AYAH && selectedAyah != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            selectedAyah?.let { ayah ->
                AyahContextualActions(
                    ayah = ayah,
                    surah = surah,
                    onPlay = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("تشغيل الآية ${DateTimeHelper.toArabicDigits(ayah.ayahNumber)} من سورة ${surah.arabicName}")
                        }
                    },
                    onBookmark = {
                        onToggleBookmark()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("تم حفظ الإشارة المرجعية للآية ${DateTimeHelper.toArabicDigits(ayah.ayahNumber)}")
                        }
                    },
                    onShare = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("مشاركة نص الآية")
                        }
                    },
                    onTafsir = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("تفسير الآية ${DateTimeHelper.toArabicDigits(ayah.ayahNumber)}")
                        }
                    },
                    onDismiss = { onSelectAyah(null) }
                )
            }
        }

        // Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        )
    }
}

@Composable
private fun ReaderTopBar(
    surah: Surah,
    currentPage: Int,
    readerMode: ReaderMode,
    fontSize: Float,
    isBookmarked: Boolean,
    onModeChange: (ReaderMode) -> Unit,
    onFontSizeDecrease: () -> Unit,
    onFontSizeIncrease: () -> Unit,
    onBookmarkClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val pageArabic = DateTimeHelper.toArabicDigits(currentPage)
    val juzArabic = DateTimeHelper.toArabicDigits(((currentPage - 1) / 20) + 1)
    val versesCountArabic = DateTimeHelper.toArabicDigits(surah.versesCount)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back & Surah Information
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("reader_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(modifier = Modifier.padding(start = 4.dp)) {
                        Text(
                            text = "سورة ${surah.arabicName}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.testTag("reader_surah_title")
                        )
                        Text(
                            text = "صفحة $pageArabic • جزء $juzArabic • $versesCountArabic آية",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.testTag("reader_surah_metadata")
                        )
                    }
                }

                // Controls: Font Size, Bookmark, Mode toggle
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Font Size adjustments
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "A-",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary,
                            modifier = Modifier
                                .clickable { onFontSizeDecrease() }
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                        Text(
                            text = "A+",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary,
                            modifier = Modifier
                                .clickable { onFontSizeIncrease() }
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Bookmark toggle
                    IconButton(
                        onClick = onBookmarkClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "حفظ علامة",
                            tint = if (isBookmarked) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Mode Switcher Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(EmeraldContainer)
                            .clickable {
                                onModeChange(
                                    if (readerMode == ReaderMode.MUSHAF) ReaderMode.AYAH else ReaderMode.MUSHAF
                                )
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("reader_mode_switcher"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (readerMode == ReaderMode.MUSHAF) "وضع الآيات" else "وضع المصحف",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MushafModeView(
    surah: Surah,
    verses: List<Ayah>,
    fontSizeSp: Float,
    currentPage: Int,
    onPageChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Mushaf Page Frame
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldAccent.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Surah Header Ornament
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(GoldContainer)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "﴿ سُورَةُ ${surah.arabicName} ﴾",
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldOnContainer,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Bismillah (except for Surah At-Tawbah)
                if (surah.number != 9) {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        style = QuranTextStyle.copy(
                            fontSize = (fontSizeSp + 2).sp,
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 18.dp)
                    )
                }

                // Continuous Page-Flow Quran Text
                val combinedText = buildString {
                    verses.forEach { ayah ->
                        val text = if (ayah.ayahNumber == 1 && surah.number != 1 && ayah.textUthmani.startsWith("بِسْمِ اللَّهِ")) {
                            val stripped = ayah.textUthmani.removePrefix("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ").trim()
                            if (stripped.isNotBlank()) stripped else ayah.textUthmani
                        } else {
                            ayah.textUthmani
                        }
                        if (text.isNotBlank()) {
                            append(text)
                            append(" ﴿${DateTimeHelper.toArabicDigits(ayah.ayahNumber)}﴾ ")
                        }
                    }
                }

                Text(
                    text = combinedText,
                    style = QuranTextStyle.copy(
                        fontSize = fontSizeSp.sp,
                        lineHeight = (fontSizeSp * 2.1f).sp,
                        textAlign = TextAlign.Justify
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Page Navigation Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (currentPage > 1) onPageChange(currentPage - 1) },
                enabled = currentPage > 1,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "الصفحة السابقة",
                    tint = if (currentPage > 1) EmeraldPrimary else MaterialTheme.colorScheme.outline
                )
            }

            Text(
                text = "صفحة ${DateTimeHelper.toArabicDigits(currentPage)} من ٦٠٤",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            IconButton(
                onClick = { if (currentPage < 604) onPageChange(currentPage + 1) },
                enabled = currentPage < 604,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "الصفحة التالية",
                    tint = if (currentPage < 604) EmeraldPrimary else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun AyahModeView(
    surah: Surah,
    verses: List<Ayah>,
    selectedAyah: Ayah?,
    fontSizeSp: Float,
    onSelectAyah: (Ayah) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Surah Header Ornament for Ayah Mode
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(GoldContainer)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "﴿ سُورَةُ ${surah.arabicName} ﴾",
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldOnContainer,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }

                if (surah.number != 9 && surah.number != 1) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        style = QuranTextStyle.copy(
                            fontSize = (fontSizeSp + 2).sp,
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }
        }

        items(verses, key = { "${it.surahNumber}_${it.ayahNumber}" }) { ayah ->
            val isSelected = selectedAyah?.ayahNumber == ayah.ayahNumber

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onSelectAyah(ayah) },
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) EmeraldContainer else MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = DateTimeHelper.toArabicDigits(ayah.ayahNumber),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (isSelected) {
                            Text(
                                text = "محددة",
                                style = MaterialTheme.typography.labelSmall,
                                color = EmeraldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = ayah.textUthmani,
                        style = QuranTextStyle.copy(
                            fontSize = fontSizeSp.sp,
                            lineHeight = (fontSizeSp * 2f).sp,
                            textAlign = TextAlign.Right
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun AyahContextualActions(
    ayah: Ayah,
    surah: Surah,
    onPlay: () -> Unit,
    onBookmark: () -> Unit,
    onShare: () -> Unit,
    onTafsir: () -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${surah.arabicName} • الآية ${DateTimeHelper.toArabicDigits(ayah.ayahNumber)}",
                    style = MaterialTheme.typography.labelLarge,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "إلغاء التحديد",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable { onDismiss() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AyahActionButton(
                    title = "تشغيل",
                    icon = Icons.Default.PlayArrow,
                    onClick = onPlay,
                    modifier = Modifier.weight(1f)
                )
                AyahActionButton(
                    title = "حفظ",
                    icon = Icons.Default.Bookmark,
                    onClick = onBookmark,
                    modifier = Modifier.weight(1f)
                )
                AyahActionButton(
                    title = "مشاركة",
                    icon = Icons.Default.Share,
                    onClick = onShare,
                    modifier = Modifier.weight(1f)
                )
                AyahActionButton(
                    title = "تفسير",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    onClick = onTafsir,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun AyahActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(EmeraldContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = EmeraldPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

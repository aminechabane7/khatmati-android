package com.example.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.component.KhatmatiBadge
import com.example.core.designsystem.component.KhatmatiCard
import com.example.data.model.Reciter
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldOnContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldOnContainer
import com.example.ui.theme.WarmBorder

@Composable
fun SettingsScreen(
    selectedReciter: Reciter,
    quranFontSize: Float,
    quranReadingMode: String,
    themeMode: String,
    onNavigateToReciters: () -> Unit,
    onNavigateToPrayerTimes: () -> Unit,
    onUpdateFontSize: (Float) -> Unit,
    onUpdateReadingMode: (String) -> Unit,
    onUpdateThemeMode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFontSizeDialog by remember { mutableStateOf(false) }
    var currentFontSize by remember(quranFontSize) { mutableFloatStateOf(quranFontSize) }

    var notificationsEnabled by remember { mutableStateOf(true) }
    var showAboutDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Title
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "المزيد",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "الإعدادات والتفضيلات والحساب",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 1. Guest Account Card
        item {
            KhatmatiCard(
                backgroundColor = EmeraldContainer,
                borderColor = EmeraldPrimary.copy(alpha = 0.2f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "مستخدم ختمتي",
                                style = MaterialTheme.typography.titleMedium,
                                color = EmeraldOnContainer,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "الوضع المحلي (زائر) • حفظ البيانات محلياً",
                                style = MaterialTheme.typography.bodySmall,
                                color = EmeraldOnContainer.copy(alpha = 0.8f)
                            )
                        }
                    }

                    KhatmatiBadge(
                        text = "محلي",
                        containerColor = GoldContainer,
                        textColor = GoldOnContainer
                    )
                }
            }
        }

        // 2. Quran & Reading Section
        item {
            SettingsSectionHeader(title = "القرآن والتلاوة")
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingsItemRow(
                    title = "القارئ المفضل",
                    subtitle = selectedReciter.arabicName,
                    icon = Icons.Default.RecordVoiceOver,
                    onClick = onNavigateToReciters
                )

                SettingsItemRow(
                    title = "حجم خط المصحف",
                    subtitle = "${quranFontSize.toInt()} نقطة",
                    icon = Icons.Default.FormatSize,
                    onClick = { showFontSizeDialog = true }
                )

                SettingsItemRow(
                    title = "طريقة عرض المصحف الافتراضية",
                    subtitle = if (quranReadingMode == "mushaf") "وضع المصحف (صفحات)" else "وضع الآيات",
                    icon = Icons.Default.ViewList,
                    onClick = {
                        val nextMode = if (quranReadingMode == "mushaf") "ayah" else "mushaf"
                        onUpdateReadingMode(nextMode)
                    }
                )
            }
        }

        // 3. Worship & Prayers Section
        item {
            SettingsSectionHeader(title = "العبادات والمواقيت")
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingsItemRow(
                    title = "مواقيت الصلاة",
                    subtitle = "أم القرى - مكة المكرمة",
                    icon = Icons.Default.Mosque,
                    onClick = onNavigateToPrayerTimes
                )

                SettingsItemRow(
                    title = "الموقع الحالي",
                    subtitle = "مكة المكرمة (تحديد يدوي متاح)",
                    icon = Icons.Default.LocationOn,
                    onClick = onNavigateToPrayerTimes
                )
            }
        }

        // 4. Preferences Section
        item {
            SettingsSectionHeader(title = "التفضيلات والتطبيق")
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Notifications switch
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "التنبيهات والإشعارات",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "تذكير الورد اليومي ومواقيت الصلاة",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = EmeraldPrimary
                            )
                        )
                    }
                }

                SettingsItemRow(
                    title = "المظهر",
                    subtitle = when (themeMode) {
                        "dark" -> "الوضع الداكن"
                        "light" -> "الوضع الفاتح"
                        else -> "تلقائي حسب النظام"
                    },
                    icon = Icons.Default.Nightlight,
                    onClick = {
                        val next = when (themeMode) {
                            "system" -> "light"
                            "light" -> "dark"
                            else -> "system"
                        }
                        onUpdateThemeMode(next)
                    }
                )

                SettingsItemRow(
                    title = "اللغة",
                    subtitle = "العربية (الافتراضية)",
                    icon = Icons.Default.Language,
                    onClick = { /* Arabic default */ }
                )

                SettingsItemRow(
                    title = "حول تطبيق ختمتي",
                    subtitle = "الإصدار ١.٠.٠ • رفيقك القرآني اليومي",
                    icon = Icons.Default.Info,
                    onClick = { showAboutDialog = true }
                )
            }
        }
    }

    // Font Size Adjustment Dialog
    if (showFontSizeDialog) {
        AlertDialog(
            onDismissRequest = { showFontSizeDialog = false },
            title = {
                Text(
                    text = "حجم خط المصحف",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = currentFontSize.sp,
                            lineHeight = (currentFontSize * 1.8f).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    Slider(
                        value = currentFontSize,
                        onValueChange = { currentFontSize = it },
                        valueRange = 18f..36f,
                        colors = SliderDefaults.colors(
                            thumbColor = EmeraldPrimary,
                            activeTrackColor = EmeraldPrimary
                        )
                    )
                    Text(
                        text = "${currentFontSize.toInt()} نقطة",
                        style = MaterialTheme.typography.labelMedium,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onUpdateFontSize(currentFontSize)
                        showFontSizeDialog = false
                    }
                ) {
                    Text("حفظ", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFontSizeDialog = false }) {
                    Text("إلغاء", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Text(
                    text = "ختمتي — Khatmati",
                    style = MaterialTheme.typography.titleLarge,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "تطبيق ختمتي رفيق المسلم اليومي لقراءة القرآن الكريم ومتابعة الختمات والأذكار ومواقيت الصلاة وفق معايير وتصميم دقيق يعين على الطاعة والاستمرار.",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "الإصدار: ١.٠.٠ (المرحلة الأولى: الأساس البصري والواجهات)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("إغلاق", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
    )
}

@Composable
private fun SettingsItemRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("settings_item_$title"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

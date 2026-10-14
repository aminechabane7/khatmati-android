package com.example.feature.khatmah

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.util.DateTimeHelper
import com.example.core.designsystem.component.KhatmatiBadge
import com.example.core.designsystem.component.KhatmatiButton
import com.example.core.designsystem.component.KhatmatiCard
import com.example.core.designsystem.component.KhatmatiTopBar
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldOnContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldOnContainer
import com.example.ui.theme.WarmBorder

@Composable
fun CreateKhatmahScreen(
    onSavePlan: (String, Int, Boolean) -> Unit, // title, days, divideByPrayers
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackClick() }

    val presetDays = listOf(7, 15, 30, 60, 90)
    var selectedDays by remember { mutableIntStateOf(30) }
    var isCustomDays by remember { mutableStateOf(false) }
    var customDaysSlider by remember { mutableStateOf(45f) }
    var divideByPrayers by remember { mutableStateOf(true) }

    val effectiveDays = if (isCustomDays) customDaysSlider.toInt() else selectedDays
    val totalPages = 604
    val baseDailyTarget = (totalPages + effectiveDays - 1) / effectiveDays
    val perPrayerPages = (baseDailyTarget + 4) / 5

    Surface(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            KhatmatiTopBar(
                title = "ابدأ ختمة جديدة",
                subtitle = "حدد المدة وسنقسم وردك تلقائياً",
                onBackClick = onBackClick
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // 1. Duration Selection Header
                item {
                    Text(
                        text = "اختر مدة الختمة",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 2. Preset Options
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        presetDays.forEach { days ->
                            val isSelected = !isCustomDays && selectedDays == days
                            val approxPages = (totalPages + days - 1) / days

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        isCustomDays = false
                                        selectedDays = days
                                    }
                                    .testTag("preset_days_$days"),
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) EmeraldContainer else MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline
                                )
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
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "ختمة ${DateTimeHelper.toArabicDigits(days)} يوماً",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }

                                    KhatmatiBadge(
                                        text = "~${DateTimeHelper.toArabicDigits(approxPages)} صفحة/يوم",
                                        containerColor = if (isSelected) EmeraldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                        textColor = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Custom Duration Option
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { isCustomDays = true }
                                .testTag("custom_days_btn"),
                            shape = RoundedCornerShape(16.dp),
                            color = if (isCustomDays) EmeraldContainer else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isCustomDays) 1.5.dp else 1.dp,
                                color = if (isCustomDays) EmeraldPrimary else MaterialTheme.colorScheme.outline
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(if (isCustomDays) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isCustomDays) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "مدة مخصصة",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontWeight = if (isCustomDays) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }

                                    if (isCustomDays) {
                                        KhatmatiBadge(
                                            text = "${DateTimeHelper.toArabicDigits(effectiveDays)} يوماً",
                                            containerColor = EmeraldPrimary.copy(alpha = 0.15f),
                                            textColor = EmeraldPrimary
                                        )
                                    }
                                }

                                if (isCustomDays) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Slider(
                                        value = customDaysSlider,
                                        onValueChange = { customDaysSlider = it },
                                        valueRange = 5f..120f,
                                        steps = 114,
                                        colors = SliderDefaults.colors(
                                            thumbColor = EmeraldPrimary,
                                            activeTrackColor = EmeraldPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Plan Calculation Summary Card
                item {
                    KhatmatiCard(
                        backgroundColor = GoldContainer,
                        borderColor = GoldAccent.copy(alpha = 0.4f)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "ملخص الخطة المقترحة",
                                style = MaterialTheme.typography.titleMedium,
                                color = GoldOnContainer,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "المستهدف اليومي",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GoldOnContainer.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = "${DateTimeHelper.toArabicDigits(baseDailyTarget)} صفحة/يوم",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = GoldOnContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "إجمالي الصفحات",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GoldOnContainer.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = "٦٠٤ صفحة (مصحف المدينة)",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = GoldOnContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // 4. Divide by Prayers Toggle
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "تقسيم الورد على الصلوات الخمس",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "حوالي ${DateTimeHelper.toArabicDigits(perPrayerPages)} صفحات دبر كل صلاة لتيسير الإنجاز",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = divideByPrayers,
                                onCheckedChange = { divideByPrayers = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = EmeraldPrimary
                                )
                            )
                        }
                    }
                }
            }

            // Bottom CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                KhatmatiButton(
                    text = "حفظ وبدء الختمة",
                    onClick = {
                        val planTitle = "ختمة $effectiveDays يوماً"
                        onSavePlan(planTitle, effectiveDays, divideByPrayers)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "save_khatmah_plan_btn"
                )
            }
        }
    }
}

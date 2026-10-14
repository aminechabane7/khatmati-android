package com.example.feature.khatmah

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.core.designsystem.component.KhatmatiOutlinedButton
import com.example.core.designsystem.component.KhatmatiProgressBar
import com.example.data.model.KhatmahPlan
import com.example.data.model.PrayerWirdProgress
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldOnContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldOnContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarmBorder

@Composable
fun KhatmatiScreen(
    plan: KhatmahPlan,
    onContinueWirdClick: (Int) -> Unit, // surahNumber
    onPrayerToggle: (String, Boolean) -> Unit, // prayerName, isCompleted
    onTogglePrayerDivision: (Boolean) -> Unit,
    onCreateNewKhatmahClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Screen Title Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ختمتي",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "متابعة خطة الختمة والورد اليومي",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldContainer)
                        .clickable { onCreateNewKhatmahClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("create_new_khatmah_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ختمة جديدة",
                            style = MaterialTheme.typography.labelMedium,
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2. Current Khatmah Overview Card
        item {
            KhatmatiCard(
                backgroundColor = MaterialTheme.colorScheme.surface,
                borderColor = MaterialTheme.colorScheme.outline
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ختمتي الحالية",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = plan.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        KhatmatiBadge(
                            text = "${DateTimeHelper.toArabicDigits(plan.progressPercent.toInt())}%",
                            containerColor = EmeraldContainer,
                            textColor = EmeraldPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    KhatmatiProgressBar(
                        progress = plan.progressPercent / 100f,
                        progressColor = EmeraldPrimary,
                        height = 10.dp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(
                            label = "اليوم الحالي",
                            value = "${DateTimeHelper.toArabicDigits(plan.currentDay)} من ${DateTimeHelper.toArabicDigits(plan.totalDays)}"
                        )
                        StatItem(
                            label = "الصفحات المكتملة",
                            value = "${DateTimeHelper.toArabicDigits(plan.completedPages)} من ${DateTimeHelper.toArabicDigits(plan.totalPages)}"
                        )
                        StatItem(
                            label = "المتبقي",
                            value = "${DateTimeHelper.toArabicDigits(plan.remainingPages)} صفحة"
                        )
                    }
                }
            }
        }

        // 3. Today's Wird Card (Core Focus)
        item {
            KhatmatiCard(
                backgroundColor = GoldContainer,
                borderColor = GoldAccent.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(GoldAccent.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint = GoldOnContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "ورد اليوم",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GoldOnContainer,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${DateTimeHelper.toArabicDigits(plan.todayTargetPages)} صفحة",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GoldOnContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Text(
                            text = "من ${DateTimeHelper.toArabicDigits(plan.todayStartPage)} إلى ${DateTimeHelper.toArabicDigits(plan.todayEndPage)}",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldOnContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    KhatmatiProgressBar(
                        progress = plan.todayProgressPercent / 100f,
                        progressColor = GoldAccent,
                        trackColor = Color.White.copy(alpha = 0.6f),
                        height = 8.dp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${DateTimeHelper.toArabicDigits(plan.todayCompletedPages)} من ${DateTimeHelper.toArabicDigits(plan.todayTargetPages)} صفحة مكتملة (${DateTimeHelper.toArabicDigits(plan.todayProgressPercent.toInt())}%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldOnContainer.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary CTA: متابعة الورد
                    KhatmatiButton(
                        text = "متابعة الورد",
                        onClick = { onContinueWirdClick(1) },
                        containerColor = EmeraldPrimary,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "continue_wird_primary_btn"
                    )
                }
            }
        }

        // 4. Prayer-Based Wird Division Section
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "تقسيم الورد على الصلوات",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "توزيع صفحات الورد بعد كل صلاة مكتوبة",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = plan.isPrayerDivisionEnabled,
                        onCheckedChange = { onTogglePrayerDivision(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = EmeraldPrimary
                        )
                    )
                }
            }
        }

        // Prayer Wird Items if enabled
        if (plan.isPrayerDivisionEnabled) {
            items(plan.prayerProgress, key = { it.prayerName }) { prayerWird ->
                PrayerWirdRowItem(
                    prayerWird = prayerWird,
                    onToggle = { isChecked ->
                        onPrayerToggle(prayerWird.prayerName, isChecked)
                    }
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PrayerWirdRowItem(
    prayerWird: PrayerWirdProgress,
    onToggle: (Boolean) -> Unit
) {
    val isCompleted = prayerWird.isCompleted
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onToggle(!isCompleted) },
        shape = RoundedCornerShape(16.dp),
        color = if (isCompleted) EmeraldContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCompleted) EmeraldPrimary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isCompleted) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = prayerWird.prayerName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "صفحة ${DateTimeHelper.toArabicDigits(prayerWird.startPage)} إلى ${DateTimeHelper.toArabicDigits(prayerWird.endPage)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                KhatmatiBadge(
                    text = "${DateTimeHelper.toArabicDigits(prayerWird.pagesCount)} صفحات",
                    containerColor = if (isCompleted) EmeraldContainer else MaterialTheme.colorScheme.surfaceVariant,
                    textColor = if (isCompleted) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(10.dp))
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { onToggle(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = EmeraldPrimary,
                        checkmarkColor = Color.White
                    )
                )
            }
        }
    }
}

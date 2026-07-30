package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ConcreteOption
import com.example.ui.MainViewModel
import com.example.ui.components.ArabicDatePickerDialog
import com.example.util.DateUtils
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcreteCalcScreen(viewModel: MainViewModel) {
    val castingDate by viewModel.castingDate.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val customDaysInput by viewModel.customDaysInput.collectAsState()
    val showSaveDialog by viewModel.showSaveDialog.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }

    val daysOffset = when (selectedOption) {
        ConcreteOption.SEVEN_DAYS -> 7L
        ConcreteOption.TWENTY_EIGHT_DAYS -> 28L
        ConcreteOption.FIFTY_SIX_DAYS -> 56L
        ConcreteOption.CUSTOM -> customDaysInput.toLongOrNull() ?: 14L
    }

    val breakStatus = remember(castingDate, daysOffset) {
        DateUtils.getTestBreakStatus(castingDate, daysOffset)
    }

    val day7Status = remember(castingDate) {
        DateUtils.getTestBreakStatus(castingDate, 7L)
    }

    val day28Status = remember(castingDate) {
        DateUtils.getTestBreakStatus(castingDate, 28L)
    }

    val day56Status = remember(castingDate) {
        DateUtils.getTestBreakStatus(castingDate, 56L)
    }

    if (showDatePicker) {
        ArabicDatePickerDialog(
            initialDate = castingDate,
            onDateSelected = { viewModel.setCastingDate(it) },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showSaveDialog) {
        SaveBatchDialog(
            viewModel = viewModel,
            castingDate = castingDate,
            onDismiss = { viewModel.dismissSaveDialog() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Engineering,
                        contentDescription = "Engineering Icon",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "حاسبة موعد كسر العينات الخرسانية",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "حدد تاريخ صب الخرسانة لاحتساب مواعيد الاختبارات القياسية (7 و 28 يوم)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Section 1: Casting Date Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "تاريخ صب العينة الخرسانية:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    onClick = { showDatePicker = true },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("select_casting_date_btn")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = DateUtils.formatArabicDate(castingDate),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "انقر لتغيير تاريخ الصب",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "Select Date",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Quick Date Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.setCastingDate(LocalDate.now()) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("صب اليوم", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = { viewModel.setCastingDate(LocalDate.now().minusDays(1)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("صب الأمس (-1)", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = { viewModel.setCastingDate(LocalDate.now().minusDays(7)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("منذ أسبوع (-7)", fontSize = 12.sp)
                    }
                }
            }
        }

        // Section 2: Option Selection (7 Days vs 28 Days vs Custom)
        Text(
            text = "اختر خيار الحساب المطلوب:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp)
        )

        ConcreteOptionCard(
            option = ConcreteOption.SEVEN_DAYS,
            isSelected = selectedOption == ConcreteOption.SEVEN_DAYS,
            targetDate = day7Status.targetDate,
            testTag = "option_7_days",
            onSelect = { viewModel.setSelectedOption(ConcreteOption.SEVEN_DAYS) }
        )

        ConcreteOptionCard(
            option = ConcreteOption.TWENTY_EIGHT_DAYS,
            isSelected = selectedOption == ConcreteOption.TWENTY_EIGHT_DAYS,
            targetDate = day28Status.targetDate,
            testTag = "option_28_days",
            onSelect = { viewModel.setSelectedOption(ConcreteOption.TWENTY_EIGHT_DAYS) }
        )

        ConcreteOptionCard(
            option = ConcreteOption.FIFTY_SIX_DAYS,
            isSelected = selectedOption == ConcreteOption.FIFTY_SIX_DAYS,
            targetDate = day56Status.targetDate,
            testTag = "option_56_days",
            onSelect = { viewModel.setSelectedOption(ConcreteOption.FIFTY_SIX_DAYS) }
        )

        ConcreteOptionCard(
            option = ConcreteOption.CUSTOM,
            isSelected = selectedOption == ConcreteOption.CUSTOM,
            targetDate = castingDate.plusDays(customDaysInput.toLongOrNull() ?: 14L),
            testTag = "option_custom_days",
            onSelect = { viewModel.setSelectedOption(ConcreteOption.CUSTOM) },
            customInputContent = {
                AnimatedVisibility(visible = selectedOption == ConcreteOption.CUSTOM) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "عدد الأيام المطلوبة:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = customDaysInput,
                            onValueChange = { viewModel.setCustomDaysInput(it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .width(100.dp)
                                .testTag("custom_days_input"),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Text("يوم")
                    }
                }
            }
        )

        // Section 3: Calculated Result Details Display
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "موعد الاختبار المعتمد:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    StatusBadge(breakStatus = breakStatus)
                }

                Text(
                    text = DateUtils.formatArabicDate(breakStatus.targetDate),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = breakStatus.statusText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = when (breakStatus.statusType) {
                        DateUtils.StatusType.TODAY -> MaterialTheme.colorScheme.error
                        DateUtils.StatusType.UPCOMING -> MaterialTheme.colorScheme.primary
                        DateUtils.StatusType.OVERDUE -> Color(0xFF6B7280)
                    }
                )

                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Expected Strength Preview
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "المقاومة المستهدفة متوقعة:",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = selectedOption.expectedStrength,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    val progressValue = when (selectedOption) {
                        ConcreteOption.SEVEN_DAYS -> 0.67f
                        ConcreteOption.TWENTY_EIGHT_DAYS -> 1.0f
                        ConcreteOption.FIFTY_SIX_DAYS -> 1.0f
                        ConcreteOption.CUSTOM -> ((customDaysInput.toFloatOrNull() ?: 14f) / 28f).coerceIn(0.1f, 1.2f)
                    }
                    LinearProgressIndicator(
                        progress = progressValue.coerceAtMost(1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = { viewModel.openSaveDialog() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_sample_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("حفظ العينة في سجل الخرسانة", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Section 4: Full Overview Summary Table (7-Day & 28-Day Side-by-Side)
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "ملخص المواعيد الخرسانية للوجبة:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 7-day summary box
                    SummaryBox(
                        title = "اختبار 7 أيام",
                        dateText = DateUtils.formatShortDate(day7Status.targetDate),
                        dayName = DateUtils.getArabicDayName(day7Status.targetDate),
                        statusText = day7Status.statusText,
                        modifier = Modifier.weight(1f)
                    )

                    // 28-day summary box
                    SummaryBox(
                        title = "اختبار 28 يوم",
                        dateText = DateUtils.formatShortDate(day28Status.targetDate),
                        dayName = DateUtils.getArabicDayName(day28Status.targetDate),
                        statusText = day28Status.statusText,
                        modifier = Modifier.weight(1f)
                    )

                    // 56-day summary box
                    SummaryBox(
                        title = "اختبار 56 يوم",
                        dateText = DateUtils.formatShortDate(day56Status.targetDate),
                        dayName = DateUtils.getArabicDayName(day56Status.targetDate),
                        statusText = day56Status.statusText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ConcreteOptionCard(
    option: ConcreteOption,
    isSelected: Boolean,
    targetDate: LocalDate,
    testTag: String,
    onSelect: () -> Unit,
    customInputContent: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected)
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = option.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = DateUtils.formatShortDate(targetDate),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            customInputContent?.invoke()
        }
    }
}

@Composable
private fun StatusBadge(breakStatus: DateUtils.TestBreakStatus) {
    val bgColor = when (breakStatus.statusType) {
        DateUtils.StatusType.TODAY -> MaterialTheme.colorScheme.errorContainer
        DateUtils.StatusType.UPCOMING -> MaterialTheme.colorScheme.primaryContainer
        DateUtils.StatusType.OVERDUE -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when (breakStatus.statusType) {
        DateUtils.StatusType.TODAY -> MaterialTheme.colorScheme.onErrorContainer
        DateUtils.StatusType.UPCOMING -> MaterialTheme.colorScheme.onPrimaryContainer
        DateUtils.StatusType.OVERDUE -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = when (breakStatus.statusType) {
                    DateUtils.StatusType.TODAY -> Icons.Default.Info
                    DateUtils.StatusType.UPCOMING -> Icons.Default.Schedule
                    DateUtils.StatusType.OVERDUE -> Icons.Default.CheckCircle
                },
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = when (breakStatus.statusType) {
                    DateUtils.StatusType.TODAY -> "اليوم!"
                    DateUtils.StatusType.UPCOMING -> "قادم"
                    DateUtils.StatusType.OVERDUE -> "منقضي"
                },
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SummaryBox(
    title: String,
    dateText: String,
    dayName: String,
    statusText: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = dateText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = dayName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun SaveBatchDialog(
    viewModel: MainViewModel,
    castingDate: LocalDate,
    onDismiss: () -> Unit
) {
    val projectName by viewModel.projectName.collectAsState()
    val elementName by viewModel.elementName.collectAsState()
    val concreteGrade by viewModel.concreteGrade.collectAsState()
    val cubeCount by viewModel.cubeCount.collectAsState()
    val notes by viewModel.notes.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("تسجيل عينة خرسانية جديدة", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "تاريخ الصب: ${DateUtils.formatArabicDate(castingDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = projectName,
                    onValueChange = { viewModel.setProjectName(it) },
                    label = { Text("اسم المشروع (مثال: برج السلام)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_project_name_input")
                )

                OutlinedTextField(
                    value = elementName,
                    onValueChange = { viewModel.setElementName(it) },
                    label = { Text("اسم العنصر (مثال: سقف الدور الأول / أعمدة)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_element_name_input")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = concreteGrade,
                        onValueChange = { viewModel.setConcreteGrade(it) },
                        label = { Text("رتبة الخرسانة") },
                        placeholder = { Text("C30") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = cubeCount,
                        onValueChange = { viewModel.setCubeCount(it) },
                        label = { Text("عدد المكعبات") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { viewModel.setNotes(it) },
                    label = { Text("ملاحظات إضافية (اختياري)") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.saveBatch() },
                modifier = Modifier.testTag("dialog_save_confirm_btn")
            ) {
                Text("حفظ العينة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

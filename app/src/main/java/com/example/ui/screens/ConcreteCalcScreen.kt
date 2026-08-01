package com.example.ui.screens

import com.example.R
import androidx.compose.ui.res.stringResource

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.ConcreteOption
import com.example.ui.components.OneUIDivider
import com.example.ui.components.OneUIPillButton
import com.example.ui.components.OneUIRow
import com.example.ui.components.OneUISurface
import com.example.ui.theme.OneUIDarkSurfaceVariant
import com.example.ui.theme.OneUIPrimary
import com.example.util.DateUtils
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcreteCalcScreen(viewModel: MainViewModel) {
    val castingDate by viewModel.castingDate.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val projectName by viewModel.projectName.collectAsState()
    
    val targetDate = castingDate?.plusDays(selectedOption.days.toLong())
    
    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDatePicker) {
        val initialSelectedDateMillis = castingDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                        viewModel.setCastingDate(localDate)
                    }
                    showDatePicker = false
                }) { Text(stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        OneUISurface {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                // Title Field
                OutlinedTextField(
                    value = projectName,
                    onValueChange = { viewModel.setProjectName(it) },
                    placeholder = { Text(stringResource(R.string.project_title_hint), color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
                
                OneUIDivider()

                // Date Picker Row
                OneUIRow(
                    icon = Icons.Default.Schedule,
                    text = castingDate?.let { DateUtils.formatArabicDate(it) } ?: stringResource(R.string.select_casting_date),
                    onClick = { showDatePicker = true },
                    trailing = {
                        Switch(
                            checked = castingDate != null, 
                            onCheckedChange = { if(!it) viewModel.setCastingDate(null) else showDatePicker = true },
                            colors = SwitchDefaults.colors(checkedTrackColor = OneUIPrimary)
                        )
                    }
                )

                // Quick Date Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OneUIPillButton(
                        text = stringResource(R.string.today),
                        onClick = { viewModel.setCastingDate(LocalDate.now()) },
                        isSelected = castingDate == LocalDate.now(),
                        modifier = Modifier.weight(1f)
                    )
                    OneUIPillButton(
                        text = stringResource(R.string.yesterday),
                        onClick = { viewModel.setCastingDate(LocalDate.now().minusDays(1)) },
                        isSelected = castingDate == LocalDate.now().minusDays(1),
                        modifier = Modifier.weight(1f)
                    )
                }

                OneUIDivider()
                
                // Test Options
                OneUIRow(text = stringResource(R.string.test_period))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ConcreteOption.values().forEach { option ->
                        OneUIPillButton(
                            text = stringResource(option.labelResId),
                            onClick = { viewModel.setSelectedOption(option) },
                            isSelected = selectedOption == option,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                if (targetDate != null) {
                    OneUIDivider()
                    // Result
                    OneUIRow(
                        icon = Icons.Default.CalendarToday,
                        text = stringResource(R.string.breaking_date),
                        subText = DateUtils.formatArabicDate(targetDate)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(16.dp))

        // Save / Cancel Bottom Action
        if (targetDate != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .background(OneUIDarkSurfaceVariant, RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextButton(
                        onClick = { viewModel.setCastingDate(null); viewModel.setProjectName("") }
                    ) {
                        Text(stringResource(R.string.cancel), color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    }
                    
                    TextButton(
                        onClick = { viewModel.saveBatch() }
                    ) {
                        Text(stringResource(R.string.save), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

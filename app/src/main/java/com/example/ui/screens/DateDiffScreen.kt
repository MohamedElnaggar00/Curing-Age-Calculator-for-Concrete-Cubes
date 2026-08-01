package com.example.ui.screens

import com.example.R
import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.MainViewModel
import com.example.ui.components.OneUIDivider
import com.example.ui.components.OneUIPillButton
import com.example.ui.components.OneUIRow
import com.example.ui.components.OneUISurface
import com.example.util.DateUtils
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDiffScreen(viewModel: MainViewModel) {
    val startDate by viewModel.diffStartDate.collectAsState()
    val endDate by viewModel.diffEndDate.collectAsState()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val daysDiff = ChronoUnit.DAYS.between(startDate, endDate).absoluteValue

    if (showStartDatePicker) {
        val initialSelectedDateMillis = startDate.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                        viewModel.setDiffStartDate(localDate)
                    }
                    showStartDatePicker = false
                }) { Text(stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text(stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEndDatePicker) {
        val initialSelectedDateMillis = endDate.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                        viewModel.setDiffEndDate(localDate)
                    }
                    showEndDatePicker = false
                }) { Text(stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text(stringResource(R.string.cancel)) }
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
                OneUIRow(
                    icon = Icons.Default.Event,
                    text = DateUtils.formatArabicDate(startDate),
                    onClick = { showStartDatePicker = true }
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OneUIPillButton(
                        text = stringResource(R.string.today),
                        onClick = { viewModel.setDiffStartDate(LocalDate.now()) },
                        isSelected = startDate == LocalDate.now(),
                        modifier = Modifier.weight(1f)
                    )
                }

                OneUIDivider()

                OneUIRow(
                    icon = Icons.Default.Event,
                    text = DateUtils.formatArabicDate(endDate),
                    onClick = { showEndDatePicker = true }
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OneUIPillButton(
                        text = stringResource(R.string.today),
                        onClick = { viewModel.setDiffEndDate(LocalDate.now()) },
                        isSelected = endDate == LocalDate.now(),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                OneUIDivider()
                OneUIRow(
                    icon = Icons.Default.DateRange,
                    text = stringResource(R.string.date_diff_title),
                    subText = stringResource(R.string.diff_result, daysDiff)
                )
            }
        }
    }
}

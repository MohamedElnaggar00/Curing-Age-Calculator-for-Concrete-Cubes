package com.example.ui.screens

import com.example.R
import androidx.compose.ui.res.stringResource

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ConcreteBatch
import com.example.ui.MainViewModel
import com.example.ui.components.OneUIDivider
import com.example.ui.components.OneUISurface
import com.example.ui.theme.OneUIDarkSurface
import com.example.ui.theme.OneUIDarkSurfaceVariant
import com.example.ui.theme.OneUIError
import com.example.ui.theme.OneUITextPrimary
import com.example.util.DateUtils
import java.time.LocalDate

@Composable
fun SavedBatchesScreen(viewModel: MainViewModel) {
    val batches by viewModel.batches.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filterStatus by viewModel.filterStatus.collectAsState()
    
    val context = LocalContext.current
    var batchToDelete by remember { mutableStateOf<ConcreteBatch?>(null) }

    if (batchToDelete != null) {
        AlertDialog(
            onDismissRequest = { batchToDelete = null },
            title = { Text(stringResource(R.string.delete_batch_title), color = OneUITextPrimary) },
            text = { Text(stringResource(R.string.delete_batch_msg), color = OneUITextPrimary) },
            confirmButton = {
                TextButton(onClick = {
                    batchToDelete?.let { viewModel.deleteBatch(it) }
                    batchToDelete = null
                }) {
                    Text(stringResource(R.string.delete), color = OneUIError)
                }
            },
            dismissButton = {
                TextButton(onClick = { batchToDelete = null }) {
                    Text(stringResource(R.string.cancel), color = OneUITextPrimary)
                }
            },
            containerColor = OneUIDarkSurface,
            textContentColor = OneUITextPrimary
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.search_hint), color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                    }
                }
            },
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = OneUIDarkSurfaceVariant,
                unfocusedContainerColor = OneUIDarkSurfaceVariant,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Grid of filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterCard(
                title = stringResource(R.string.filter_all),
                count = batches.size, // Approximation for UI based on current view
                isSelected = filterStatus == "ALL",
                onClick = { viewModel.setFilterStatus("ALL") },
                modifier = Modifier.weight(1f)
            )
            FilterCard(
                title = stringResource(R.string.filter_upcoming),
                count = batches.count { LocalDate.ofEpochDay(it.castingDateEpochDay).plusDays(28).isAfter(LocalDate.now()) },
                isSelected = filterStatus == "UPCOMING",
                onClick = { viewModel.setFilterStatus("UPCOMING") },
                modifier = Modifier.weight(1f)
            )
            FilterCard(
                title = stringResource(R.string.filter_completed),
                count = batches.count { LocalDate.ofEpochDay(it.castingDateEpochDay).plusDays(28).isBefore(LocalDate.now()) },
                isSelected = filterStatus == "COMPLETED",
                onClick = { viewModel.setFilterStatus("COMPLETED") },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.all_batches),
            color = OneUITextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        if (batches.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.no_batches),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(batches, key = { it.id }) { batch ->
                    BatchItem(
                        batch = batch,
                        onShareClick = { shareBatch(context, batch) },
                        onDeleteClick = { batchToDelete = batch }
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun FilterCard(
    title: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else OneUIDarkSurface)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = count.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else OneUITextPrimary
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}

@Composable
fun BatchItem(
    batch: ConcreteBatch,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(OneUIDarkSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = batch.projectName.ifBlank { stringResource(R.string.unnamed_batch) },
                    color = OneUITextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = onShareClick, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = OneUIError, modifier = Modifier.size(20.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = stringResource(R.string.casting_date_prefix, DateUtils.formatArabicDate(LocalDate.ofEpochDay(batch.castingDateEpochDay))),
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            Text(
                text = stringResource(R.string.testing_date_prefix, DateUtils.formatArabicDate(LocalDate.ofEpochDay(batch.castingDateEpochDay).plusDays(28)), stringResource(R.string.days_28)),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

private fun shareBatch(context: Context, batch: ConcreteBatch) {
    val castingLocalDate = LocalDate.ofEpochDay(batch.castingDateEpochDay)
    val targetLocalDate = castingLocalDate.plusDays(28)
    
    val text = buildString {
        appendLine(context.getString(R.string.share_title))
        if (batch.projectName.isNotBlank()) appendLine(context.getString(R.string.share_project, batch.projectName))
        appendLine(context.getString(R.string.share_casting, DateUtils.formatArabicDate(castingLocalDate)))
        appendLine(context.getString(R.string.share_breaking, DateUtils.formatArabicDate(targetLocalDate)))
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_chooser)))
}

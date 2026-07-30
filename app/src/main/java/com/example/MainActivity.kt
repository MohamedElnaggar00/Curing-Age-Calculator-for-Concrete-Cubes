package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.ui.MainViewModel
import com.example.ui.screens.ConcreteCalcScreen
import com.example.ui.screens.DateDiffScreen
import com.example.ui.screens.SavedBatchesScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                // Force Right-to-Left Layout Direction for Arabic UX
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val selectedTab by viewModel.selectedTab.collectAsState()
                    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
                    val snackbarHostState = remember { SnackbarHostState() }

                    LaunchedEffect(snackbarMessage) {
                        snackbarMessage?.let { msg ->
                            snackbarHostState.showSnackbar(msg)
                            viewModel.clearSnackbarMessage()
                        }
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        text = when (selectedTab) {
                                            0 -> "حاسبة صب العينات الخرسانية"
                                            1 -> "حاسبة الفرق بين تاريخين"
                                            2 -> "سجل العينات المحفوظة"
                                            else -> "حاسبة العينات الخرسانية"
                                        },
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                navigationIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Engineering,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                )
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 3.dp
                            ) {
                                NavigationBarItem(
                                    selected = selectedTab == 0,
                                    onClick = { viewModel.setSelectedTab(0) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selectedTab == 0) Icons.Default.CalendarMonth else Icons.Outlined.CalendarMonth,
                                            contentDescription = "Concrete Calc"
                                        )
                                    },
                                    label = { Text("صب الخرسانة", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                                    modifier = Modifier.testTag("tab_concrete_calc")
                                )

                                NavigationBarItem(
                                    selected = selectedTab == 1,
                                    onClick = { viewModel.setSelectedTab(1) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selectedTab == 1) Icons.Default.DateRange else Icons.Outlined.DateRange,
                                            contentDescription = "Date Diff"
                                        )
                                    },
                                    label = { Text("الفرق بين تاريخين", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                                    modifier = Modifier.testTag("tab_date_diff")
                                )

                                NavigationBarItem(
                                    selected = selectedTab == 2,
                                    onClick = { viewModel.setSelectedTab(2) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selectedTab == 2) Icons.Default.ListAlt else Icons.Outlined.ListAlt,
                                            contentDescription = "Saved Batches"
                                        )
                                    },
                                    label = { Text("سجل العينات", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
                                    modifier = Modifier.testTag("tab_saved_batches")
                                )
                            }
                        },
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            AnimatedContent(
                                targetState = selectedTab,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "TabTransition"
                            ) { tab ->
                                when (tab) {
                                    0 -> ConcreteCalcScreen(viewModel = viewModel)
                                    1 -> DateDiffScreen(viewModel = viewModel)
                                    2 -> SavedBatchesScreen(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

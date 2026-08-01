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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.screens.ConcreteCalcScreen
import com.example.ui.screens.DateDiffScreen
import com.example.ui.screens.SavedBatchesScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
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
                        modifier = Modifier.fillMaxSize().background(Color.Black),
                        containerColor = Color.Black,
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Top Segmented Control (One UI style)
                            TopSegmentedBar(
                                selectedTab = selectedTab,
                                onTabSelected = { viewModel.setSelectedTab(it) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(modifier = Modifier.weight(1f)) {
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
}

@Composable
fun TopSegmentedBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF252525))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            val tabs = listOf("الصب", "الفروقات", "السجل")
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color(0xFF3A3A3C) else Color.Transparent)
                        .clickable { onTabSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else Color(0xFF8E8E93),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

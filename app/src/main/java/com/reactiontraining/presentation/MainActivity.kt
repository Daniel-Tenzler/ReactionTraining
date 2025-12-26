package com.reactiontraining.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.reactiontraining.presentation.ui.MainScreen
import com.reactiontraining.presentation.ui.StatsCard
import com.reactiontraining.presentation.viewmodel.MainViewModel
import com.reactiontraining.ui.theme.ReactionTrainingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ReactionTrainingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReactionTrainingApp()
                }
            }
        }
    }
}

@Composable
fun ReactionTrainingApp() {
    val viewModel: MainViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val stats by viewModel.stats.collectAsState(initial = null)
    var statsExpanded by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        MainScreen(
            uiState = uiState,
            onScreenTap = { viewModel.onScreenTap() }
        )
        
        // Stats overlay at the bottom
        StatsCard(
            stats = stats,
            expanded = statsExpanded,
            onToggleExpanded = { statsExpanded = !statsExpanded },
            onResetStats = { viewModel.resetStats() },
            modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
        )
    }
}
package com.reactiontraining.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reactiontraining.domain.model.ReactionStats
import com.reactiontraining.domain.repository.ReactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import javax.inject.Inject

enum class GameState { IDLE, WAITING, READY, FINISHED }

data class GameUiState(
    val gameState: GameState = GameState.IDLE,
    val currentReactionTime: Long? = null,
    val backgroundColor: Color = Color.hsv(230.8108f, 1.0f, 0.43529412f),
    val showMessage: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ReactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _stats = MutableStateFlow<ReactionStats?>(null)
    val stats: StateFlow<ReactionStats?> = _stats.asStateFlow()

    private var timerStartTime: Long = 0
    private var timerJob: Job? = null
    private var readyTime: Long = 0

    init {
        startGame()
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _stats.value = repository.getStats()
        }
    }

    fun refreshStats() {
        loadStats()
    }

    fun startGame() {
        cancelCurrentTimer()
        _uiState.value = GameUiState(
            gameState = GameState.WAITING,
            backgroundColor = Color.hsv(230.8108f, 1.0f, 0.43529412f),
            showMessage = null
        )

        val randomDelay = Random.nextLong(2000, 6001) // 2-6 seconds
        
        timerJob = viewModelScope.launch {
            try {
                delay(randomDelay)
                readyTime = System.currentTimeMillis()
                _uiState.value = GameUiState(
                    gameState = GameState.READY,
                    backgroundColor = Color.hsv(102.352936f, 1.0f, 0.33333334f),
                    showMessage = null
                )
            } catch (e: CancellationException) {
                // Timer was cancelled, this is expected
            }
        }
    }

    fun onScreenTap() {
        when (_uiState.value.gameState) {
            GameState.IDLE -> {
                startGame()
            }
            GameState.WAITING -> {
                // Too early!
                cancelCurrentTimer()
                _uiState.value = GameUiState(
                    gameState = GameState.IDLE,
                    backgroundColor = Color.hsv(230.8108f, 1.0f, 0.43529412f),
                    showMessage = null
                )
            }
            GameState.READY -> {
                // Calculate reaction time
                val reactionTime = System.currentTimeMillis() - readyTime
                
                viewModelScope.launch {
                    repository.saveReactionTime(reactionTime)
                    // Refresh stats after saving
                    _stats.value = repository.getStats()
                }

                _uiState.value = GameUiState(
                    gameState = GameState.FINISHED,
                    backgroundColor = Color.hsv(102.352936f, 1.0f, 0.33333334f),
                    currentReactionTime = reactionTime,
                    showMessage = null
                )

                // Auto-restart after 2 seconds
                timerJob = viewModelScope.launch {
                    delay(2000)
                    startGame()
                }
            }
            GameState.FINISHED -> {
                // Tap to restart immediately
                startGame()
            }
        }
    }

    fun resetStats() {
        viewModelScope.launch {
            repository.clearAllData()
            loadStats()
        }
    }

    private fun cancelCurrentTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        cancelCurrentTimer()
    }
}
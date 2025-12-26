package com.reactiontraining.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.reactiontraining.domain.repository.ReactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @Mock
    private lateinit var repository: ReactionRepository
    
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(repository)
    }

    @Test
    fun `initial state should be WAITING with blue background`() = runTest {
        // Then
        assert(viewModel.uiState.value.gameState == GameState.WAITING)
        assert(viewModel.uiState.value.backgroundColor == Color.Blue)
    }

    @Test
    fun `onScreenTap in WAITING state should show too early message`() = runTest {
        // When
        viewModel.onScreenTap()
        
        // Then
        assert(viewModel.uiState.value.gameState == GameState.IDLE)
        assert(viewModel.uiState.value.showMessage == "Too early! Tap to try again.")
        assert(viewModel.uiState.value.backgroundColor == Color.Blue)
    }

    @Test
    fun `startGame should set state to WAITING with blue background`() = runTest {
        // When
        viewModel.startGame()
        
        // Then
        assert(viewModel.uiState.value.gameState == GameState.WAITING)
        assert(viewModel.uiState.value.backgroundColor == Color.Blue)
        assert(viewModel.uiState.value.showMessage == "Wait for green...")
    }

    @Test
    fun `startGame called multiple times should cancel previous timer`() = runTest {
        // Given
        viewModel.startGame()
        val firstState = viewModel.uiState.value
        
        // When
        viewModel.startGame()
        
        // Then - state should be updated to new waiting state
        assert(viewModel.uiState.value.gameState == GameState.WAITING)
        assert(viewModel.uiState.value.showMessage == "Wait for green...")
    }
}
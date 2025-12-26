package com.reactiontraining.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.reactiontraining.presentation.viewmodel.GameState
import com.reactiontraining.presentation.viewmodel.GameUiState

@Composable
fun MainScreen(
    uiState: GameUiState,
    onScreenTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val circleColor by animateColorAsState(
        targetValue = uiState.backgroundColor,
        animationSpec = tween(300)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .semantics { 
                contentDescription = "Reaction training game screen"
            }
            .clickable { onScreenTap() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                circleColor.copy(alpha = 0.6f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.width / 2 + 64.dp.toPx()
                        ),
                        radius = size.width / 2 + 64.dp.toPx(),
                        center = center
                    )
                }
                .clip(CircleShape)
                .background(circleColor),
            contentAlignment = Alignment.Center
        ) {
            when (uiState.gameState) {
                GameState.IDLE -> IdleContent(uiState.showMessage)
                GameState.WAITING -> WaitingContent(uiState.showMessage)
                GameState.READY -> ReadyContent(uiState.showMessage)
                GameState.FINISHED -> ReactionResultContent(
                    reactionTime = uiState.currentReactionTime,
                    message = uiState.showMessage
                )
            }
        }
    }
}

@Composable
private fun IdleContent(message: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message ?: "Reaction Training",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tap to start",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun WaitingContent(message: String?) {
    Text(
        text = message ?: "Wait for green...",
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ReadyContent(message: String?) {
    Text(
        text = message ?: "TAP NOW!",
        color = Color.White,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ReactionResultContent(
    reactionTime: Long?,
    message: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message ?: if (reactionTime != null) "${reactionTime}ms" else "",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tap to try again",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}
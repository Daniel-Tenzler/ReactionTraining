package com.reactiontraining.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardColors
import androidx.compose.ui.graphics.Color

import com.reactiontraining.domain.model.ReactionStats

@Composable
fun StatsCard(
    modifier: Modifier = Modifier,
    stats: ReactionStats?,
    expanded: Boolean = false,
    onToggleExpanded: () -> Unit = {},
    onResetStats: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .clickable { onToggleExpanded() }
            .background(
                color = Color.DarkGray,
                shape = RoundedCornerShape(32.dp)
            ),
        colors = CardColors(
            Color.hsv(0.0f, 0.0f, 0.13333334f),
            Color.White,
            Color.Black,
            Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (expanded) {
                        Button(
                            onClick = onResetStats,
                            modifier = Modifier.height(32.dp),
                            colors = ButtonColors(Color.hsv(230.8108f, 1.0f, 0.43529412f), Color.White, Color.DarkGray, Color.DarkGray)
                        ) {
                            Text(
                                text = "Reset",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse statistics" else "Expand statistics"
                    )
                }
            }
            
            if (expanded) {
                if (stats != null && stats.averageTime >= 0 && stats.bestTime >= 0 && stats.worstTime >= 0 && stats.totalAttempts >= 0) {
                    StatRow("Average", "${stats.averageTime.toInt()}ms")
                    StatRow("Best", "${stats.bestTime}ms")
                    StatRow("Worst", "${stats.worstTime}ms")
                    StatRow("Attempts", "${stats.totalAttempts}")
                } else {
                    Text(
                        text = "No data yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}
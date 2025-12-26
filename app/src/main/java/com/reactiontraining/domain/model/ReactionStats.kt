package com.reactiontraining.domain.model

data class ReactionStats(
    val averageTime: Float,
    val bestTime: Long,
    val worstTime: Long,
    val totalAttempts: Int
)
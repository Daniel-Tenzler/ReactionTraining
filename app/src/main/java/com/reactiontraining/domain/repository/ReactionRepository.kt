package com.reactiontraining.domain.repository

import com.reactiontraining.domain.model.ReactionStats
import com.reactiontraining.domain.model.ReactionTime
import kotlinx.coroutines.flow.Flow

interface ReactionRepository {
    suspend fun saveReactionTime(reactionTimeMs: Long)
    fun getAllReactionTimes(): Flow<List<ReactionTime>>
    suspend fun getStats(): ReactionStats?
    suspend fun clearAllData()
}
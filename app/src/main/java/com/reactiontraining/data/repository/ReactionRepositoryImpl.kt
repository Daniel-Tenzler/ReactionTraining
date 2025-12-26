package com.reactiontraining.data.repository

import com.reactiontraining.data.local.dao.ReactionTimeDao
import com.reactiontraining.data.local.entity.ReactionTimeEntity
import com.reactiontraining.domain.model.ReactionStats
import com.reactiontraining.domain.model.ReactionTime
import com.reactiontraining.domain.repository.ReactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReactionRepositoryImpl @Inject constructor(
    private val reactionTimeDao: ReactionTimeDao
) : ReactionRepository {

    override suspend fun saveReactionTime(reactionTimeMs: Long) {
        try {
            val entity = ReactionTimeEntity(reactionTimeMs = reactionTimeMs)
            reactionTimeDao.insertReactionTime(entity)
        } catch (e: Exception) {
            // Handle database error - could log to analytics or retry
            throw e // Re-throw for now to maintain current behavior
        }
    }

    override fun getAllReactionTimes(): Flow<List<ReactionTime>> {
        return reactionTimeDao.getAllReactionTimes().map { entities ->
            entities.map { entity ->
                ReactionTime(
                    id = entity.id,
                    timestamp = entity.timestamp,
                    reactionTimeMs = entity.reactionTimeMs
                )
            }
        }
    }

    override suspend fun getStats(): ReactionStats? {
        val totalAttempts = reactionTimeDao.getTotalAttempts()
        if (totalAttempts == 0) return null

        val averageTime = reactionTimeDao.getAverageReactionTime() ?: 0f
        val bestTime = reactionTimeDao.getBestReactionTime() ?: 0L
        val worstTime = reactionTimeDao.getWorstReactionTime() ?: 0L

        return ReactionStats(
            averageTime = averageTime,
            bestTime = bestTime,
            worstTime = worstTime,
            totalAttempts = totalAttempts
        )
    }

    override suspend fun clearAllData() {
        reactionTimeDao.clearAllReactionTimes()
    }
}
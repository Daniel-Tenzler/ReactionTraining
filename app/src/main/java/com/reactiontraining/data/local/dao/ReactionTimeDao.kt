package com.reactiontraining.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.reactiontraining.data.local.entity.ReactionTimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReactionTimeDao {
    @Insert
    suspend fun insertReactionTime(reactionTime: ReactionTimeEntity)
    
    @Query("SELECT * FROM reaction_times ORDER BY timestamp DESC")
    fun getAllReactionTimes(): Flow<List<ReactionTimeEntity>>
    
    @Query("SELECT AVG(reactionTimeMs) FROM reaction_times")
    suspend fun getAverageReactionTime(): Float?
    
    @Query("SELECT MIN(reactionTimeMs) FROM reaction_times")
    suspend fun getBestReactionTime(): Long?
    
    @Query("SELECT MAX(reactionTimeMs) FROM reaction_times")
    suspend fun getWorstReactionTime(): Long?
    
    @Query("SELECT COUNT(*) FROM reaction_times")
    suspend fun getTotalAttempts(): Int
    
    @Query("DELETE FROM reaction_times")
    suspend fun clearAllReactionTimes()
}
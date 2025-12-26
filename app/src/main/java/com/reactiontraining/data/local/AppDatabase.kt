package com.reactiontraining.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reactiontraining.data.local.dao.ReactionTimeDao
import com.reactiontraining.data.local.entity.ReactionTimeEntity

@Database(entities = [ReactionTimeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reactionTimeDao(): ReactionTimeDao
}
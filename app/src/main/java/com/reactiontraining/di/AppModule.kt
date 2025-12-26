package com.reactiontraining.di

import android.content.Context
import androidx.room.Room
import com.reactiontraining.data.local.AppDatabase
import com.reactiontraining.data.local.dao.ReactionTimeDao
import com.reactiontraining.data.repository.ReactionRepositoryImpl
import com.reactiontraining.domain.repository.ReactionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "reaction_database"
        ).build()
    }

    @Provides
    fun provideReactionTimeDao(database: AppDatabase): ReactionTimeDao {
        return database.reactionTimeDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindReactionRepository(
        reactionRepositoryImpl: ReactionRepositoryImpl
    ): ReactionRepository
}
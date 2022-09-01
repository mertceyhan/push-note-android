package com.penguenlabs.pushnote.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.penguenlabs.pushnote.data.local.DATABASE_NAME
import com.penguenlabs.pushnote.data.local.HistoryDatabase
import com.penguenlabs.pushnote.data.local.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val SHARED_PREFERENCES = "PUSH_NOTE_SHARED_PREFERENCES"

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideHistoryDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        HistoryDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(MIGRATION_1_2).build()

    @Singleton
    @Provides
    fun provideHistoryDao(historyDatabase: HistoryDatabase) = historyDatabase.historyDao()
}
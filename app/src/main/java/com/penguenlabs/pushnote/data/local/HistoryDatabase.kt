package com.penguenlabs.pushnote.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.penguenlabs.pushnote.data.local.dao.HistoryDao
import com.penguenlabs.pushnote.data.local.entity.HistoryEntity

const val DATABASE_NAME = "history"
private const val DATABASE_VERSION = 2

@Suppress("unused")
@Database(entities = [HistoryEntity::class], version = DATABASE_VERSION, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE HistoryEntity ADD COLUMN is_pinned_note INTEGER DEFAULT 0 NOT NULL")
    }
}
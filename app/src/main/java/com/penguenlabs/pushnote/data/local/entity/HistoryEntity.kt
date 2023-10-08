package com.penguenlabs.pushnote.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val note: String,
    val time: Long,
    @ColumnInfo(name = "is_pinned_note") val isPinnedNote: Boolean,
    val active: Boolean = true
)
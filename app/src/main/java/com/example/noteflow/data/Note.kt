package com.example.noteflow.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long,
    val voicePath: String? = null,
    val tags: List<String> = emptyList(),
    val audioUri: String? = null,
    val isSummarizing: Boolean = false,
    val isPendingSummary: Boolean = false

)

package com.example.noteflow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Note::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NoteDatabse:RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
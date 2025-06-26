package com.example.noteflow.repository

import com.example.noteflow.data.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(note: Note): Long
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)
    fun getAllNotes():Flow<List<Note>>
    suspend fun getNoteById(id:Int): Note?
}
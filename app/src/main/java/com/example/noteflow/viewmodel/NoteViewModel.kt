package com.example.noteflow.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteflow.data.Note
import com.example.noteflow.data.remote.AiService
import com.example.noteflow.repository.NoteRepository
import com.example.noteflow.util.StorageHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    enum class SortType { LATEST, OLDEST, ALPHABETICAL }

    // Search + sort state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortType = MutableStateFlow(SortType.LATEST)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()

    // Combined notes flow
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getAllNotes(),
                _searchQuery,
                _sortType
            ) { all, query, sort ->
                all
                    .filter {
                        it.title.contains(query, ignoreCase = true)
                                || it.content.contains(query, ignoreCase = true)
                    }
                    .let { filtered ->
                        when (sort) {
                            SortType.LATEST       -> filtered.sortedByDescending { it.timestamp }
                            SortType.OLDEST       -> filtered.sortedBy { it.timestamp }
                            SortType.ALPHABETICAL -> filtered.sortedBy { it.title }
                        }
                    }
            }.collect { merged ->
                _notes.value = merged
            }
        }
    }

    /** Change search text */
    fun setSearchQuery(q: String) { _searchQuery.value = q }

    /** Change sort mode */
    fun setSortType(t: SortType) { _sortType.value = t }

    /** Basic inserts / updates / deletes */
    fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }
    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }
    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }

    /** Fetch a single note for edit/detail */
    suspend fun getNoteById(id: Int): Note? =
        repository.getNoteById(id)

    // ─── NEW! Insert & return its row ID ─────────────────────────────────
    /**
     * Inserts the note and returns the generated row ID.
     * Must be called from a coroutine.
     */
    suspend fun insertNoteReturningId(note: Note): Long =
        repository.insertNote(note)

    // ─── Voice Note + Background AI Summarization ──────────────────────────
    /**
     * 1) Inserts a bare placeholder row (gets its new ID).
     * 2) Uploads to Storage.
     * 3) Calls AI summarizer.
     * 4) Updates that same row with real title/content/tags.
     */
    fun insertVoiceNoteAndSummarize(uri: Uri) = viewModelScope.launch {
        // A) placeholder
        val placeholder = Note(
            title     = "Voice Note",
            content   = "",
            timestamp = System.currentTimeMillis(),
            voicePath = uri.toString(),
            tags      = emptyList()
        )
        // B) get new ID
        val newId = repository.insertNote(placeholder).toInt()

        // C) upload & AI
        val downloadUrl = StorageHelper.uploadVoiceNote(uri)
        if (downloadUrl != null) {
            val result = AiService.summarizeNote(downloadUrl)
            // D) update that row
            repository.updateNote(
                placeholder.copy(
                    id      = newId,
                    title   = result.title,
                    content = result.summary,
                    tags    = result.tags
                )
            )
        }
    }
}

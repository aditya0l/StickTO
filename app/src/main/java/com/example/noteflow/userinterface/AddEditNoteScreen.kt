package com.example.noteflow.ui

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.noteflow.data.Note
import com.example.noteflow.data.remote.AiService
import com.example.noteflow.util.AudioPlayer
import com.example.noteflow.util.StorageHelper
import com.example.noteflow.util.VoiceRecorder
import com.example.noteflow.util.hasRecordPermission
import com.example.noteflow.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditNoteScreen(
    viewModel: NoteViewModel,
    noteId: Int? = null,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    val snackbarHost = remember { SnackbarHostState() }

    // STATES
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var noteTags by remember { mutableStateOf("") }
    val suggestedTags = remember { mutableStateListOf<String>() }

    var recordedFile by remember { mutableStateOf<String?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying   by remember { mutableStateOf(false) }
    var isSummarizing by remember { mutableStateOf(false) }

    // RECORD/PLAY HELPERS
    val recorder = remember { VoiceRecorder(context) }
    val player   = remember { AudioPlayer() }
    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            recordedFile = recorder.startRecording()
            isRecording = true
        }
    }

    // LOAD EXISTING NOTE
    LaunchedEffect(noteId) {
        if ((noteId ?: -1) >= 0) {
            viewModel.getNoteById(noteId!!)?.let {
                noteTitle   = it.title
                noteContent = it.content
                recordedFile = it.voicePath
                noteTags    = it.tags.joinToString(", ")
            }
        }
    }

    // UI
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if ((noteId ?: -1) >= 0) "Edit Note" else "Add Note") })
        },
        snackbarHost = { SnackbarHost(snackbarHost) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // 🟢 VOICE NOTE ONLY → immediate save + background summarize
                if (noteTitle.isBlank() && noteContent.isBlank() && recordedFile != null) {
                    val placeholder = Note(
                        // id = 0 will auto-generate
                        title = "Voice Note",
                        content = "",
                        timestamp = System.currentTimeMillis(),
                        voicePath = recordedFile,
                        tags = emptyList(),
                        isPendingSummary = true
                    )
                    scope.launch {
                        // 1️⃣ insert & get its new ID
                        val newId = viewModel.insertNoteReturningId(placeholder).toInt()
                        onBack() // navigate away instantly
                        // 2️⃣ kick off AI summarization
                        isSummarizing = true
                        val fileUri = Uri.parse(recordedFile!!)
                        val downloadUrl = StorageHelper.uploadVoiceNote(fileUri)
                        if (downloadUrl != null) {
                            val result = AiService.summarizeNote(downloadUrl)
                            // update the same row with real title/content/tags
                            viewModel.updateNote(
                                placeholder.copy(
                                    id = newId,
                                    title = result.title,
                                    content = result.summary,
                                    tags = result.tags,
                                    isPendingSummary = false
                                )
                            )
                        }
                        isSummarizing = false
                    }
                }
                // ✏️ REGULAR NOTE → save and back
                else {
                    val note = Note(
                        id = noteId ?: 0,
                        title = noteTitle.trim(),
                        content = noteContent.trim(),
                        timestamp = System.currentTimeMillis(),
                        voicePath = recordedFile,
                        tags = noteTags.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                        isPendingSummary = false
                    )
                    if ((noteId ?: -1) >= 0) viewModel.updateNote(note)
                    else viewModel.insertNote(note)
                    scope.launch { snackbarHost.showSnackbar("Note saved") }
                    onBack()
                }
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // 🔄 Summarizing Indicator
            if (isSummarizing) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
            }

            // TITLE
            OutlinedTextField(
                value = noteTitle,
                onValueChange = { noteTitle = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // CONTENT
            OutlinedTextField(
                value = noteContent,
                onValueChange = { noteContent = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(Modifier.height(16.dp))

            // TAGS
            OutlinedTextField(
                value = noteTags,
                onValueChange = { noteTags = it },
                label = { Text("Tags (comma separated)") },
                modifier = Modifier.fillMaxWidth()
            )
            if (suggestedTags.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text("Suggested Tags:", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    suggestedTags.forEach { tag ->
                        AssistChip(onClick = {
                            if (!noteTags.contains(tag)) noteTags = (noteTags + ", " + tag).trim(',', ' ')
                        }, label = { Text(tag) })
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // RECORDING UI
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    if (!isRecording) {
                        if (hasRecordPermission(context))
                            permLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    } else {
                        recordedFile = recorder.stopRecording()
                        isRecording = false
                    }
                }) {
                    Icon(
                        imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = if (isRecording) "Stop" else "Record"
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isRecording) "Recording…" else "Tap mic to record",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // PLAYBACK
            if (recordedFile != null && !isRecording) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        if (!isPlaying) {
                            player.play(recordedFile!!) { isPlaying = false }
                            isPlaying = true
                        } else {
                            player.stop()
                            isPlaying = false
                        }
                    }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = "Play/Stop"
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isPlaying) "Playing…" else "Tap to play",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

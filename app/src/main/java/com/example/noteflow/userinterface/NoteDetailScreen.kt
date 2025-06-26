package com.example.noteflow.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.noteflow.data.Note
import com.example.noteflow.util.AudioPlayer
import com.example.noteflow.viewmodel.NoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Int,
    viewModel: NoteViewModel,
    onEditClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    var note by remember { mutableStateOf<Note?>(null) }
    val audioPlayer = remember { AudioPlayer() }
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        withContext(Dispatchers.IO) {
            note = viewModel.getNoteById(noteId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(noteId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Note")
                    }
                }
            )
        }
    ) { padding ->
        note?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(it.title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(it.content, style = MaterialTheme.typography.bodyLarge)

                if (it.voicePath != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Voice Note:", style = MaterialTheme.typography.titleMedium)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            if (!isPlaying) {
                                audioPlayer.play(it.voicePath) {
                                    isPlaying = false
                                }
                                isPlaying = true
                            } else {
                                audioPlayer.stop()
                                isPlaying = false
                            }
                        }) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Stop Playback" else "Play Voice Note"
                            )
                        }

                        Text(if (isPlaying) "Playing..." else "Tap to play note")
                    }
                }
            }
        }
    }
}

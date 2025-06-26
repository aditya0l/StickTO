package com.example.noteflow.userinterface

import com.airbnb.lottie.compose.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteflow.data.Note
import com.example.noteflow.viewmodel.NoteViewModel
import com.example.noteflow.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    viewModel: NoteViewModel,
    onAddNoteClick: () -> Unit,
    onNoteClick: (Int) -> Unit,
    onSettingsClick: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var recentlyDeletedNote by remember { mutableStateOf<Note?>(null) }
    var showUndoSnackbar by remember { mutableStateOf(false) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortType by viewModel.sortType.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("STICKTO") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::setSearchQuery,
                    label = { Text("Search Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                var expanded by remember { mutableStateOf(false) }

                Box {
                    Button(onClick = { expanded = true }) {
                        Text("Sort: ${sortType.name.lowercase().replaceFirstChar { it.uppercase() }}")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("Latest First") }, onClick = {
                            viewModel.setSortType(NoteViewModel.SortType.LATEST)
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("Oldest First") }, onClick = {
                            viewModel.setSortType(NoteViewModel.SortType.OLDEST)
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("A-Z") }, onClick = {
                            viewModel.setSortType(NoteViewModel.SortType.ALPHABETICAL)
                            expanded = false
                        })
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (notes.isEmpty()) {
                item {
                    EmptyState(
                        message = "No notes found!",
                        subtext = if (searchQuery.isNotEmpty())
                            "Try changing your search keywords."
                        else
                            "You haven’t added any notes yet.",
                        onAddNoteClick = onAddNoteClick
                    )
                }
            } else {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        onClick = { onNoteClick(note.id) },
                        onDelete = {
                            viewModel.deleteNote(note)
                            recentlyDeletedNote = note
                            showUndoSnackbar = true
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    LaunchedEffect(showUndoSnackbar) {
        if (showUndoSnackbar && recentlyDeletedNote != null) {
            val result = snackbarHostState.showSnackbar(
                message = "Note deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.insertNote(recentlyDeletedNote!!)
            }
            recentlyDeletedNote = null
            showUndoSnackbar = false
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(note.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onDelete) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun EmptyState(
    message: String,
    subtext: String,
    onAddNoteClick: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.empty_state)
    )
    val progress by animateLottieCompositionAsState(composition)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.height(220.dp)
            )

            Text(
                text = message,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = subtext,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Button(onClick = onAddNoteClick) {
                Text("Add a Note")
            }
        }
    }
}

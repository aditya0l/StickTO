package com.example.noteflow.ui

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteflow.data.Note
import com.example.noteflow.ui.theme.*
import com.example.noteflow.util.AudioPlayer
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

    // Recording pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val recordingPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

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
    Box(modifier = Modifier.fillMaxSize()) {
        // Subtle creative background
        FloatingParticlesBackground(
            particleColor = AccentPurple.copy(alpha = 0.06f),
            particleCount = 25
        )
        
        AuroraBackground(
            colors = listOf(
                PrimaryGradientStart.copy(alpha = 0.1f),
                AccentBlue.copy(alpha = 0.08f),
                AccentGreen.copy(alpha = 0.06f)
            )
        )
        
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            if ((noteId ?: -1) >= 0) "✏️ Edit Note" else "📝 Create Note",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    )
                )
            },
            snackbarHost = { SnackbarHost(snackbarHost) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // Save note locally (with optional voice recording)
                        val note = Note(
                            id = noteId ?: 0,
                            title = noteTitle.trim().ifBlank { "Untitled Note" },
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
                    },
                    containerColor = PrimaryGradientEnd,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Default.Check, 
                        contentDescription = "Save",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        ) { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // TITLE
                OutlinedTextField(
                    value = noteTitle,
                    onValueChange = { noteTitle = it },
                    label = { Text("Note Title") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Title,
                            contentDescription = null,
                            tint = PrimaryGradientEnd
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGradientEnd,
                        focusedLabelColor = PrimaryGradientEnd
                    ),
                    textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(Modifier.height(20.dp))

                // CONTENT
                OutlinedTextField(
                    value = noteContent,
                    onValueChange = { noteContent = it },
                    label = { Text("Write your thoughts...") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = null,
                            tint = PrimaryGradientEnd
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGradientEnd,
                        focusedLabelColor = PrimaryGradientEnd
                    )
                )
                Spacer(Modifier.height(20.dp))

                // TAGS
                OutlinedTextField(
                    value = noteTags,
                    onValueChange = { noteTags = it },
                    label = { Text("Tags (comma separated)") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Label,
                            contentDescription = null,
                            tint = PrimaryGradientEnd
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGradientEnd,
                        focusedLabelColor = PrimaryGradientEnd
                    ),
                    placeholder = { Text("work, personal, ideas") }
                )
                
                AnimatedVisibility(visible = suggestedTags.isNotEmpty()) {
                    Column {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Suggested Tags:", 
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            suggestedTags.forEach { tag ->
                                AssistChip(
                                    onClick = {
                                        if (!noteTags.contains(tag)) noteTags = (noteTags + ", " + tag).trim(',', ' ')
                                    }, 
                                    label = { Text(tag) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = AccentGreen.copy(alpha = 0.15f),
                                        labelColor = AccentGreen
                                    )
                                )
                            }
                        }
                    }
                }
                
                Spacer(Modifier.height(24.dp))

                // VOICE RECORDING SECTION
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(
                            "🎙️ Voice Recording",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        // RECORDING UI
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(if (isRecording) (64.dp * recordingPulse) else 64.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isRecording) 
                                            Brush.radialGradient(
                                                colors = listOf(CardAccent1, CardAccent1.copy(alpha = 0.6f))
                                            )
                                        else 
                                            Brush.radialGradient(
                                                colors = listOf(PrimaryGradientEnd, PrimaryGradientStart)
                                            )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = {
                                        if (!isRecording) {
                                            if (hasRecordPermission(context))
                                                permLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        } else {
                                            recordedFile = recorder.stopRecording()
                                            isRecording = false
                                        }
                                    },
                                    modifier = Modifier.size(64.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                        contentDescription = if (isRecording) "Stop" else "Record",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                            
                            Column {
                                Text(
                                    text = if (isRecording) "Recording in progress..." else "Tap to start recording",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                if (isRecording) {
                                    Text(
                                        text = "🔴 Recording...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CardAccent1
                                    )
                                }
                            }
                        }

                        // PLAYBACK
                        AnimatedVisibility(visible = recordedFile != null && !isRecording) {
                            Column {
                                Spacer(Modifier.height(16.dp))
                                HorizontalDivider()
                                Spacer(Modifier.height(16.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(AccentBlue.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        IconButton(
                                            onClick = {
                                                if (!isPlaying) {
                                                    player.play(recordedFile!!) { isPlaying = false }
                                                    isPlaying = true
                                                } else {
                                                    player.stop()
                                                    isPlaying = false
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                                contentDescription = "Play/Stop",
                                                tint = AccentBlue,
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                    }
                                    
                                    Column {
                                        Text(
                                            text = if (isPlaying) "Playing audio..." else "Voice note recorded",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "Tap to ${if (isPlaying) "stop" else "play"}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

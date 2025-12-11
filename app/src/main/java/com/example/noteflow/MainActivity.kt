package com.example.noteflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.noteflow.data.NoteDatabse
import com.example.noteflow.data.ThemePreferenceManager
import com.example.noteflow.remoteconfig.RemoteConfigManager
import com.example.noteflow.repository.NoteRepositoryImpl
import com.example.noteflow.ui.NoteApp
import com.example.noteflow.ui.theme.NoteFlowTheme
import com.example.noteflow.viewmodel.AppConfigViewModel
import com.example.noteflow.viewmodel.NoteViewModel
import com.example.noteflow.viewmodel.NoteViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase Remote Config (FirebaseApp initialized inside)
        RemoteConfigManager.initialize(this)

        setContent {
            val context = LocalContext.current
            val themeManager = ThemePreferenceManager(context)
            val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)

            // Room setup with fallback migration
            val db = Room.databaseBuilder(
                context,
                NoteDatabse::class.java,
                "notes.db"
            ).fallbackToDestructiveMigration().build()

            // Repository & ViewModels
            val repo = NoteRepositoryImpl(db.noteDao())
            val factory = NoteViewModelFactory(repo)
            val noteViewModel: NoteViewModel = viewModel(factory = factory)
            val appConfigViewModel: AppConfigViewModel = viewModel()

            // Theming + App
            NoteFlowTheme(darkTheme = isDarkMode) {
                NoteApp(
                    noteViewModel = noteViewModel,
                    appConfigViewModel = appConfigViewModel,
                    isDarkTheme = isDarkMode,
                    onThemeToggle = {
                        CoroutineScope(Dispatchers.IO).launch {
                            themeManager.setDarkMode(it)
                        }
                    }
                )
            }
        }
    }
}

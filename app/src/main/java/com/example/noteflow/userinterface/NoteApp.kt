package com.example.noteflow.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteflow.userinterface.MaintenanceModeScreen
import com.example.noteflow.userinterface.NotesListScreen
import com.example.noteflow.userinterface.SettingsScreen
import com.example.noteflow.viewmodel.AppConfigViewModel
import com.example.noteflow.viewmodel.NoteViewModel

@Composable
fun NoteApp(
    noteViewModel: NoteViewModel,
    appConfigViewModel: AppConfigViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    
    // Observe Remote Config state
    val isLoading by appConfigViewModel.isLoading.collectAsState()
    val appEnabled by appConfigViewModel.appEnabled.collectAsState()
    val maintenanceMessage by appConfigViewModel.maintenanceMessage.collectAsState()
    
    // Show loading while fetching Remote Config
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    
    // Show maintenance screen if app is disabled
    if (!appEnabled) {
        MaintenanceModeScreen(message = maintenanceMessage)
        return
    }

    // Normal navigation when app is enabled
    NavHost(
        navController = navController,
        startDestination = "notes"
    ) {
        // ─── NOTES LIST ───────────────────────────────────────────────────
        composable("notes") {
            NotesListScreen(
                viewModel       = noteViewModel,
                onAddNoteClick  = { navController.navigate("add_edit") },
                onNoteClick     = { id -> navController.navigate("note_detail/$id") },
                onSettingsClick = { navController.navigate("settings") }
            )
        }

        // ─── SETTINGS ─────────────────────────────────────────────────────
        composable("settings") {
            SettingsScreen(
                onBack       = { navController.popBackStack() },
                isDarkTheme  = isDarkTheme,
                onThemeToggle= onThemeToggle
            )
        }

        // ─── NOTE DETAIL ─────────────────────────────────────────────────
        composable(
            "note_detail/{noteId}",
            arguments = listOf(navArgument("noteId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("noteId") ?: -1
            NoteDetailScreen(
                noteId      = id,
                viewModel   = noteViewModel,
                onBack      = { navController.popBackStack() },
                onEditClick = { navController.navigate("add_edit?noteId=$id") }
            )
        }

        // ─── ADD / EDIT NOTE ──────────────────────────────────────────────
        composable(
            "add_edit?noteId={noteId}",
            arguments = listOf(navArgument("noteId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val rawId = backStackEntry.arguments?.getInt("noteId") ?: -1
            AddEditNoteScreen(
                viewModel = noteViewModel,
                noteId    = rawId.takeIf { it >= 0 },
                onBack    = { navController.popBackStack() }
            )
        }
    }
}

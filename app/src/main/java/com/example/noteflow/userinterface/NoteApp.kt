package com.example.noteflow.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.example.noteflow.userinterface.LoginScreen
import com.example.noteflow.userinterface.RegisterScreen
import com.example.noteflow.userinterface.NotesListScreen
import com.example.noteflow.userinterface.SettingsScreen
import com.example.noteflow.viewmodel.AuthViewModel
import com.example.noteflow.viewmodel.NoteViewModel

@Composable
fun NoteApp(
    viewModel: NoteViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val authViewModel = remember { AuthViewModel() }
    val currentUser = FirebaseAuth.getInstance().currentUser

    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) "login" else "notes"
    ) {
        // ─── LOGIN ─────────────────────────────────────────────────────────
        composable("login") {
            LoginScreen(
                viewModel            = authViewModel,
                onLoginSuccess       = {
                    navController.navigate("notes") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        // ─── REGISTER ──────────────────────────────────────────────────────
        composable("register") {
            RegisterScreen(
                viewModel            = authViewModel,
                onRegisterSuccess    = {
                    navController.navigate("notes") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin    = {
                    navController.popBackStack("login", inclusive = false)
                }
            )
        }

        // ─── NOTES LIST ───────────────────────────────────────────────────
        composable("notes") {
            NotesListScreen(
                viewModel       = viewModel,
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
                onThemeToggle= onThemeToggle,
                onLogout     = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        // clear everything
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
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
                viewModel   = viewModel,
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
                viewModel = viewModel,
                noteId    = rawId.takeIf { it >= 0 },
                onBack    = { navController.popBackStack() }
            )
        }
    }
}

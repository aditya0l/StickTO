package com.example.noteflow.userinterface

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.noteflow.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (email.isBlank() || password.length < 6) {
                    errorText = "Please enter a valid email and password (6+ characters)"
                    return@Button
                }
                viewModel.register(email, password) { success, error ->
                    if (success) onRegisterSuccess()
                    else errorText = error.toString()
                }
            }) {
                Text("Register")
            }
            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login")
            }
            errorText?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

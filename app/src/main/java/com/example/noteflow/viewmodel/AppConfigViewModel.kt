package com.example.noteflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteflow.remoteconfig.RemoteConfigManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing app-level configuration from Remote Config.
 * 
 * Handles:
 * - Fetching Remote Config on app launch
 * - Exposing app_enabled state to UI
 * - Maintenance mode state management
 */
class AppConfigViewModel : ViewModel() {
    
    // Loading state
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // App enabled state (default: true)
    private val _appEnabled = MutableStateFlow(true)
    val appEnabled: StateFlow<Boolean> = _appEnabled.asStateFlow()
    
    // Maintenance message
    private val _maintenanceMessage = MutableStateFlow("")
    val maintenanceMessage: StateFlow<String> = _maintenanceMessage.asStateFlow()
    
    init {
        fetchRemoteConfig()
    }
    
    /**
     * Fetch Remote Config and update app state
     */
    private fun fetchRemoteConfig() {
        viewModelScope.launch {
            try {
                // Fetch and activate
                RemoteConfigManager.fetchAndActivate()
                
                // Update states
                _appEnabled.value = RemoteConfigManager.isAppEnabled()
                _maintenanceMessage.value = RemoteConfigManager.getMaintenanceMessage()
                
            } catch (e: Exception) {
                e.printStackTrace()
                // On error, default to enabled (fail-safe)
                _appEnabled.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Manually refresh config (for debug/testing)
     */
    fun refreshConfig() {
        _isLoading.value = true
        fetchRemoteConfig()
    }
}


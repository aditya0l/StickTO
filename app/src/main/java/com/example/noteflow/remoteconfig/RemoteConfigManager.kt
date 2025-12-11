package com.example.noteflow.remoteconfig

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

/**
 * Manages Firebase Remote Config for app-level feature toggles.
 * 
 * Primary use: Safe maintenance mode implementation.
 * If `app_enabled` is false, the app shows a maintenance screen.
 * This is a standard, Play Store-compliant feature toggle.
 */
object RemoteConfigManager {
    
    private lateinit var remoteConfig: FirebaseRemoteConfig
    
    // Remote Config Keys
    private const val KEY_APP_ENABLED = "app_enabled"
    private const val KEY_MAINTENANCE_MESSAGE = "maintenance_message"
    private const val KEY_MIN_SUPPORTED_VERSION = "min_supported_version"
    
    // Default Values (always allow app if fetch fails)
    private const val DEFAULT_APP_ENABLED = true
    private const val DEFAULT_MAINTENANCE_MESSAGE = 
        "This version is currently unavailable. Please check back later."
    private const val DEFAULT_MIN_SUPPORTED_VERSION = 1
    
    /**
     * Initialize Remote Config with default values.
     * Call this in Application.onCreate() or MainActivity.onCreate()
     * 
     * @param context Application or Activity context
     */
    fun initialize(context: Context) {
        // Initialize FirebaseApp first (safe to call multiple times)
        FirebaseApp.initializeApp(context)
        
        // Now get Remote Config instance
        remoteConfig = Firebase.remoteConfig
        
        // Configure settings
        val configSettings = remoteConfigSettings {
            // Fetch interval: 12 hours in production, 0 for debug
            minimumFetchIntervalInSeconds = if (isDebugMode()) 0 else 43200
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        
        // Set default values
        val defaults = mapOf(
            KEY_APP_ENABLED to DEFAULT_APP_ENABLED,
            KEY_MAINTENANCE_MESSAGE to DEFAULT_MAINTENANCE_MESSAGE,
            KEY_MIN_SUPPORTED_VERSION to DEFAULT_MIN_SUPPORTED_VERSION
        )
        remoteConfig.setDefaultsAsync(defaults)
    }
    

    suspend fun fetchAndActivate(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            e.printStackTrace()
            false // Fail gracefully - use defaults
        }
    }
    

    fun isAppEnabled(): Boolean {
        return remoteConfig.getBoolean(KEY_APP_ENABLED)
    }
    
    /**
     * Get the maintenance message to display
     */
    fun getMaintenanceMessage(): String {
        return remoteConfig.getString(KEY_MAINTENANCE_MESSAGE)
    }
    
    /**
     * Get minimum supported version code
     */
    fun getMinSupportedVersion(): Long {
        return remoteConfig.getLong(KEY_MIN_SUPPORTED_VERSION)
    }
    
    /**
     * Simple debug mode check
     * In production, use BuildConfig.DEBUG
     */
    private fun isDebugMode(): Boolean {
        return false // Set to BuildConfig.DEBUG in production
    }
}


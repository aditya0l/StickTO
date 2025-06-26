package com.example.noteflow.data.remote

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await

data class AiResult(
    val title: String,
    val summary: String,
    val tags: List<String>
)

object AiService {
    private val functions = FirebaseFunctions.getInstance()

    suspend fun summarizeNote(audioUrl: String): AiResult {
        val data = mapOf("audioUrl" to audioUrl)
        return try {
            val result = functions
                .getHttpsCallable("summarizeNote")
                .call(data)
                .await()
                .data as Map<*, *>

            // Safe cast
            val title   = result["title"]   as? String   ?: "Untitled"
            val summary = result["summary"] as? String   ?: ""
            val tags    = (result["tags"]   as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

            AiResult(title, summary, tags)
        } catch (e: Exception) {
            e.printStackTrace()
            AiResult("Error", "Could not summarize", emptyList())
        }
    }
}

package com.example.noteflow.util

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*

object StorageHelper {
    suspend fun uploadVoiceNote(fileUri: Uri): String? {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child("voice_notes/${UUID.randomUUID()}.m4a")

        return try {
            fileRef.putFile(fileUri).await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
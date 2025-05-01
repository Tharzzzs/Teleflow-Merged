package com.tele.teleflow.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tele.teleflow.data.Script
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScriptRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val scriptsCollection get() = db.collection("users")
        .document(auth.currentUser?.uid ?: "")
        .collection("scripts")

    suspend fun getScriptById(scriptId: String): Script? {
        return try {
            val document = scriptsCollection.document(scriptId).get().await()
            if (document.exists()) {
                val title = document.getString("title") ?: ""
                val content = document.getString("content") ?: ""
                val lastEdited = document.getString("lastEdited") ?: getCurrentTimeString()
                val isBookmarked = document.getBoolean("isBookmarked") ?: false

                Script(title, lastEdited, content, isBookmarked).apply {
                    // Use the document ID as the script ID
                    id = document.id
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createScript(script: Script) {
        val scriptData = hashMapOf(
            "title" to script.title,
            "content" to script.content,
            "lastEdited" to getCurrentTimeString(),
            "isBookmarked" to script.isBookmarked
        )

        scriptsCollection.document(script.id).set(scriptData).await()
    }

    suspend fun updateScript(script: Script) {
        val scriptData = hashMapOf(
            "title" to script.title,
            "content" to script.content,
            "lastEdited" to getCurrentTimeString(),
            "isBookmarked" to script.isBookmarked
        )

        scriptsCollection.document(script.id).update(scriptData as Map<String, Any>).await()
    }

    suspend fun deleteScript(scriptId: String): Result<Boolean> {
        return try {
            scriptsCollection.document(scriptId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllScripts(): Result<List<Script>> {
        return try {
            val snapshot = scriptsCollection.get().await()
            val scripts = snapshot.documents.mapNotNull { document ->
                val title = document.getString("title") ?: ""
                val content = document.getString("content") ?: ""
                val lastEdited = document.getString("lastEdited") ?: ""
                val isBookmarked = document.getBoolean("isBookmarked") ?: false

                Script(title, lastEdited, content, isBookmarked).apply {
                    id = document.id
                }
            }
            Result.success(scripts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecentScripts(): Result<List<Script>> {
        return try {
            val snapshot = scriptsCollection
                .orderBy("lastEdited", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .await()

            val scripts = snapshot.documents.mapNotNull { document ->
                val title = document.getString("title") ?: ""
                val content = document.getString("content") ?: ""
                val lastEdited = document.getString("lastEdited") ?: ""
                val isBookmarked = document.getBoolean("isBookmarked") ?: false

                Script(title, lastEdited, content, isBookmarked).apply {
                    id = document.id
                }
            }
            Result.success(scripts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookmarkedScripts(): Result<List<Script>> {
        return try {
            val snapshot = scriptsCollection
                .whereEqualTo("isBookmarked", true)
                .get()
                .await()

            val scripts = snapshot.documents.mapNotNull { document ->
                val title = document.getString("title") ?: ""
                val content = document.getString("content") ?: ""
                val lastEdited = document.getString("lastEdited") ?: ""
                val isBookmarked = document.getBoolean("isBookmarked") ?: true

                Script(title, lastEdited, content, isBookmarked).apply {
                    id = document.id
                }
            }
            Result.success(scripts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleBookmark(scriptId: String): Result<Boolean> {
        return try {
            // First get the current script to check its bookmark status
            val scriptDoc = scriptsCollection.document(scriptId).get().await()
            val currentBookmarkStatus = scriptDoc.getBoolean("isBookmarked") ?: false

            // Toggle the bookmark status
            val newBookmarkStatus = !currentBookmarkStatus

            // Update the document
            scriptsCollection.document(scriptId)
                .update("isBookmarked", newBookmarkStatus)
                .await()

            Result.success(newBookmarkStatus)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getCurrentTimeString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
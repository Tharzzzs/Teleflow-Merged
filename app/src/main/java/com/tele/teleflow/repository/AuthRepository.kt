package com.tele.teleflow.repository

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("usernames")

    /**
     * Registers a new user with email, password, and username.
     * Also updates the Firebase user profile with the username.
     */
    suspend fun registerUser(email: String, password: String, username: String): FirebaseUser {
        // Create user with email and password
        val result: AuthResult = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("Registration failed: No user returned")

        // Save username in Realtime Database under UID
        db.child(user.uid).setValue(username).await()

        // Optionally update the display name
        val profileUpdate = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()
        user.updateProfile(profileUpdate).await()

        return user
    }

    /**
     * Logs in the user using Firebase Authentication.
     */
    suspend fun login(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: throw Exception("Login failed: No user returned")
    }

    /**
     * Returns the currently authenticated Firebase user, or null if not signed in.
     */
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Signs the user out.
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Deletes the currently logged-in user's account.
     */
    suspend fun deleteAccount() {
        val user = auth.currentUser ?: throw Exception("No user signed in")
        user.delete().await()
    }

    /**
     * Updates the display name of the current user.
     */
    suspend fun updateUsername(newUsername: String) {
        val user = auth.currentUser ?: throw Exception("No user signed in")
        val update = UserProfileChangeRequest.Builder()
            .setDisplayName(newUsername)
            .build()
        user.updateProfile(update).await()
        db.child(user.uid).setValue(newUsername).await()
    }

    /**
     * Updates the user's email address.
     */
    suspend fun updateEmail(newEmail: String) {
        val user = auth.currentUser ?: throw Exception("No user signed in")
        user.updateEmail(newEmail).await()
    }

    /**
     * Updates the user's password.
     */
    suspend fun updatePassword(newPassword: String) {
        val user = auth.currentUser ?: throw Exception("No user signed in")
        user.updatePassword(newPassword).await()
    }

    /**
     * Uploads a new profile image URL (if you store URLs manually).
     * This is placeholder behavior; actual storage should be handled via Firebase Storage.
     */
    suspend fun updateProfileImage(imageUrl: String) {
        val user = auth.currentUser ?: throw Exception("No user signed in")
        val update = UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(imageUrl))
            .build()
        user.updateProfile(update).await()
    }

    suspend fun getUserData(): Result<UserData> {
        val user = auth.currentUser ?: return Result.failure(Exception("No user logged in"))

        return try {
            val usernameSnapshot = db.child(user.uid).get().await()
            val username = usernameSnapshot.getValue(String::class.java) ?: user.displayName ?: "Unknown"

            val userData = UserData(
                uid = user.uid,
                username = username,
                email = user.email ?: "No email",
                profileImageUrl = user.photoUrl?.toString() ?: "https://example.com/default_profile.jpg"
            )

            Result.success(userData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

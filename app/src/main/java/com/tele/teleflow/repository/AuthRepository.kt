package com.tele.teleflow.repository

import android.net.Uri
import java.util.UUID

// Mock implementation for development without Firebase
class AuthRepository {
    // Mock user data
    private var currentUser: MockFirebaseUser? = MockFirebaseUser(
        uid = "user123",
        email = "user@example.com",
        displayName = "Test User"
    )

    // Register a new user
    suspend fun registerUser(email: String, password: String, username: String): MockFirebaseUser {
        // Simulate network delay
        kotlinx.coroutines.delay(1000)

        // Create a mock user
        currentUser = MockFirebaseUser(
            uid = UUID.randomUUID().toString(),
            email = email,
            displayName = username
        )

        return currentUser!!
    }

    // Get current user
    suspend fun getCurrentUser(): MockFirebaseUser? {
        // Simulate network delay
        kotlinx.coroutines.delay(500)
        return currentUser
    }

    // Get user preferences
    suspend fun getUserPreferences(userId: String): UserPreferences? {
        // Simulate network delay
        kotlinx.coroutines.delay(500)

        // Return mock preferences
        return UserPreferences(
            pushNotificationsEnabled = true,
            emailNotificationsEnabled = true,
            profileVisible = true
        )
    }

    // Upload profile image
    suspend fun uploadProfileImage(imageUri: Uri): String {
        // Simulate network delay
        kotlinx.coroutines.delay(2000)

        // Return a mock URL
        return "https://example.com/profile_images/${currentUser?.uid}.jpg"
    }

    // Update profile image
    suspend fun updateProfileImage(imageUrl: String) {
        // Simulate network delay
        kotlinx.coroutines.delay(1000)

        // Update mock user
        currentUser = currentUser?.copy(photoUrl = Uri.parse(imageUrl))
    }

    // Update username
    suspend fun updateUsername(newUsername: String) {
        // Simulate network delay
        kotlinx.coroutines.delay(1000)

        // Update mock user
        currentUser = currentUser?.copy(displayName = newUsername)
    }

    // Update email
    suspend fun updateEmail(newEmail: String) {
        // Simulate network delay
        kotlinx.coroutines.delay(1000)

        // Update mock user
        currentUser = currentUser?.copy(email = newEmail)
    }

    // Update password
    suspend fun updatePassword(newPassword: String) {
        // Simulate network delay
        kotlinx.coroutines.delay(1000)

        // Password updated (no actual change in mock)
    }

    // Update user preference
    suspend fun updateUserPreference(key: String, value: Boolean) {
        // Simulate network delay
        kotlinx.coroutines.delay(500)

        // Preference updated (no actual change in mock)
    }

    // Delete account
    suspend fun deleteAccount() {
        // Simulate network delay
        kotlinx.coroutines.delay(1500)

        // Clear current user
        currentUser = null
    }

    // Sign out
    fun signOut() {
        // Clear current user
        currentUser = null
    }

    suspend fun login(email: String, password: String): Result<MockFirebaseUser > {
        // Simulate network delay
        kotlinx.coroutines.delay(1000)

        // Check if the email matches the mock user data
        return if (currentUser ?.email == email) {
            // Simulate successful login
            Result.success(currentUser !!)
        } else {
            // Simulate login failure
            Result.failure(Exception("Invalid email or password"))
        }
    }


}

// Mock Firebase User class
data class MockFirebaseUser(
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: Uri? = null
)

// User preferences data class
data class UserPreferences(
    val pushNotificationsEnabled: Boolean = true,
    val emailNotificationsEnabled: Boolean = true,
    val profileVisible: Boolean = true
)
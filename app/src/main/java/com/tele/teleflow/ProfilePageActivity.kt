package com.tele.teleflow

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import com.tele.teleflow.repository.AuthRepository
import kotlinx.coroutines.launch

class ProfilePageActivity : AppCompatActivity() {

    private lateinit var usernameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var profileImage: ImageView
    private lateinit var changePhotoButton: MaterialButton
    private lateinit var pushNotificationsSwitch: SwitchMaterial
    private lateinit var emailNotificationsSwitch: SwitchMaterial
    private lateinit var profileVisibilitySwitch: SwitchMaterial
    private lateinit var loadingView: View

    private val authRepository = AuthRepository()
    private var selectedImageUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                profileImage.setImageURI(uri)
//                uploadProfileImage()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        initializeViews()
//        loadUserData()
        setupClickListeners()
    }

    private fun initializeViews() {
        // Initialize Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Profile"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Initialize TextInputLayouts
        usernameLayout = findViewById(R.id.username_layout)
        emailLayout = findViewById(R.id.email_layout)
        passwordLayout = findViewById(R.id.password_layout)

        // Initialize EditTexts
        usernameEditText = findViewById(R.id.edit_username)
        emailEditText = findViewById(R.id.edit_email)
        passwordEditText = findViewById(R.id.edit_password)

        // Initialize ImageView and Button
        profileImage = findViewById(R.id.profile_image)
        changePhotoButton = findViewById(R.id.change_photo_button)

        // Initialize Switches
        pushNotificationsSwitch = findViewById(R.id.push_notifications_switch)
        emailNotificationsSwitch = findViewById(R.id.email_notifications_switch)
        profileVisibilitySwitch = findViewById(R.id.profile_visibility_switch)

        // Initialize Loading View
        loadingView = findViewById(R.id.loading_view)
    }

    // Implement the missing loadUserData method
//    private fun loadUserData() {
//        showLoading(true)
//
//        lifecycleScope.launch {
//            try {
//                val currentUser = authRepository.getCurrentUser()
//                if (currentUser != null) {
//                    // Set user data to UI
//                    usernameEditText.setText(currentUser.displayName ?: "")
//                    emailEditText.setText(currentUser.email ?: "")
//
//                    // Load profile image if available
//                    currentUser.photoUrl?.let { url ->
//                        // In a real app, you would use Glide or similar library to load the image
//                        // For now, we'll just log it
//                        Log.d("ProfilePageActivity", "Profile image URL: $url")
//                    }
//
//                    // Load user preferences from Firestore
//                    val userPrefs = authRepository.getUserPreferences(currentUser.uid)
//                    pushNotificationsSwitch.isChecked = userPrefs?.pushNotificationsEnabled ?: true
//                    emailNotificationsSwitch.isChecked = userPrefs?.emailNotificationsEnabled ?: true
//                    profileVisibilitySwitch.isChecked = userPrefs?.profileVisible ?: true
//                }
//            } catch (e: Exception) {
//                Log.e("ProfilePageActivity", "Error loading user data", e)
//                Toast.makeText(this@ProfilePageActivity, "Failed to load user data: ${e.message}", Toast.LENGTH_SHORT).show()
//            } finally {
//                showLoading(false)
//            }
//        }
//    }

    // Implement the missing setupClickListeners method
    private fun setupClickListeners() {
        // Change photo button
        changePhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImage.launch(intent)
        }

        // Save username button
        findViewById<MaterialButton>(R.id.save_username_button).setOnClickListener {
            val newUsername = usernameEditText.text.toString().trim()
            if (newUsername.isEmpty()) {
                usernameLayout.error = "Username cannot be empty"
                return@setOnClickListener
            }

            updateUsername(newUsername)
        }

        // Save email button
        findViewById<MaterialButton>(R.id.save_email_button).setOnClickListener {
            val newEmail = emailEditText.text.toString().trim()
            if (newEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                emailLayout.error = "Please enter a valid email address"
                return@setOnClickListener
            }

            updateEmail(newEmail)
        }

        // Save password button
        findViewById<MaterialButton>(R.id.save_password_button).setOnClickListener {
            val newPassword = passwordEditText.text.toString()
            if (newPassword.isEmpty() || newPassword.length < 6) {
                passwordLayout.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            updatePassword(newPassword)
        }

        // Switch listeners
//        pushNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
//            updateUserPreference("pushNotificationsEnabled", isChecked)
//        }
//
//        emailNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
//            updateUserPreference("emailNotificationsEnabled", isChecked)
//        }
//
//        profileVisibilitySwitch.setOnCheckedChangeListener { _, isChecked ->
//            updateUserPreference("profileVisible", isChecked)
//        }

        // Delete account button
        findViewById<MaterialButton>(R.id.delete_account_button).setOnClickListener {
            showDeleteAccountConfirmation()
        }
    }

//    private fun uploadProfileImage() {
//        selectedImageUri?.let { uri ->
//            showLoading(true)
//
//            lifecycleScope.launch {
//                try {
//                    val downloadUrl = authRepository.uploadProfileImage(uri)
//                    authRepository.updateProfileImage(downloadUrl)
//                    Toast.makeText(this@ProfilePageActivity, "Profile image updated", Toast.LENGTH_SHORT).show()
//                } catch (e: Exception) {
//                    Log.e("ProfilePageActivity", "Error uploading profile image", e)
//                    Toast.makeText(this@ProfilePageActivity, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
//                } finally {
//                    showLoading(false)
//                }
//            }
//        }
//    }

    private fun updateUsername(newUsername: String) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                authRepository.updateUsername(newUsername)
                Toast.makeText(this@ProfilePageActivity, "Username updated", Toast.LENGTH_SHORT).show()
                usernameLayout.error = null
            } catch (e: Exception) {
                Log.e("ProfilePageActivity", "Error updating username", e)
                Toast.makeText(this@ProfilePageActivity, "Failed to update username: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun updateEmail(newEmail: String) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                authRepository.updateEmail(newEmail)
                Toast.makeText(this@ProfilePageActivity, "Email updated", Toast.LENGTH_SHORT).show()
                emailLayout.error = null
            } catch (e: Exception) {
                Log.e("ProfilePageActivity", "Error updating email", e)
                Toast.makeText(this@ProfilePageActivity, "Failed to update email: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun updatePassword(newPassword: String) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                authRepository.updatePassword(newPassword)
                Toast.makeText(this@ProfilePageActivity, "Password updated", Toast.LENGTH_SHORT).show()
                passwordLayout.error = null
                passwordEditText.setText("")
            } catch (e: Exception) {
                Log.e("ProfilePageActivity", "Error updating password", e)
                Toast.makeText(this@ProfilePageActivity, "Failed to update password: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                showLoading(false)
            }
        }
    }

//    private fun updateUserPreference(key: String, value: Boolean) {
//        lifecycleScope.launch {
//            try {
//                authRepository.updateUserPreference(key, value)
//            } catch (e: Exception) {
//                Log.e("ProfilePageActivity", "Error updating user preference", e)
//                Toast.makeText(this@ProfilePageActivity, "Failed to update preference: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun showDeleteAccountConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteAccount() {
        showLoading(true)

        lifecycleScope.launch {
            try {
                authRepository.deleteAccount()
                Toast.makeText(this@ProfilePageActivity, "Account deleted", Toast.LENGTH_SHORT).show()

                // Navigate to login screen
                val intent = Intent(this@ProfilePageActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e("ProfilePageActivity", "Error deleting account", e)
                Toast.makeText(this@ProfilePageActivity, "Failed to delete account: ${e.message}", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        loadingView.visibility = if (show) View.VISIBLE else View.GONE
    }
}
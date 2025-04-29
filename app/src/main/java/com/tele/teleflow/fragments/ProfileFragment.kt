package com.tele.teleflow.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.tele.teleflow.LoginActivity
import com.tele.teleflow.ProfilePageActivity
import com.tele.teleflow.R
import com.tele.teleflow.SettingsActivity
import com.tele.teleflow.repository.AuthRepository
import com.tele.teleflow.utils.FirebaseUtils
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var profilePicture: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var editProfileButton: MaterialButton
    private lateinit var logoutButton: MaterialButton
    private lateinit var loadingView: View

    private val authRepository = AuthRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupClickListeners()
        loadProfileData()
        setupProfileCards(view)
    }

    private fun initializeViews(view: View) {
        profilePicture = view.findViewById(R.id.profile_picture)
        userNameTextView = view.findViewById(R.id.user_name)
        userEmailTextView = view.findViewById(R.id.user_email)
        editProfileButton = view.findViewById(R.id.edit_profile_button)
        logoutButton = view.findViewById(R.id.logout_button)
        loadingView = view.findViewById(R.id.loading_view)
    }

    private fun setupClickListeners() {
        // Edit profile button
        editProfileButton.setOnClickListener {
            startActivity(Intent(requireContext(), ProfilePageActivity::class.java))
        }

        // Logout button
        logoutButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    authRepository.logout()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    showToast("Logged out successfully")
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun setupProfileCards(view: View) {
        view.findViewById<MaterialCardView>(R.id.card_my_scripts).setOnClickListener {
            // Navigate to Scripts tab
            val bottomNav = requireActivity().findViewById<View>(R.id.bottom_navigation)
            if (bottomNav is com.google.android.material.bottomnavigation.BottomNavigationView) {
                bottomNav.selectedItemId = R.id.nav_script
            }
        }

        view.findViewById<MaterialCardView>(R.id.card_bookmarks).setOnClickListener {
            // Navigate to Home tab and show bookmarks
            val bottomNav = requireActivity().findViewById<View>(R.id.bottom_navigation)
            if (bottomNav is com.google.android.material.bottomnavigation.BottomNavigationView) {
                bottomNav.selectedItemId = R.id.nav_home
                // This is a bit of a hack, but it works for this demo
                // In a real app, you'd use a shared ViewModel or other communication method
                val homeFragment = requireActivity().supportFragmentManager.fragments
                    .filterIsInstance<HomeFragment>()
                    .firstOrNull()

                homeFragment?.let {
                    // Use reflection to call the private method
                    try {
                        val method = HomeFragment::class.java.getDeclaredMethod("loadBookmarkedScripts")
                        method.isAccessible = true
                        method.invoke(it)
                    } catch (e: Exception) {
                        Log.e("ProfileFragment", "Failed to call loadBookmarkedScripts", e)
                    }
                }
            }
        }

        view.findViewById<MaterialCardView>(R.id.card_settings).setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfileData()
    }

    private fun loadProfileData() {
        loadingView.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val result = authRepository.getUserData()

                if (result.isSuccess) {
                    val user = result.getOrNull()

                    if (user != null) {
                        userNameTextView.text = user.username
                        userEmailTextView.text = user.email

                        // Load profile image
                        FirebaseUtils.loadImage(
                            requireContext(),
                            user.profileImageUrl,
                            profilePicture
                        )
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    Log.e("ProfileFragment", "Failed to load user data", exception)
                    showToast("Failed to load profile: ${exception?.message}")
                }
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Exception loading user data", e)
                showToast("Error loading profile: ${e.message}")
            } finally {
                loadingView.visibility = View.GONE
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

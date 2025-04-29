package com.tele.teleflow.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tele.teleflow.R
import com.tele.teleflow.ScriptEditorActivity
import com.tele.teleflow.SettingsActivity
import com.tele.teleflow.adapters.RecentScriptAdapter
import com.tele.teleflow.data.Script
import com.tele.teleflow.repository.ScriptRepository
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var recentScriptsRecyclerView: RecyclerView
    private lateinit var adapter: RecentScriptAdapter
    private lateinit var loadingView: View
    private lateinit var emptyView: View

    private val scriptRepository = ScriptRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
//        loadingView = view.findViewById(R.id.loading_view)
//        emptyView = view.findViewById(R.id.empty_view)

        // Setup settings button
        view.findViewById<View>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        // Setup recent scripts recycler view
        recentScriptsRecyclerView = view.findViewById(R.id.recent_scripts_recycler)
        recentScriptsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = RecentScriptAdapter(emptyList()) { script ->
            // Open script editor with the selected script
            val intent = Intent(requireContext(), ScriptEditorActivity::class.java)
            intent.putExtra("scriptId", script.id)
            startActivity(intent)
        }

        recentScriptsRecyclerView.adapter = adapter

        // Setup quick action cards
        setupQuickActionCards(view)

        // Setup floating action button
        view.findViewById<FloatingActionButton>(R.id.fab_create_script).setOnClickListener {
            startActivity(Intent(requireContext(), ScriptEditorActivity::class.java))
        }

        // Load data
        loadScripts()
    }

    override fun onResume() {
        super.onResume()
        // Reload scripts when returning to this fragment
        loadScripts()
    }

    private fun setupQuickActionCards(view: View) {
        view.findViewById<MaterialCardView>(R.id.card_create_script).setOnClickListener {
            startActivity(Intent(requireContext(), ScriptEditorActivity::class.java))
        }

        view.findViewById<MaterialCardView>(R.id.card_templates).setOnClickListener {
            showToast("Browse templates feature coming soon")
        }

        view.findViewById<MaterialCardView>(R.id.card_bookmarks).setOnClickListener {
            loadBookmarkedScripts()
        }
    }

    private fun loadScripts() {
        loadingView.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        recentScriptsRecyclerView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val result = scriptRepository.getRecentScripts()

                if (result.isSuccess) {
                    val scripts = result.getOrNull() ?: emptyList()
                    updateUI(scripts)
                } else {
                    val exception = result.exceptionOrNull()
                    Log.e("HomeFragment", "Failed to load scripts", exception)
                    showToast("Failed to load scripts: ${exception?.message}")
                    updateUI(emptyList())
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Exception loading scripts", e)
                showToast("Error loading scripts: ${e.message}")
                updateUI(emptyList())
            }
        }
    }

    private fun loadBookmarkedScripts() {
        loadingView.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        recentScriptsRecyclerView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val result = scriptRepository.getBookmarkedScripts()

                if (result.isSuccess) {
                    val scripts = result.getOrNull() ?: emptyList()
                    updateUI(scripts)
                    if (scripts.isNotEmpty()) {
                        showToast("Showing bookmarked scripts")
                    } else {
                        showToast("No bookmarked scripts found")
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    Log.e("HomeFragment", "Failed to load bookmarked scripts", exception)
                    showToast("Failed to load bookmarks: ${exception?.message}")
                    updateUI(emptyList())
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Exception loading bookmarked scripts", e)
                showToast("Error loading bookmarks: ${e.message}")
                updateUI(emptyList())
            }
        }
    }

    private fun updateUI(scripts: List<Script>) {
        loadingView.visibility = View.GONE

        if (scripts.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            recentScriptsRecyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recentScriptsRecyclerView.visibility = View.VISIBLE
            adapter.updateScripts(scripts)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

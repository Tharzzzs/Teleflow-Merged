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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tele.teleflow.R
import com.tele.teleflow.ScriptEditorActivity
import com.tele.teleflow.SettingsActivity
import com.tele.teleflow.adapters.ScriptAdapter
import com.tele.teleflow.data.Script
import com.tele.teleflow.repository.ScriptRepository
import kotlinx.coroutines.launch

class ScriptFragment : Fragment() {

    private lateinit var scriptsRecyclerView: RecyclerView
    private lateinit var scriptAdapter: ScriptAdapter
    private lateinit var loadingView: View
    private lateinit var emptyView: View

    private val scriptRepository = ScriptRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_script, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
//        loadingView = view.findViewById(R.id.loading_view)
//        emptyView = view.findViewById(R.id.empty_view)

        // Setup RecyclerView
        scriptsRecyclerView = view.findViewById(R.id.scripts_recycler)
        scriptsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        scriptAdapter = ScriptAdapter(requireContext(), emptyList())
        scriptAdapter.setOnItemClickListener { script ->
            // Open script editor with the selected script
            val intent = Intent(requireContext(), ScriptEditorActivity::class.java)
            intent.putExtra("scriptId", script.id)
            startActivity(intent)
        }

        scriptAdapter.setOnBookmarkClickListener { script ->
            toggleBookmark(script)
        }

        scriptAdapter.setOnDeleteClickListener { script ->
            deleteScript(script)
        }

        scriptsRecyclerView.adapter = scriptAdapter

        // Setup settings button
        view.findViewById<View>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

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

    private fun loadScripts() {
        loadingView.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        scriptsRecyclerView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val result = scriptRepository.getAllScripts()

                if (result.isSuccess) {
                    val scripts = result.getOrNull() ?: emptyList()
                    updateUI(scripts)
                } else {
                    val exception = result.exceptionOrNull()
                    Log.e("ScriptFragment", "Failed to load scripts", exception)
                    showToast("Failed to load scripts: ${exception?.message}")
                    updateUI(emptyList())
                }
            } catch (e: Exception) {
                Log.e("ScriptFragment", "Exception loading scripts", e)
                showToast("Error loading scripts: ${e.message}")
                updateUI(emptyList())
            }
        }
    }

    private fun toggleBookmark(script: Script) {
        lifecycleScope.launch {
            try {
                val result = scriptRepository.toggleBookmark(script.id)

                if (result.isSuccess) {
                    val isBookmarked = result.getOrNull() ?: false
                    showToast("${script.title} ${if (isBookmarked) "bookmarked" else "unbookmarked"}")
                    // Reload scripts to update UI
                    loadScripts()
                } else {
                    val exception = result.exceptionOrNull()
                    Log.e("ScriptFragment", "Failed to toggle bookmark", exception)
                    showToast("Failed to update bookmark: ${exception?.message}")
                }
            } catch (e: Exception) {
                Log.e("ScriptFragment", "Exception toggling bookmark", e)
                showToast("Error updating bookmark: ${e.message}")
            }
        }
    }

    private fun deleteScript(script: Script) {
        lifecycleScope.launch {
            try {
                val result = scriptRepository.deleteScript(script.id)

                if (result.isSuccess) {
                    showToast("${script.title} deleted")
                    // Reload scripts to update UI
                    loadScripts()
                } else {
                    val exception = result.exceptionOrNull()
                    Log.e("ScriptFragment", "Failed to delete script", exception)
                    showToast("Failed to delete script: ${exception?.message}")
                }
            } catch (e: Exception) {
                Log.e("ScriptFragment", "Exception deleting script", e)
                showToast("Error deleting script: ${e.message}")
            }
        }
    }

    private fun updateUI(scripts: List<Script>) {
        loadingView.visibility = View.GONE

        if (scripts.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            scriptsRecyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            scriptsRecyclerView.visibility = View.VISIBLE
            scriptAdapter.updateScripts(scripts)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

package com.tele.teleflow

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tele.teleflow.data.Script
import com.tele.teleflow.repository.ScriptRepository
import kotlinx.coroutines.launch

class ScriptEditorActivity : AppCompatActivity() {

    private lateinit var titleEditText: TextInputEditText
    private lateinit var contentEditText: EditText
    private lateinit var titleLayout: TextInputLayout
    private lateinit var loadingView: View

    private val scriptRepository = ScriptRepository()
    private var scriptId: String? = null
    private var currentScript: Script? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_script_editor)

        // Initialize views
        titleEditText = findViewById(R.id.edit_script_title)
        contentEditText = findViewById(R.id.edit_script_content)
        titleLayout = findViewById(R.id.title_layout)
        loadingView = findViewById(R.id.loading_view)

        // Setup toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Script Editor"

        // Get script ID from intent
        scriptId = intent.getStringExtra("scriptId")

        // Load script if editing an existing one
        if (scriptId != null) {
            loadScript(scriptId!!)
        }

        // Set up toolbar navigation
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    // Load script from repository
    private fun loadScript(scriptId: String) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val script = scriptRepository.getScriptById(scriptId)
                if (script != null) {
                    currentScript = script

                    // Update UI with script data
                    titleEditText.setText(script.title)
                    contentEditText.setText(script.content)

                    supportActionBar?.title = "Edit Script"
                } else {
                    Toast.makeText(this@ScriptEditorActivity, "Script not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Log.e("ScriptEditorActivity", "Error loading script", e)
                Toast.makeText(this@ScriptEditorActivity, "Failed to load script: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
            } finally {
                showLoading(false)
            }
        }
    }

    // Create options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu resource file
        menuInflater.inflate(R.menu.menu_script_editor, menu)
        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveScript()
                true
            }
            R.id.action_delete -> {
                showDeleteConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveScript() {
        val title = titleEditText.text.toString().trim()
        val content = contentEditText.text.toString().trim()

        if (title.isEmpty()) {
            titleLayout.error = "Title cannot be empty"
            return
        }

        showLoading(true)

        lifecycleScope.launch {
            try {
                if (currentScript != null) {
                    // Update existing script
                    currentScript?.title = title
                    currentScript?.content = content
                    scriptRepository.updateScript(currentScript!!)
                } else {
                    // Create new script
                    val newScript = Script(title, System.currentTimeMillis().toString())
                    newScript.content = content
                    scriptRepository.createScript(newScript)
                }

                Toast.makeText(this@ScriptEditorActivity, "Script saved", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Log.e("ScriptEditorActivity", "Error saving script", e)
                Toast.makeText(this@ScriptEditorActivity, "Failed to save script: ${e.message}", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        }
    }

    private fun showDeleteConfirmation() {
        if (currentScript == null) {
            Toast.makeText(this, "Cannot delete unsaved script", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Delete Script")
            .setMessage("Are you sure you want to delete this script?")
            .setPositiveButton("Delete") { _, _ ->
                deleteScript()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteScript() {
        if (currentScript == null) return

        showLoading(true)

        lifecycleScope.launch {
            try {
                scriptRepository.deleteScript(currentScript!!.id)
                Toast.makeText(this@ScriptEditorActivity, "Script deleted", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Log.e("ScriptEditorActivity", "Error deleting script", e)
                Toast.makeText(this@ScriptEditorActivity, "Failed to delete script: ${e.message}", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        loadingView.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() {
        // Check if there are unsaved changes
        val title = titleEditText.text.toString().trim()
        val content = contentEditText.text.toString().trim()

        val hasChanges = if (currentScript != null) {
            title != currentScript?.title || content != currentScript?.content
        } else {
            title.isNotEmpty() || content.isNotEmpty()
        }

        if (hasChanges) {
            AlertDialog.Builder(this)
                .setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Do you want to save them?")
                .setPositiveButton("Save") { _, _ ->
                    saveScript()
                }
                .setNegativeButton("Discard") { _, _ ->
                    super.onBackPressed()
                }
                .setNeutralButton("Cancel", null)
                .show()
        } else {
            super.onBackPressed()
        }
    }
}
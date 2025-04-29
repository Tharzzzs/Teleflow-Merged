package com.tele.teleflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tele.teleflow.R
import com.tele.teleflow.data.Script

class RecentScriptAdapter(
    private var scripts: List<Script>,
    private val onItemClick: (Script) -> Unit
) : RecyclerView.Adapter<RecentScriptAdapter.ScriptViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScriptViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_script, parent, false)
        return ScriptViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScriptViewHolder, position: Int) {
        val script = scripts[position]
        holder.bind(script)
    }

    override fun getItemCount(): Int = scripts.size

    fun updateScripts(newScripts: List<Script>) {
        scripts = newScripts
        notifyDataSetChanged()
    }

    inner class ScriptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_script_title)
        private val timeTextView: TextView = itemView.findViewById(R.id.text_script_time)

        fun bind(script: Script) {
            titleTextView.text = script.title
            timeTextView.text = "Last edited: ${script.getLastEditedString()}"

            itemView.setOnClickListener {
                onItemClick(script)
            }
        }
    }
}

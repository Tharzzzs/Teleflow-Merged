package com.tele.teleflow.adapters

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.tele.teleflow.R
import com.tele.teleflow.data.Script

class ScriptAdapter(
    private val context: Context,
    private var scripts: List<Script>
) : RecyclerView.Adapter<ScriptAdapter.ScriptViewHolder>(), ListAdapter {

    private var onItemClickListener: ((Script) -> Unit)? = null
    private var onBookmarkClickListener: ((Script) -> Unit)? = null
    private var onDeleteClickListener: ((Script) -> Unit)? = null

    fun setOnItemClickListener(listener: (Script) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnBookmarkClickListener(listener: (Script) -> Unit) {
        onBookmarkClickListener = listener
    }

    fun setOnDeleteClickListener(listener: (Script) -> Unit) {
        onDeleteClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScriptViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_detailed_script, parent, false)
        return ScriptViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScriptViewHolder, position: Int) {
        holder.bind(scripts[position])
    }

    override fun getItemCount(): Int = scripts.size

    fun updateScripts(newScripts: List<Script>) {
        scripts = newScripts
        notifyDataSetChanged()
    }

    inner class ScriptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_script_title)
        private val timeTextView: TextView = itemView.findViewById(R.id.text_script_time)
        private val bookmarkButton: ImageButton = itemView.findViewById(R.id.btn_bookmark)
        // Make sure this ID exists in item_detailed_script.xml
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btn_delete)
        private val cardView: MaterialCardView = itemView.findViewById(R.id.script_card)

        fun bind(script: Script) {
            titleTextView.text = script.title
            timeTextView.text = "Last edited: ${script.getLastEditedString()}"

            // Set bookmark icon
            bookmarkButton.setImageResource(
                if (script.isBookmarked) R.drawable.bookmark_clicked
                else R.drawable.bookmark_unclicked
            )

            cardView.setOnClickListener {
                onItemClickListener?.invoke(script)
            }

            bookmarkButton.setOnClickListener {
                onBookmarkClickListener?.invoke(script)
            }

            deleteButton.setOnClickListener {
                onDeleteClickListener?.invoke(script)
            }
        }
    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(p0: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun areAllItemsEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEnabled(p0: Int): Boolean {
        TODO("Not yet implemented")
    }
}
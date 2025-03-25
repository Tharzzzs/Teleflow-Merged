package com.tele.teleflow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.tele.teleflow.R
import com.tele.teleflow.data.Script

class ScriptAdapter(
    context: Context,
    private val scripts: List<Script>
) : ArrayAdapter<Script>(context, R.layout.item_detailed_script, scripts) {

    interface OnBookmarkClickListener {
        fun onBookmarkClick(position: Int, script: Script)
    }   

    var bookmarkClickListener: OnBookmarkClickListener? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_detailed_script, parent, false)

        val script = scripts[position]
        
        view.findViewById<TextView>(R.id.text_script_title).text = script.title
        view.findViewById<TextView>(R.id.text_script_time).text = script.lastEdited

        val bookmarkButton = view.findViewById<ImageButton>(R.id.btn_bookmark)
        bookmarkButton.setImageResource(
            if (script.isBookmarked) R.drawable.bookmark_clicked
            else R.drawable.bookmark_unclicked
        )

        bookmarkButton.setOnClickListener {
            script.isBookmarked = !script.isBookmarked
            bookmarkButton.setImageResource(
                if (script.isBookmarked) R.drawable.bookmark_clicked
                else R.drawable.bookmark_unclicked
            )
            bookmarkClickListener?.onBookmarkClick(position, script)
        }

        return view
    }
} 
package com.tele.teleflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tele.teleflow.R
import com.tele.teleflow.data.Developer

class DeveloperAdapter(
    private val developers: List<Developer>
) : RecyclerView.Adapter<DeveloperAdapter.DeveloperViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeveloperViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_developer, parent, false)
        return DeveloperViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        holder.bind(developers[position])
    }

    override fun getItemCount(): Int = developers.size

    class DeveloperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.developer_name)
        private val roleTextView: TextView = itemView.findViewById(R.id.developer_role)
        private val emailTextView: TextView = itemView.findViewById(R.id.developer_email)

        fun bind(developer: Developer) {
            nameTextView.text = developer.name
            roleTextView.text = developer.role
            emailTextView.text = developer.email
        }
    }
}

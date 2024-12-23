package com.devkitu.inotes.RecyclerView.MyAdapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.devkitu.inotes.ViewNotesActivity.ViewNotesActivity
import com.devkitu.inotes.R
import com.devkitu.inotes.RecyclerView.Users.Users

class MyAdapter(
    private val userlist: ArrayList<Users>,
    private val documentIds: ArrayList<String> // List of document IDs
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_list_theme, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = userlist[position]
        val noteId = documentIds[position]

        holder.noteTitle.text = user.title
        holder.noteDescription.text = user.description

        holder.noteTitle.setOnClickListener {
            val intent = Intent(holder.itemView.context, ViewNotesActivity::class.java)
            intent.putExtra("title", user.title)
            intent.putExtra("description", user.description)
            intent.putExtra("documentId", noteId) // Pass document ID to the next activity
            holder.itemView.context.startActivity(intent)
        }

        holder.noteOpener.setOnClickListener {
            if (holder.noteDescription.visibility == View.GONE) {
                holder.noteDescription.visibility = View.VISIBLE
                holder.noteOpener.setImageResource(R.drawable.expand_less)
            } else {
                holder.noteDescription.visibility = View.GONE
                holder.noteOpener.setImageResource(R.drawable.more_option_icon)
            }
        }
    }

    override fun getItemCount(): Int = userlist.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.note_title)
        val noteDescription: TextView = itemView.findViewById(R.id.note_description)
        val noteOpener: ImageView = itemView.findViewById(R.id.note_opener)
    }
}

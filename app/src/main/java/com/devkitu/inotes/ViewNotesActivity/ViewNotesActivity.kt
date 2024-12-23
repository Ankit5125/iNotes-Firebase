package com.devkitu.inotes.ViewNotesActivity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devkitu.inotes.EditNoteActivity.EditNoteActivity
import com.devkitu.inotes.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewNotesActivity : AppCompatActivity() {

    lateinit var title : TextView
    lateinit var description : TextView
    lateinit var editButton : FloatingActionButton
    lateinit var deleteNoteButton : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_notes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setIds()
        val NoteTitle = intent.getStringExtra("title")
        val NoteDescription = intent.getStringExtra("description")
        val noteId =intent.getStringExtra("documentId")

        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser == null || noteId.isNullOrEmpty()){
            Toast.makeText(this, "User Error... Please Login Again !", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        title.text = NoteTitle
        description.text = NoteDescription

        deleteNoteButton.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("Notes")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("MyNotes")
                .document(noteId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show()
                    finish() // Return to home_activity
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to delete note: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        editButton.setOnClickListener {
            val EditnoteIntent = Intent(this, EditNoteActivity::class.java)
            EditnoteIntent.putExtra("title", NoteTitle)
            EditnoteIntent.putExtra("description", NoteDescription)
            EditnoteIntent.putExtra("documentId", noteId)
            startActivity(EditnoteIntent)
        }
    }

    private fun setIds(){
        title = findViewById(R.id.textView4)
        description = findViewById(R.id.textView5)
        description = findViewById(R.id.textView5)
        editButton = findViewById(R.id.goto_edit_button)
        deleteNoteButton = findViewById(R.id.delete_note_button)
    }
}
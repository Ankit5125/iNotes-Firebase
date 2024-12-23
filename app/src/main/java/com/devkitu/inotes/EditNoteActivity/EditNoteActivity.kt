package com.devkitu.inotes.EditNoteActivity

import android.content.ClipDescription
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.devkitu.inotes.HomePage.home_activity
import com.devkitu.inotes.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditNoteActivity : AppCompatActivity() {

    lateinit var title : EditText
    lateinit var description : EditText
    lateinit var updateButton : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title = findViewById(R.id.edit_note_title)
        description = findViewById(R.id.edit_note_description)
        updateButton = findViewById(R.id.update_note_button)

        val oldTitle = intent.getStringExtra("title")
        val oldDescription = intent.getStringExtra("description")
        val documentId = intent.getStringExtra("documentId")

        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if(oldTitle == null || oldDescription == null || documentId == null || firebaseUser == null){
            Toast.makeText(this, "Error... Please Try Again !", Toast.LENGTH_SHORT).show()
            return
        }

        title.setText(oldTitle)
        description.setText(oldDescription)

        updateButton.setOnClickListener{
            FirebaseFirestore.getInstance()
                .collection("Notes")
                .document(firebaseUser.uid)
                .collection("MyNotes")
                .document(documentId)
                .update("title", title.text.toString(), "description", description.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this, "Note Updated Successfully", Toast.LENGTH_SHORT).show()
                    val gotoHome = Intent(this, home_activity::class.java)
                    gotoHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(gotoHome)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update note: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }
}
package com.devkitu.inotes.CreateNewNote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devkitu.inotes.HomePage.home_activity
import com.devkitu.inotes.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class create_new_note : AppCompatActivity() {

    lateinit var editText_Title : EditText
    lateinit var editText_Description : EditText
    lateinit var upload_note_button : FloatingActionButton
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_new_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editText_Title = findViewById(R.id.new_note_title)
        editText_Description = findViewById(R.id.new_note_description)
        upload_note_button = findViewById(R.id.upload_note_button)
        progressBar = findViewById(R.id.createNewNote_progressbar)

        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val firebaseFirestore = FirebaseFirestore.getInstance()

        if (firebaseUser == null){
            Toast.makeText(this, "User Not Found" , Toast.LENGTH_LONG).show()
            return
        }

        upload_note_button.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            val title = editText_Title.text.toString().trim()
            val description = editText_Description.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please Enter Values in Both Fields", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }
            else{
                val documentRefrance = firebaseFirestore.collection("Notes")
                    .document(firebaseUser.uid)
                    .collection("MyNotes")

                val myMap = mutableMapOf<String, String>()
                myMap["description"] = description
                myMap["title"] = title

                documentRefrance.add(myMap).addOnSuccessListener {
                    Toast.makeText(this, "Note Uploaded Successfully", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                    val goHome = Intent(this, home_activity::class.java)
                    goHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(goHome)
                    finish()
                }.addOnFailureListener{
                    Toast.makeText(this, "Failed to Upload Note", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                }

            }
        }

    }
}
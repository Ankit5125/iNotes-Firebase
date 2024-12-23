package com.devkitu.inotes.HomePage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devkitu.inotes.LoginActivity.login_activity
import com.devkitu.inotes.R
import com.devkitu.inotes.CreateNewNote.create_new_note
import com.devkitu.inotes.LogoutActivity.logout_activity
import com.devkitu.inotes.RecyclerView.MyAdapter.MyAdapter
import com.devkitu.inotes.RecyclerView.Users.Users
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class home_activity : AppCompatActivity() {

    private lateinit var addnotebutton: FloatingActionButton
    private lateinit var accountButton: FloatingActionButton
    private lateinit var notes_in_list: FloatingActionButton
    private lateinit var notes_in_staggered_grid: FloatingActionButton
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Users>
    private lateinit var documentIds: ArrayList<String> // To store document IDs
    private lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setIds()
        setOnClickListeners()
    }

    override fun onResume() {
        super.onResume()
        refreshNotes() // Refresh notes when the activity resumes
    }

    private fun refreshNotes() {
        userArrayList.clear() // Clear existing notes
        documentIds.clear()   // Clear document IDs
        myAdapter.notifyDataSetChanged() // Notify adapter about data reset
        fetchNotes() // Fetch latest notes from Firestore
    }

    private fun setIds() {
        addnotebutton = findViewById(R.id.addnotebutton)
        accountButton = findViewById(R.id.home_activity_account)
        notes_in_list = findViewById(R.id.home_activity_list_notes)
        notes_in_staggered_grid = findViewById(R.id.home_activity_stagger_list)
        recyclerView = findViewById(R.id.recyclerView)

        firebaseUser = FirebaseAuth.getInstance().currentUser ?: run {
            startActivity(Intent(this, login_activity::class.java))
            finish()
            return
        }

        firebaseFirestore = FirebaseFirestore.getInstance()

        userArrayList = arrayListOf()
        documentIds = arrayListOf() // Initialize document ID list
        myAdapter = MyAdapter(userArrayList, documentIds)
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setOnClickListeners() {
        addnotebutton.setOnClickListener {
            startActivity(Intent(this, create_new_note::class.java))
        }

        accountButton.setOnClickListener {
            startActivity(Intent(this, logout_activity::class.java))
        }

        notes_in_list.setOnClickListener {
            recyclerView.layoutManager = LinearLayoutManager(this)
            refreshNotes()
        }

        notes_in_staggered_grid.setOnClickListener {
            staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recyclerView.layoutManager = staggeredGridLayoutManager
            refreshNotes()
        }
    }

    private fun fetchNotes() {
        firebaseFirestore.collection("Notes")
            .document(firebaseUser.uid)
            .collection("MyNotes")
            .orderBy("title")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }

                for (dc in value?.documentChanges ?: emptyList()) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val user = dc.document.toObject(Users::class.java)
                            userArrayList.add(user)
                            documentIds.add(dc.document.id) // Store document ID
                        }
                        DocumentChange.Type.REMOVED -> {
                            val documentId = dc.document.id
                            val index = documentIds.indexOf(documentId)
                            if (index != -1) {
                                userArrayList.removeAt(index)
                                documentIds.removeAt(index)
                                myAdapter.notifyItemRemoved(index)
                            }
                        }
                        // Handle updates if needed
                        else -> {}
                    }
                }
                myAdapter.notifyDataSetChanged()
            }
    }
}

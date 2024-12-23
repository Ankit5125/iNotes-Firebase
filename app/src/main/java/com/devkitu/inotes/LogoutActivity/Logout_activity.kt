package com.devkitu.inotes.LogoutActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devkitu.inotes.LoginActivity.login_activity
import com.devkitu.inotes.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class logout_activity : AppCompatActivity() {

    lateinit var signout_button : MaterialButton
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        signout_button = findViewById(R.id.logout_button)
        progressBar = findViewById(R.id.logout_progressbar)

        signout_button.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logout Successful.", Toast.LENGTH_LONG).show()

            val intent = Intent(this, login_activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            progressBar.visibility = View.GONE

            startActivity(intent)
            finish()
        }


    }
}
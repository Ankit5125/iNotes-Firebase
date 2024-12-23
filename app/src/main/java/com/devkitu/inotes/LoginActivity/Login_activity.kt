package com.devkitu.inotes.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import com.devkitu.inotes.HomePage.home_activity
import com.devkitu.inotes.R
import com.devkitu.inotes.ForgotPasswordActivity.forgot_password_activity
import com.devkitu.inotes.SignupActivity.signup_activity

class login_activity : AppCompatActivity() {

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var login: MaterialButton
    private lateinit var imageView: ImageView
    private lateinit var signupText: TextView
    private lateinit var forgetPassword: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views and FirebaseAuth
        setupIds()
        firebaseAuth = FirebaseAuth.getInstance()

        // Navigate to signup activity
        signupText.setOnClickListener {
            startActivity(Intent(this, signup_activity::class.java))
            finish()
        }

        // Navigate to forgot password activity
        forgetPassword.setOnClickListener {
            startActivity(Intent(this, forgot_password_activity::class.java))
        }

        // Handle login button click
        login.setOnClickListener {
            val userEmail = email.text?.toString()?.trim()
            val userPassword = password.text?.toString()?.trim()

            // Validate email and password
            if (userEmail.isNullOrEmpty() || userPassword.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter valid email and password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser?.isEmailVerified == true) {

                            progressBar.visibility = View.GONE
                            startActivity(Intent(this, home_activity::class.java))
                            finish()
                        } else {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Please verify your email.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this,"Wrong Password / Account Doesn't Exist" , Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Animate UI elements
        imageView.alpha = 0F
        login.alpha = 0F
        imageView.postDelayed({
            imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_animation))
            imageView.alpha = 1F

            login.startAnimation(AnimationUtils.loadAnimation(this, R.anim.move_animation))
            login.alpha = 1F
        }, 500)
    }

    private fun setupIds() {
        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)
        login = findViewById(R.id.login2)
        imageView = findViewById(R.id.login_image)
        signupText = findViewById(R.id.signup_text)
        forgetPassword = findViewById(R.id.forgetPassword)
        progressBar = findViewById(R.id.login_progressbar)
    }
}
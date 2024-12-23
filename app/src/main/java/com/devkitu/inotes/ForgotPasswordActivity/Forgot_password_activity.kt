package com.devkitu.inotes.ForgotPasswordActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devkitu.inotes.MainActivity
import com.devkitu.inotes.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class forgot_password_activity : AppCompatActivity() {


    lateinit var email: TextInputEditText
    lateinit var sendMail: MaterialButton
    lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupIds()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                imageView.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(this,
                        R.anim.fade_in_animation
                    )
                )
                imageView.alpha = 1F

                sendMail.alpha = 1F
                sendMail.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(
                        this,
                        R.anim.move_animation
                    )
                )

            }, 500
        )

        sendMail.setOnClickListener {

            val userEmail = email.text.toString()

            if (userEmail.isNotEmpty()){

                progressBar.visibility = View.VISIBLE
                val firebaseAuth = FirebaseAuth.getInstance()

                firebaseAuth.sendPasswordResetEmail(userEmail).addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Reset Link Sent to Your Email Address",
                        Toast.LENGTH_LONG
                    ).show()

                    progressBar.visibility = View.GONE

                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            else{
                Toast.makeText(this, "Please Enter Valid Email Address", Toast.LENGTH_LONG).show()
            }
        }


    }

    fun setupIds() {
        email = findViewById(R.id.login_email)
        imageView = findViewById(R.id.signup_image)
        sendMail = findViewById(R.id.sendEmail)
        progressBar = findViewById(R.id.forgetPassword_progressbar)
    }
}
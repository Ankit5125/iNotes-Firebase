package com.devkitu.inotes.SignupActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devkitu.inotes.LoginActivity.login_activity
import com.devkitu.inotes.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class signup_activity : AppCompatActivity() {


    lateinit var email: TextInputEditText
    lateinit var password : TextInputEditText
    lateinit var signup : MaterialButton
    lateinit var imageView : ImageView
    lateinit var loginText : TextView
    lateinit var progressBar: ProgressBar
    lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupIds()

        firebaseAuth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                imageView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this,
                    R.anim.fade_in_animation
                ))
                imageView.alpha = 1F

                signup.alpha = 1F
                signup.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this,
                    R.anim.move_animation
                ))

            }, 500)

        loginText.setOnClickListener {
            val goLogin = Intent(this, login_activity::class.java)
            startActivity(goLogin)
            finish()
        }

        signup.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val userEmail = email.text.toString()
            val userPassword = password.text.toString()

            if (!userEmail.equals("") && !userPassword.equals("")){
                if (userPassword.length < 7){
                    Toast.makeText(this, "Password Must Longer Than 7 Characters", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                }
                else{
                    // signup approved
                    firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener {
                        if (firebaseAuth.currentUser != null){
                            Toast.makeText(this, "Account Created Successfully !", Toast.LENGTH_LONG).show()
                            sendEmailVerification()
                            progressBar.visibility = View.GONE
                        }
                        else{
                            Toast.makeText(this, "Failed to Create Account..." , Toast.LENGTH_LONG).show()
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
            else{
                Toast.makeText(this, "Please Enter Proper Details !", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }

        }

    }

    fun setupIds(){
        email = findViewById(R.id.signup_email)
        password = findViewById(R.id.signup_password)
        signup = findViewById(R.id.signup_Button)
        imageView = findViewById(R.id.signup_image)
        loginText = findViewById(R.id.login_text)
        progressBar = findViewById(R.id.signup_progressbar)
    }

    fun sendEmailVerification(){
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener {
                Toast.makeText(this, "Verify Your Email to Login in Account", Toast.LENGTH_LONG).show()
                firebaseAuth.signOut()
                startActivity(Intent(this, login_activity::class.java))
                finish()
            }
        }
        else{
            Toast.makeText(this, "Failed to Send Verification Email", Toast.LENGTH_LONG).show()
        }
    }

}
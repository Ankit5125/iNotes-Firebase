package com.devkitu.inotes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devkitu.inotes.HomePage.home_activity
import com.devkitu.inotes.LoginActivity.login_activity
import com.devkitu.inotes.SignupActivity.signup_activity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var login: MaterialButton
    lateinit var signup: MaterialButton
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setIds()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null){
            startActivity(Intent(this, home_activity::class.java))
            finish()
        }


        val blink = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.blink_anim)
        val fadeIn = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in_animation)
        val moveAnimation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.move_animation)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                imageView.alpha = 1F
                imageView.startAnimation(fadeIn)

                login.alpha = 1F
                login.startAnimation(moveAnimation)

                signup.alpha = 1F
                signup.startAnimation(moveAnimation)
            }, 500)

        signup.setOnClickListener {
            signup.startAnimation(blink)
        }

        login.setOnClickListener{
            login.startAnimation(blink)
            val loginIntent = Intent(this, login_activity::class.java)
            startActivity(loginIntent)
        }

        signup.setOnClickListener {
            signup.startAnimation(blink)
            val signUp = Intent(this, signup_activity::class.java)
            startActivity(signUp)
        }

    }

    fun setIds(){
        login = findViewById(R.id.login)
        signup = findViewById(R.id.signup)
        imageView = findViewById(R.id.imageView)
    }
}


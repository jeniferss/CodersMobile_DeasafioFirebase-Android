package com.example.myapplication.desafiofirebase.splashscreen.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.myapplication.desafiofirebase.R
import com.example.myapplication.desafiofirebase.home.view.HomeActivity
import com.example.myapplication.desafiofirebase.login.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth
        val user = auth.currentUser

        val prefs = getSharedPreferences(APP_NAME, MODE_PRIVATE)
        val prefsChecked = prefs.getBoolean(NOTIFICATION_PREFS, false)

        Handler(Looper.getMainLooper()).postDelayed({

            if(prefsChecked && user != null){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
    }

    companion object{
        const val APP_NAME = "DesafioFirebase"
        const val NOTIFICATION_PREFS = "NOTIFICATION_PREFS"
    }
}

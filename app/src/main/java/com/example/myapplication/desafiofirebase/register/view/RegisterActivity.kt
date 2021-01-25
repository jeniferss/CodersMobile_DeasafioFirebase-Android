package com.example.myapplication.desafiofirebase.register.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.desafiofirebase.R
import com.example.myapplication.desafiofirebase.home.view.HomeActivity
import com.example.myapplication.desafiofirebase.login.view.LoginActivity
import com.google.android.material.button.MaterialButton

class RegisterActivity : AppCompatActivity() {

    private val btnCreateAccount: MaterialButton by lazy {findViewById(R.id.btnCreate)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
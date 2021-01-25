package com.example.myapplication.desafiofirebase.login.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import com.example.myapplication.desafiofirebase.R
import com.example.myapplication.desafiofirebase.home.view.HomeActivity
import com.example.myapplication.desafiofirebase.register.view.RegisterActivity
import com.example.myapplication.desafiofirebase.savegame.view.SaveGameActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private val btnCreateAccount: MaterialButton by lazy { findViewById(R.id.btnCreateAccount) }
    private val btnLogin: MaterialButton by lazy { findViewById(R.id.btnLogin) }
    private val etEmail: TextInputEditText by lazy { findViewById(R.id.etEmailLogin) }
    private val etSenha: TextInputEditText by lazy { findViewById(R.id.etSenhaLogin) }
    private val tilEmail: TextInputLayout by lazy { findViewById(R.id.tilEmailLogin) }
    private val tilSenha: TextInputLayout by lazy { findViewById(R.id.tilSenhaLogin) }
    private val checkBox: CheckBox by lazy { findViewById(R.id.cbRemember) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}
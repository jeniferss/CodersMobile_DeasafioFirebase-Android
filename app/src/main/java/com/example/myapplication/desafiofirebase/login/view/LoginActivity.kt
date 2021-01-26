package com.example.myapplication.desafiofirebase.login.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import com.example.myapplication.desafiofirebase.R
import com.example.myapplication.desafiofirebase.home.view.HomeActivity
import com.example.myapplication.desafiofirebase.register.view.RegisterActivity
import com.example.myapplication.desafiofirebase.savegame.view.SaveGameActivity
import com.example.myapplication.desafiofirebase.splashscreen.view.SplashActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val btnCreateAccount: MaterialButton by lazy { findViewById(R.id.btnCreateAccount) }
    private val btnLogin: MaterialButton by lazy { findViewById(R.id.btnLogin) }
    private val etEmail: TextInputEditText by lazy { findViewById(R.id.etEmailLogin) }
    private val etSenha: TextInputEditText by lazy { findViewById(R.id.etSenhaLogin) }
    private val checkBox: CheckBox by lazy { findViewById(R.id.cbRemember) }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val prefs = getSharedPreferences(SplashActivity.APP_NAME, MODE_PRIVATE)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(SplashActivity.NOTIFICATION_PREFS, isChecked).apply()
        }

        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val senha = etSenha.text.toString()
            login(email, senha)
        }
    }

    private fun camposVazios(email: String, senha: String): Boolean {
        return when {
            email.isEmpty() -> {
                etEmail.error = RegisterActivity.ERRO_VAZIO
                false
            }
            senha.isEmpty() -> {
                etSenha.error = RegisterActivity.ERRO_VAZIO
                false
            }
            else -> {
                true
            }
        }
    }

    private fun autenticarUsuario(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user!!.isEmailVerified) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else Toast.makeText(baseContext, "Valide seu email", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(baseContext, "Email e/ou senha incorretos", Toast.LENGTH_SHORT)
                    .show()
                etEmail.text!!.clear()
                etSenha.text!!.clear()
            }
        }
    }

    private fun login(email: String, senha: String) {
        if (camposVazios(email, senha)) {
            autenticarUsuario(email, senha)
        }
    }
}
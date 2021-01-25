package com.example.myapplication.desafiofirebase.register.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.desafiofirebase.R
import com.example.myapplication.desafiofirebase.home.view.HomeActivity
import com.example.myapplication.desafiofirebase.login.view.LoginActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private val btnCreateAccount: MaterialButton by lazy {findViewById(R.id.btnCreate)}
    private val etEmailRegister: TextInputEditText by lazy { findViewById(R.id.etEmailRegister) }
    private val etSenhaRegister: TextInputEditText by lazy { findViewById(R.id.etSenhaRegister) }
    private val etRepeateSenha: TextInputEditText by lazy { findViewById(R.id.etSenhaRepeateRegister) }
    private val etNomeRegister: TextInputEditText by lazy { findViewById(R.id.etNameRegister) }
    private val tilEmailRegister: TextInputLayout by lazy { findViewById(R.id.tilEmailRegister) }
    private val tilSenhaRegister: TextInputLayout by lazy { findViewById(R.id.tilSenhaRegister) }
    private val tilRepeateSenha: TextInputLayout by lazy { findViewById(R.id.tilSenhaRepeateRegister) }
    private val tilNomeRegister: TextInputLayout by lazy { findViewById(R.id.tilNameRegister) }

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
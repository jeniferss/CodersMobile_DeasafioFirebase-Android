package com.example.myapplication.desafiofirebase.savegame.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.desafiofirebase.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SaveGameActivity : AppCompatActivity() {

    private val btnSave: MaterialButton by lazy {findViewById(R.id.btnSave)}
    private val etNameGame: TextInputEditText by lazy { findViewById(R.id.etNameGame) }
    private val etDataGame: TextInputEditText by lazy { findViewById(R.id.etDataGame) }
    private val etDescriptionGame: TextInputEditText by lazy { findViewById(R.id.etDescriptionGame) }
    private val tilNameGame: TextInputLayout by lazy { findViewById(R.id.tilNameGame) }
    private val tilDataGame: TextInputLayout by lazy { findViewById(R.id.tilDataGame) }
    private val tilDescriptionGame: TextInputLayout by lazy { findViewById(R.id.tilDescriptionGame) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_game)
    }
}
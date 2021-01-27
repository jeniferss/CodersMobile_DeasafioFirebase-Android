package com.example.myapplication.desafiofirebase.savegame.view

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.desafiofirebase.R
import com.example.myapplication.desafiofirebase.detail.view.DetailActivity
import com.example.myapplication.desafiofirebase.game.repository.GameRepository
import com.example.myapplication.desafiofirebase.game.viewmodel.GameViewModel
import com.example.myapplication.desafiofirebase.home.view.HomeActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class SaveGameActivity : AppCompatActivity() {

    private val btnSave: MaterialButton by lazy { findViewById(R.id.btnSave) }
    private val etNameGame: TextInputEditText by lazy { findViewById(R.id.etNameGame) }
    private val etDataGame: TextInputEditText by lazy { findViewById(R.id.etDataGame) }
    private val etDescriptionGame: TextInputEditText by lazy { findViewById(R.id.etDescriptionGame) }
    private val img: CircleImageView by lazy { findViewById(R.id.imgGame) }

    private val database = Firebase.database
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private val storage = Firebase.storage

    private lateinit var _viewModel: GameViewModel

    private lateinit var imgUri: Uri
    private lateinit var imgURL: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_game)

        auth = Firebase.auth

        viewModelProvider()
        addUser(auth.currentUser!!.uid, database)

        edit()

        img.setOnClickListener {
            getImage()
        }


        btnSave.setOnClickListener {
            val name = etNameGame.text.toString()
            val data = etDataGame.text.toString()
            val description = etDescriptionGame.text.toString()

            addGame(ref, name, data, description, imgURL)
        }
    }

    private fun edit() {
        val nomeAtual = intent.getStringExtra("NAMEA")
        val descricaoAtual = intent.getStringExtra("DESCRICAOA")
        val dataAtual = intent.getStringExtra("LANCAMENTOA")

        etNameGame.setText(nomeAtual)
        etDataGame.setText(dataAtual)
        etDescriptionGame.setText(descricaoAtual)
    }

    private fun addUser(userId: String, database: FirebaseDatabase) {
        _viewModel.addUser(userId, database).observe(this, {
            ref = it
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goHome()
    }

    private fun addGame(
        ref: DatabaseReference,
        nome: String,
        data: String,
        description: String,
        imgURL: String
    ) {
        _viewModel.addGame(nome, data, description, imgURL, ref).observe(this, {
            Toast.makeText(
                this@SaveGameActivity,
                "Game salvo com sucesso",
                Toast.LENGTH_SHORT
            ).show()

            goHome()
        })
    }

    private fun viewModelProvider() {
        _viewModel =
            ViewModelProvider(this, GameViewModel.GameViewModelFactory(GameRepository())).get(
                GameViewModel::class.java
            )
    }

    private fun goHome() {
        finish()
    }

    private fun getImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            imgUri = data?.data!!
            img.setImageURI(imgUri)
            sendImg(auth.currentUser!!.uid)
        } else {
            Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendImg(userId: String) {
        imgUri.run {

            val extension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(imgUri))

            val storageRef = storage.getReference("${userId}/imgGames")
            val fileRef = storageRef.child("${Calendar.MILLISECOND}.${extension}")

            fileRef.putFile(imgUri).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {
                    imgURL = it.toString()
                }
            }
        }
    }

    companion object {
        const val CAMERA_REQUEST = 3
    }
}
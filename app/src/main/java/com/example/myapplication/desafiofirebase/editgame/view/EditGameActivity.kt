package com.example.myapplication.desafiofirebase.editgame.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.desafiofirebase.R
import com.example.myapplication.desafiofirebase.game.repository.GameRepository
import com.example.myapplication.desafiofirebase.game.viewmodel.GameViewModel
import com.example.myapplication.desafiofirebase.home.view.HomeActivity
import com.example.myapplication.desafiofirebase.register.view.RegisterActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class EditGameActivity : AppCompatActivity() {

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
    private var imgURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_game)

        val nomeAtual = intent.getStringExtra("NAMEA")
        val descricaoAtual = intent.getStringExtra("DESCRICAOA")
        val dataAtual = intent.getStringExtra("LANCAMENTOA")
        val imgAtual = intent.getStringExtra("URL")

        etNameGame.setText(nomeAtual)
        etDataGame.setText(dataAtual)
        etDescriptionGame.setText(descricaoAtual)
        Picasso.get().load(imgAtual).into(img)

        viewModelProvider()

        auth = Firebase.auth
        val path = nomeAtual!!.toLowerCase(Locale.ROOT) + "-" + dataAtual
        ref = database.getReference(auth.currentUser!!.uid).child(path)
        val refId = database.getReference(auth.currentUser!!.uid)

        img.setOnClickListener {
            getImage()
        }

        btnSave.setOnClickListener {
            val name = etNameGame.text.toString()
            val data = etDataGame.text.toString()
            val description = etDescriptionGame.text.toString()

            if (camposVazios(name, data, description)) {
                if (imgURL.isEmpty()) {
                    imgURL = imgAtual!!
                }
                editGame(ref, name, data, description, imgURL, refId)
            }
        }
    }

    private fun camposVazios(
        nome: String,
        data: String,
        description: String
    ): Boolean {
        return if (nome.isEmpty()) {
            etNameGame.error = RegisterActivity.ERRO_VAZIO
            false
        } else if (data.isEmpty()) {
            etDataGame.error = RegisterActivity.ERRO_VAZIO
            false
        } else if (description.isEmpty()) {
            etDescriptionGame.error = RegisterActivity.ERRO_VAZIO
            false
        } else {
            true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun editGame(
        ref: DatabaseReference,
        nome: String,
        data: String,
        description: String,
        imgURL: String, refId: DatabaseReference
    ) {
        _viewModel.editGame(nome, data, description, imgURL, ref, refId).observe(this, {
            Toast.makeText(
                this@EditGameActivity,
                "Game alterado com sucesso",
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
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
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
            sendNewImg(auth.currentUser!!.uid)
        } else {
            Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendNewImg(userId: String) {
        imgUri.run {

            val extension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(imgUri))

            val storageRef = storage.getReference("${userId}/imgGames")
            val fileRef = storageRef.child("${System.currentTimeMillis()}.${extension}")

            fileRef.putFile(imgUri)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener {
                        imgURL = it.toString()
                    }
                        .addOnFailureListener {
                            imgURL =
                                "https://www.solidbackgrounds.com/images/1024x600/1024x600-black-solid-color-background.jpg"
                            Toast.makeText(
                                this@EditGameActivity,
                                "Erro ao salvar imagem",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@EditGameActivity,
                        "Erro ao salvar imagem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    companion object {
        const val CAMERA_REQUEST = 4
    }
}
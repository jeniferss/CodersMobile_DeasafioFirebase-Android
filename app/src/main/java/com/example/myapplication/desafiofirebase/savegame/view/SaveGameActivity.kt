package com.example.myapplication.desafiofirebase.savegame.view

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.desafiofirebase.R
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


class SaveGameActivity : AppCompatActivity() {

    private val btnSave: MaterialButton by lazy {findViewById(R.id.btnSave)}
    private val etNameGame: TextInputEditText by lazy { findViewById(R.id.etNameGame) }
    private val etDataGame: TextInputEditText by lazy { findViewById(R.id.etDataGame) }
    private val etDescriptionGame: TextInputEditText by lazy { findViewById(R.id.etDescriptionGame) }
    private val img: CircleImageView by lazy {findViewById(R.id.imgGame)}

    private lateinit var _viewModel: GameViewModel
    private val database = Firebase.database
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private val storage = Firebase.storage
    private lateinit var imgUri: Uri
    private lateinit var imgUrl: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_game)

        auth = Firebase.auth

        viewModelProvider()
        addUser(auth.currentUser!!.uid, database)

        img.setOnClickListener {
            getImage()
        }

        btnSave.setOnClickListener {
            val name = etNameGame.text.toString()
            val data = etDataGame.text.toString()
            val description = etDescriptionGame.text.toString()

            addGame(ref, name, data, description, "")
            goHome()
        }
    }

    private fun addUser(userId: String, database: FirebaseDatabase){
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
    ){
        _viewModel.addGame(nome, data, description, imgURL, ref).observe(this, {
            if (it) Toast.makeText(
                this@SaveGameActivity,
                "Game salvo com sucesso",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    private fun viewModelProvider() {
        _viewModel =
            ViewModelProvider(this, GameViewModel.GameViewModelFactory(GameRepository())).get(
                GameViewModel::class.java
            )
    }

    private fun goHome(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            _viewModel.showImg(data?.data!!, img)
            imgUri = data.data!!
        }
    }

    private fun sendAndGetImg(userId: String, nameGame: String, imgURI: Uri, firebaseStorage: FirebaseStorage, contentResolver: ContentResolver, circleImageView: CircleImageView){
        _viewModel.addImg(userId, nameGame, imgURI, firebaseStorage, contentResolver, circleImageView).observe(this, {
            imgUrl = it
        })
    }

    companion object {
        const val CAMERA_REQUEST = 3
    }
}
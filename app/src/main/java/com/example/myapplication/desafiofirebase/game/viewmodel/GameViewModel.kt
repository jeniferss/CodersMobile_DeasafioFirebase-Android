package com.example.myapplication.desafiofirebase.game.viewmodel

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.myapplication.desafiofirebase.game.model.GameModel
import com.example.myapplication.desafiofirebase.game.repository.GameRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers

class GameViewModel(private val repository: GameRepository): ViewModel() {

    fun addUser(userId: String, databse: FirebaseDatabase) = liveData(Dispatchers.IO){
        repository.addUser(userId, databse)
        emit(repository.addUser(userId, databse))
    }


    fun addGame(nome: String, data: String, description: String, imgURL: String, ref: DatabaseReference) = liveData(Dispatchers.IO) {
        repository.addGame(ref, GameModel(nome, data, description, imgURL))
        emit(true)
    }

    fun addImg(userId: String, nameGame: String, imgURI: Uri, firebaseStorage: FirebaseStorage, contentResolver: ContentResolver, circleImageView: CircleImageView) = liveData(Dispatchers.IO) {
        val imgUrl = repository.sendImg(userId, nameGame, imgURI, firebaseStorage, contentResolver, circleImageView)
        emit(imgUrl)
    }

    fun showImg(imgURI: Uri, circleImageView: CircleImageView) = liveData(Dispatchers.IO){
        repository.showImg(imgURI, circleImageView)
        emit(true)
    }

    class GameViewModelFactory(private val repository: GameRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GameViewModel(repository) as T
        }
    }

}
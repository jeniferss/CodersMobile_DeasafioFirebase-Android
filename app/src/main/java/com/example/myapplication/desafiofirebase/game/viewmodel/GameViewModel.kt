package com.example.myapplication.desafiofirebase.game.viewmodel

import android.content.ContentResolver
import android.content.Context
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
import kotlinx.coroutines.delay
import java.lang.Thread.sleep

class GameViewModel(private val repository: GameRepository): ViewModel() {

    private val _gamesBeforeSearch = mutableListOf<GameModel>()
    private val _games = mutableListOf<GameModel>()

    fun addUser(userId: String, databse: FirebaseDatabase) = liveData(Dispatchers.IO){
        repository.addUser(userId, databse)
        emit(repository.addUser(userId, databse))
    }

    fun addGame(nome: String, data: String, description: String, imgURL: String, ref: DatabaseReference) = liveData(Dispatchers.IO) {
        repository.addGame(ref, GameModel(nome, data, description, imgURL))
        emit(true)
    }

    fun getGames(ref: DatabaseReference, context: Context, list: MutableList<GameModel>) = liveData(Dispatchers.IO){
        val listGame = repository.getGames(ref, context, list)
        emit(listGame as List<GameModel>)
    }

    fun searchByName(string: String) = liveData(Dispatchers.IO){
        emit(true)
    }

    class GameViewModelFactory(private val repository: GameRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GameViewModel(repository) as T
        }
    }

}
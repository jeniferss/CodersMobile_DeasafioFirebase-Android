package com.example.myapplication.desafiofirebase.game.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.myapplication.desafiofirebase.game.model.GameModel
import com.example.myapplication.desafiofirebase.game.repository.GameRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers

class GameViewModel(private val repository: GameRepository): ViewModel() {

    private var _games = mutableListOf<GameModel>()

    fun addUser(userId: String, databse: FirebaseDatabase) = liveData(Dispatchers.IO){
        repository.addUser(userId, databse)
        emit(repository.addUser(userId, databse))
    }

    fun addGame(nome: String, data: String, description: String, imgURL: String, ref: DatabaseReference) = liveData(Dispatchers.IO) {
        repository.addGame(ref, GameModel(nome, data, description, imgURL))
        emit(true)
    }

    fun editGame(nome: String, data: String, description: String, imgURL: String, ref: DatabaseReference, refUser: DatabaseReference) = liveData(Dispatchers.IO) {
        repository.editGame(ref, GameModel(nome, data, description, imgURL), refUser)
        emit(true)
    }

    fun getGames(ref: DatabaseReference, context: Context, list: MutableList<GameModel>) = liveData(Dispatchers.IO){
        val listGames = repository.getGames(ref, context, list)
        _games.addAll(listGames)
        emit(listGames as List<GameModel>)
    }

    fun searchByName(string: String, ref: DatabaseReference) = liveData(Dispatchers.IO){
        val response = repository.searchByName(string, ref)
        emit(response)
    }

    class GameViewModelFactory(private val repository: GameRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GameViewModel(repository) as T
        }
    }

}
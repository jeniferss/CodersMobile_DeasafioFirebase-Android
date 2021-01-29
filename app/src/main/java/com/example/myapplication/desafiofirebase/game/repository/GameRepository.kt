package com.example.myapplication.desafiofirebase.game.repository

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.desafiofirebase.game.model.GameModel
import com.google.firebase.database.*
import kotlinx.coroutines.delay
import java.util.*

class GameRepository {

    fun addGame(ref: DatabaseReference, gameModel: GameModel) {
        val path = gameModel.nome.toLowerCase(Locale.ROOT) + "-" + gameModel.dataLancamento
        val newGame = ref.child(path)
        newGame.setValue(gameModel)
    }

    fun editGame(ref: DatabaseReference, gameModel: GameModel, refUser: DatabaseReference) {
        ref.removeValue()
        addGame(refUser, gameModel)
    }

    suspend fun getGames(
        ref: DatabaseReference,
        context: Context,
        list: MutableList<GameModel>
    ): MutableList<GameModel> {

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    val game: GameModel? = dataSnapshot1.getValue(GameModel::class.java)
                    list.add(game!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar lista", Toast.LENGTH_SHORT).show()
            }
        })
        delay(1500)
        return list
    }

    fun addUser(userId: String, databse: FirebaseDatabase): DatabaseReference {
        return databse.getReference(userId)
    }

    suspend fun searchByName(
        string: String,
        ref: DatabaseReference,
        recyclerView: RecyclerView,
        linearLayout: LinearLayout
    ): GameModel? {

        val path = string.toLowerCase(Locale.ROOT)
        val query = ref.child(path)
        var game: GameModel? = GameModel()

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    game = snapshot.getValue(GameModel::class.java)
                } else {
                    recyclerView.visibility = View.GONE
                    linearLayout.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        delay(1500)
        return game
    }
}
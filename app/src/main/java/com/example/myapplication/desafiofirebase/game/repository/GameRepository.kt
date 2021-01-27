package com.example.myapplication.desafiofirebase.game.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.SearchView
import android.widget.Toast
import com.example.myapplication.desafiofirebase.game.model.GameModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.delay
import java.lang.Thread.sleep


class GameRepository {

    fun addGame(ref: DatabaseReference, gameModel: GameModel) {
        val newGame = ref.child(gameModel.nome)
        newGame.setValue(gameModel)
    }

    fun editGame(ref: DatabaseReference, gameModel: GameModel){
        ref.setValue(gameModel)
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
        val ref = databse.getReference(userId)
        return ref
    }

    suspend fun searchByName(nome: String, ref: DatabaseReference, list: MutableList<GameModel>, context: Context): MutableList<GameModel> {
        val queryCamp = "nome"

        val query = ref.child(nome).orderByChild(queryCamp).equalTo(nome)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot1 in snapshot.children) {
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
}
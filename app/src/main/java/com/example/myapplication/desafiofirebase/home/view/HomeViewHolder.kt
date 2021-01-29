package com.example.myapplication.desafiofirebase.home.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.desafiofirebase.R
import com.example.myapplication.desafiofirebase.game.model.GameModel
import com.squareup.picasso.Picasso

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val imgGame = view.findViewById<ImageView>(R.id.imgCardPersonagem)
    private val nomeGame = view.findViewById<TextView>(R.id.tvNameGame)
    private val dataGame = view.findViewById<TextView>(R.id.tvDataAno)

    fun bind(gameModel: GameModel) {
        nomeGame.text = gameModel.nome
        dataGame.text = gameModel.dataLancamento
        Picasso.get().load(gameModel.imgUrl).into(imgGame)
    }
}
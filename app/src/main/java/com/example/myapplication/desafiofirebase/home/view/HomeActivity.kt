package com.example.myapplication.desafiofirebase.home.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.desafiofirebase.R
import com.example.myapplication.desafiofirebase.detail.view.DetailActivity
import com.example.myapplication.desafiofirebase.game.model.GameModel
import com.example.myapplication.desafiofirebase.game.repository.GameRepository
import com.example.myapplication.desafiofirebase.game.viewmodel.GameViewModel
import com.example.myapplication.desafiofirebase.savegame.view.SaveGameActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeActivity : AppCompatActivity() {

    private val searchView: SearchView by lazy { findViewById(R.id.searchView) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerGames) }
    private val btnNewGame: FloatingActionButton by lazy { findViewById(R.id.btnNewGame) }

    private lateinit var _homeAdapter: HomeAdapter
    private lateinit var _viewModel: GameViewModel

    private val _gameList = mutableListOf<GameModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val manager = GridLayoutManager(this, 2)

        viewModelProvider()
        setUpNavigation()
        setUpRecyclerView(recyclerView, manager)

        btnNewGame.setOnClickListener {
            val intent = Intent(this, SaveGameActivity::class.java)
            startActivity(intent)
        }

    }


    private fun setUpRecyclerView(
        recyclerView: RecyclerView,
        viewLayoutManager: GridLayoutManager
    ) {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = _homeAdapter
        }
    }

    private fun setUpNavigation() {
        _homeAdapter = HomeAdapter(_gameList) {
            val intent = Intent(this@HomeActivity, DetailActivity::class.java)
            intent.putExtra("NAME", it.nome)
            intent.putExtra("LANCAMENTO", it.dataLancamento)
            intent.putExtra("DESCRICAO", it.descricao)
            intent.putExtra("IMG_URL", it.imgUrl)
            startActivity(intent)
        }
    }

    private fun viewModelProvider() {
        _viewModel =
            ViewModelProvider(this, GameViewModel.GameViewModelFactory(GameRepository())).get(
                GameViewModel::class.java
            )
    }
}
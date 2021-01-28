package com.example.myapplication.desafiofirebase.home.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.desafiofirebase.R
import com.example.myapplication.desafiofirebase.detail.view.DetailActivity
import com.example.myapplication.desafiofirebase.game.model.GameModel
import com.example.myapplication.desafiofirebase.game.repository.GameRepository
import com.example.myapplication.desafiofirebase.game.viewmodel.GameViewModel
import com.example.myapplication.desafiofirebase.login.view.LoginActivity
import com.example.myapplication.desafiofirebase.savegame.view.SaveGameActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay


class HomeActivity : AppCompatActivity() {

    private val searchView: SearchView by lazy { findViewById(R.id.searchView) }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy { findViewById(R.id.swipeToRefresh) }

    private val noResult: LinearLayout by lazy {findViewById(R.id.noResult)}

    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerGames) }
    private val btnNewGame: FloatingActionButton by lazy { findViewById(R.id.btnNewGame) }

    private lateinit var _homeAdapter: HomeAdapter
    private lateinit var _viewModel: GameViewModel

    private lateinit var ref: DatabaseReference
    private var databse = FirebaseDatabase.getInstance()
    private lateinit var auth: FirebaseAuth

    private val _gameList = mutableListOf<GameModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = Firebase.auth
        ref = databse.getReference(auth.currentUser!!.uid)

        searchView.queryHint = "ex: Game-Ano"

        val manager = GridLayoutManager(this, 2)

        viewModelProvider()
        refresh()
        setUpNavigation()
        setUpRecyclerView(recyclerView, manager)
        getGames(ref, this, _gameList)

        btnNewGame.setOnClickListener {
            val intent = Intent(this, SaveGameActivity::class.java)
            startActivity(intent)
        }

        searchByName()
    }

    private fun refresh() {
        swipeRefreshLayout.setOnRefreshListener {
            SwipeRefreshLayout.OnRefreshListener {
                _gameList.clear()
            }
            getGames(ref, this@HomeActivity, _gameList)
            swipeRefreshLayout.isRefreshing = false
            _homeAdapter.notifyDataSetChanged()
        }
    }

    private fun getGames(ref: DatabaseReference, context: Context, list: List<GameModel>) {
        _gameList.clear()
        _viewModel.getGames(ref, context, _gameList).observe(this, {
            list.let {_gameList.addAll(it)}
            _homeAdapter.notifyDataSetChanged()
        })
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

    private fun searchByName(){

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                _viewModel.searchByName(query!!,  ref, recyclerView, noResult).observe(this@HomeActivity, {
                    if(isFound(it!!)){
                        _gameList.clear()
                        _gameList.add(it!!)
                        _homeAdapter.notifyDataSetChanged()
                    }
                })
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    recyclerView.visibility = View.VISIBLE
                    noResult.visibility = View.GONE
                    _gameList.clear()
                    getGames(ref, this@HomeActivity, _gameList)
                    _homeAdapter.notifyDataSetChanged()
                }
                return false
            }
        })
    }

    private fun isFound(gameModel: GameModel): Boolean {
        if(gameModel.nome.isEmpty()){
            recyclerView.visibility = View.GONE
            noResult.visibility = View.VISIBLE
            return false
        }
        return true
    }
}
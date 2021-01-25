package com.example.myapplication.desafiofirebase.home.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.desafiofirebase.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeActivity : AppCompatActivity() {

    private val searchView: SearchView by lazy { findViewById(R.id.searchView) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerGames) }
    private val btnNewGame: FloatingActionButton by lazy { findViewById(R.id.btnNewGame) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}
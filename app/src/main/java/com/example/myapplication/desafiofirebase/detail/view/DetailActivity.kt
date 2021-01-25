package com.example.myapplication.desafiofirebase.detail.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.desafiofirebase.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailActivity : AppCompatActivity() {

    private val btnEditGame: FloatingActionButton by lazy { findViewById(R.id.btnEditGame) }
    private val btnBack: ImageView by lazy { findViewById(R.id.btnBack) }
    private val imgGameDetail: ImageView by lazy { findViewById(R.id.imgGameDetail) }
    private val tvNomeDetail: TextView by lazy { findViewById(R.id.tvNameGameDetalhes) }
    private val tvSecondNameDetail: TextView by lazy { findViewById(R.id.tvSecondNameGameDetalhes) }
    private val tvAnoDetalhe: TextView by lazy { findViewById(R.id.tvAnoDetalhe) }
    private val tvDescricaoDetalhe: TextView by lazy { findViewById(R.id.tvDescription) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }
}
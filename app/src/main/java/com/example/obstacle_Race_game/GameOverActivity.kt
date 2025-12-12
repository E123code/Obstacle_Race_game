package com.example.obstacle_Race_game

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class GameOverActivity: AppCompatActivity() {

    private lateinit var gameOver_LBL_title: MaterialTextView

    private lateinit var gameOver_BTN_newGame: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_over)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()
    }

    private fun findViews() {
        gameOver_LBL_title = findViewById(R.id.gameOver_LBL_title)
        gameOver_BTN_newGame = findViewById(R.id.gameOver_BTN_newGame)
    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras

        val message = bundle?.getString("MESSAGE","🤷🏻‍♂️ Unknown Status!")

        gameOver_LBL_title.text = buildString {
            append(message)
        }
        gameOver_BTN_newGame.setOnClickListener { v: View ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
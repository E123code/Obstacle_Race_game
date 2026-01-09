package com.example.obstacle_Race_game

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obstacle_Race_game.databinding.ActivityLeaderBoardBinding
import com.example.obstacle_Race_game.interfaces.Callback_HighScore_clicked

class LeaderBoardActivity : AppCompatActivity(), Callback_HighScore_clicked {

    private lateinit var binding: ActivityLeaderBoardBinding
    private lateinit var highScoresFragment: HighScoresFragment
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
    }

    private fun initViews(){
        mapFragment = MapFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.leaderboard_FRAME_map,mapFragment)
            .commit()

        highScoresFragment = HighScoresFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.leaderboard_FRAME_list, highScoresFragment)
            .commit()


    }

    override fun highScoreItemClicked(lat: Double, lon: Double) {

    }


}
package com.example.obstacle_Race_game

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obstacle_Race_game.databinding.ActivityStartBinding
import com.example.obstacle_Race_game.utilities.Constants
import com.example.obstacle_Race_game.utilities.ImageLoader

class StartActivity : AppCompatActivity() {


    private lateinit var binding: ActivityStartBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
    }


    private fun initViews() {

        binding.BTNModeButton.setOnClickListener {startGame(Constants.BundleKeys.BUTTONS)
        }

        binding.BTNSensorMode.setOnClickListener{startGame(Constants.BundleKeys.SENSORS)}

        ImageLoader.getInstance()
            .loadImage(
                R.drawable.roads,
                binding.startIMGBackground
            )


        binding.BTNLeaderboard.setOnClickListener {
            val intent = Intent(this, LeaderBoardActivity::class.java)
            startActivity(intent)
        }




    }


    private fun startGame(mode: String){
        val intent = Intent(this, MainActivity::class.java)
        var bundle = Bundle()
        bundle.putString(Constants.BundleKeys.CONTROL_MODE,mode)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }




}
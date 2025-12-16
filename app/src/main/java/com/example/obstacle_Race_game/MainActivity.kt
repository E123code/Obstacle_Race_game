package com.example.obstacle_Race_game

import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.obstacle_Race_game.utilities.Constants
import com.example.obstacle_Race_game.utilities.GameListener
import com.example.obstacle_Race_game.utilities.SignalManager
import com.example.obstacle_Race_game.logic.GameManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),GameListener{

    private var timerOn: Boolean = false
    private lateinit var main_FAB_right : ExtendedFloatingActionButton

    private lateinit var main_FAB_left : ExtendedFloatingActionButton

    private lateinit var  obstaclesGrid : Array<Array<AppCompatImageView>>

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_IMG_cars : Array<AppCompatImageView>

    private lateinit var gameLoopJob: Job

    private lateinit var gameManager : GameManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        gameManager = GameManager(this)
        initViews()
    }


    private  fun findViews(){
        main_FAB_right = findViewById(R.id.main_FAB_right)
        main_FAB_left = findViewById(R.id.main_FAB_left)
        main_IMG_hearts = arrayOf(
            findViewById(R.id.Main_IMG_heart1) ,
            findViewById(R.id.Main_IMG_heart2) ,
            findViewById(R.id.Main_IMG_heart3)
            )
        main_IMG_cars = arrayOf(
            findViewById(R.id.Main_carLeft),
            findViewById(R.id.Main_carCenter),
            findViewById(R.id.Main_carRight)
        )

        obstaclesGrid = arrayOf(
            arrayOf(
                findViewById(R.id.IMG_box1),
                findViewById(R.id.IMG_box2),
                findViewById(R.id.IMG_box3)
            ),
            arrayOf(
                findViewById(R.id.IMG_box4),
                findViewById(R.id.IMG_box5),
                findViewById(R.id.IMG_box6)
            ),
            arrayOf(
                findViewById(R.id.IMG_box7),
                findViewById(R.id.IMG_box8),
                findViewById(R.id.IMG_box9)
            ),
            arrayOf(
                findViewById(R.id.IMG_box10),
                findViewById(R.id.IMG_box11),
                findViewById(R.id.IMG_box12)
            ),
            arrayOf(
                findViewById(R.id.IMG_box13),
                findViewById(R.id.IMG_box14),
                findViewById(R.id.IMG_box15)
            ),
            arrayOf(
                findViewById(R.id.IMG_box16),
                findViewById(R.id.IMG_box17),
                findViewById(R.id.IMG_box18)
            ),
            arrayOf(
                findViewById(R.id.IMG_box19),
                findViewById(R.id.IMG_box20),
                findViewById(R.id.IMG_box21)
            )
        )
    }

    private fun initViews(){
        main_FAB_right.setOnClickListener {v: View -> gameManager.moveCar(1)}
        main_FAB_left.setOnClickListener {v: View -> gameManager.moveCar(-1)}
        // When the activity resumes, the game loop should start.
        gameManager.resetGame()


    }


    private fun startGameLoop(){
        if (!timerOn && gameManager.isGameRunning ) {
            timerOn = true

            gameLoopJob = lifecycleScope.launch {
                while (gameManager.isGameRunning){
                    val currentTime = System.currentTimeMillis()
                    Log.d("Timer Runnable", "" + currentTime)

                    gameManager.onGameTick(this)

                    delay(Constants.GameLogic.GAME_TICK_MS)

                }
            }
        }
    }

    private fun stopGameLoop(){
        if(!timerOn) return
        timerOn = false
        gameLoopJob.cancel()
    }

    override fun onPause() {
        super.onPause()
        Log.d("Game Status","On Pause!")
        stopGameLoop()
    }

    override fun onResume() {
        super.onResume()
        Log.d("Game Status","On Resume!")
        if(gameManager.isGameRunning){
            startGameLoop()
        }
    }

    override fun onMatrixUpdate(grid: List<IntArray>) {
        var rows = Constants.GameLogic.ROAD_DEPTH
        var cols = Constants.GameLogic.LANES
        for (row in 0 until rows){
            val row_index = rows - 1 - row
            for (col in 0 until cols){
                val currentObs = obstaclesGrid[row_index][col]
                currentObs.visibility = if (grid[row][col] == 1) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    override fun onLaneChange(newLaneIndex: Int) {
        for (i in 0 until Constants.GameLogic.LANES){
            main_IMG_cars[i].visibility = if(i == newLaneIndex) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun onCollision() {
        SignalManager.getInstance().toast("you Crashed!", SignalManager.ToastLength.SHORT)
        SignalManager.getInstance().vibrate()
        if (gameManager.collisions!= 0){
            main_IMG_hearts[main_IMG_hearts.size - gameManager.collisions]
                .visibility = View.INVISIBLE
        }

    }

    override fun onGameOver() {
        stopGameLoop()
        Log.d("Game Status","Game over!")
        for(car in main_IMG_cars) { car.visibility = View.GONE}
        for(row in obstaclesGrid){
            for( obstacle  in  row){
                obstacle.visibility = View.GONE
            }
        }
        changeActivity("😭Game Over!")
    }


    private fun changeActivity(message: String){
        val intent = Intent(this, GameOverActivity::class.java)
        var bundle = Bundle()
        bundle.putString("MESSAGE", message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

}
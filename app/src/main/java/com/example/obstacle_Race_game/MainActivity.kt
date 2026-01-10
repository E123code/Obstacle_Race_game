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
import com.example.obstacle_Race_game.interfaces.GameListener
import com.example.obstacle_Race_game.interfaces.TiltCallback
import com.example.obstacle_Race_game.utilities.SignalManager
import com.example.obstacle_Race_game.logic.GameManager
import com.example.obstacle_Race_game.model.RecordManager
import com.example.obstacle_Race_game.utilities.SingleSoundPlayer
import com.example.obstacle_Race_game.utilities.TiltDetector
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
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

    private lateinit var main_LBL_score: MaterialTextView

    private lateinit var tiltDetector: TiltDetector

    private lateinit var controlContainer: View

    private var controlMode: String = Constants.BundleKeys.BUTTONS

    private var lastLaneChangeTime: Long = 0L
    private var currentGameTickDelay: Long = Constants.GameLogic.GAME_TICK_MS

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

        main_LBL_score = findViewById(R.id.main_LBL_score)

        controlContainer = findViewById(R.id.control_container)

        main_IMG_hearts = arrayOf(
            findViewById(R.id.Main_IMG_heart1) ,
            findViewById(R.id.Main_IMG_heart2) ,
            findViewById(R.id.Main_IMG_heart3)
            )
        main_IMG_cars = arrayOf(
            findViewById(R.id.Main_car0),
            findViewById(R.id.Main_car1),
            findViewById(R.id.Main_car2),
            findViewById(R.id.Main_car3),
            findViewById(R.id.Main_car4)

        )

        obstaclesGrid = arrayOf(
            arrayOf(
                findViewById(R.id.IMG_box1),
                findViewById(R.id.IMG_box2),
                findViewById(R.id.IMG_box3),
                findViewById(R.id.IMG_box4),
                findViewById(R.id.IMG_box5)
            ),
            arrayOf(
                findViewById(R.id.IMG_box6),
                findViewById(R.id.IMG_box7),
                findViewById(R.id.IMG_box8),
                findViewById(R.id.IMG_box9),
                findViewById(R.id.IMG_box10)
            ),
            arrayOf(
                findViewById(R.id.IMG_box11),
                findViewById(R.id.IMG_box12),
                findViewById(R.id.IMG_box13),
                findViewById(R.id.IMG_box14),
                findViewById(R.id.IMG_box15)
            ),
            arrayOf(
                findViewById(R.id.IMG_box16),
                findViewById(R.id.IMG_box17),
                findViewById(R.id.IMG_box18),
                findViewById(R.id.IMG_box19),
                findViewById(R.id.IMG_box20)
            ),
            arrayOf(
                findViewById(R.id.IMG_box21),
                findViewById(R.id.IMG_box22),
                findViewById(R.id.IMG_box23),
                findViewById(R.id.IMG_box24),
                findViewById(R.id.IMG_box25)
            ),
            arrayOf(
                findViewById(R.id.IMG_box26),
                findViewById(R.id.IMG_box27),
                findViewById(R.id.IMG_box28),
                findViewById(R.id.IMG_box29),
                findViewById(R.id.IMG_box30)
            ),
            arrayOf(
                findViewById(R.id.IMG_box31),
                findViewById(R.id.IMG_box32),
                findViewById(R.id.IMG_box33),
                findViewById(R.id.IMG_box34),
                findViewById(R.id.IMG_box35)
            ),
            arrayOf(
                findViewById(R.id.IMG_box36),
                findViewById(R.id.IMG_box37),
                findViewById(R.id.IMG_box38),
                findViewById(R.id.IMG_box39),
                findViewById(R.id.IMG_box40),
            ),
            arrayOf(
                findViewById(R.id.IMG_box41),
                findViewById(R.id.IMG_box42),
                findViewById(R.id.IMG_box43),
                findViewById(R.id.IMG_box44),
                findViewById(R.id.IMG_box45)
            ),

        )
    }

    private fun initViews(){
        val bundle: Bundle? = intent.extras

        main_LBL_score.text = gameManager.score.toString()

        controlMode = bundle?.getString(Constants.BundleKeys.CONTROL_MODE)?: Constants.BundleKeys.BUTTONS

        if(controlMode == Constants.BundleKeys.SENSORS) {
            controlContainer.visibility = View.GONE
            initTiltDetector()
        }else{
            controlContainer.visibility = View.VISIBLE
            main_FAB_right.setOnClickListener {v: View -> gameManager.moveCar(1)}
            main_FAB_left.setOnClickListener {v: View -> gameManager.moveCar(-1)}
        }

        // When the activity resumes, the game loop should start.
        gameManager.resetGame()


    }

    /**
     * initialize the tilt detector
     */
    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            this,
            object : TiltCallback {
                override fun onTiltX(x: Float)
                {
                    handleMovement(x)
                }

                override fun onTiltY(y: Float) {
                   handleSpeed(y)
                }



            }
        )
    }

    /**
     * handle lane change on sensors mode on tilting left/right
     */
    private fun handleMovement(x: Float){
        if (System.currentTimeMillis() - lastLaneChangeTime >= Constants.GameLogic.LANE_CHANGE_DELAY_MS) {
            if (x >= 3.0) {// moving left when tilting left
                gameManager.moveCar(-1)
                lastLaneChangeTime = System.currentTimeMillis()
            } else if (x <= -3.0) {// moving right when tilting right
                gameManager.moveCar(1)
                lastLaneChangeTime = System.currentTimeMillis()
            }
        }

    }

    /**
     * handle the speed in sensors mode on tilting the phone towards/ backwards
     */

    private fun handleSpeed(y:Float){
        currentGameTickDelay = when {
            y < -2.0 -> currentGameTickDelay - Constants.GameLogic.TICK_STEP_MS//increases speed with each tilt forwards
            y > 2.0f -> currentGameTickDelay + Constants.GameLogic.TICK_STEP_MS// decreases speed with each tilt backwards
            else -> Constants.GameLogic.GAME_TICK_MS
        }.coerceIn(Constants.GameLogic.MIN_TICK_MS, Constants.GameLogic.MAX_TICK_MS)
    }

    private fun startGameLoop(){
        if (!timerOn && gameManager.isGameRunning ) {
            timerOn = true
            gameManager.resetInvulnerability()
            gameLoopJob = lifecycleScope.launch {
                while (gameManager.isGameRunning){
                    val currentTime = System.currentTimeMillis()
                    Log.d("Timer Runnable", "" + currentTime)

                    gameManager.onGameTick(this)

                    delay(currentGameTickDelay)

                }
            }
        }
    }

    private fun stopGameLoop(){
        if(!timerOn) return
        gameManager.resetInvulnerability()
        timerOn = false
        gameLoopJob.cancel()
    }

    override fun onPause() {
        super.onPause()
        Log.d("Game Status","On Pause!")
        if(controlMode == Constants.BundleKeys.SENSORS) {
            tiltDetector.stop()
        }
        stopGameLoop()
    }

    override fun onResume() {
        super.onResume()
        Log.d("Game Status","On Resume!")
        if(controlMode == Constants.BundleKeys.SENSORS) {
            tiltDetector.start()
        }
        if(gameManager.isGameRunning){
            startGameLoop()
        }
    }

    override fun onScoreUpdate(score: Int) {
        main_LBL_score.text = score.toString()
    }

    override fun onMatrixUpdate(grid: List<IntArray>) {
        var rows = Constants.GameLogic.ROAD_DEPTH
        var cols = Constants.GameLogic.LANES
        val boxP = resources.getDimensionPixelSize(R.dimen.box_padding)
        val coinP = resources.getDimensionPixelSize(R.dimen.coin_padding)
        for (row in 0 until rows){
            val row_index = rows - 1 - row
            for (col in 0 until cols){
                val itemType = grid[row][col]
                val currentObs = obstaclesGrid[row_index][col]
                when(itemType){
                    Constants.ItemTypes.EMPTY->{
                        currentObs.visibility = View.INVISIBLE
                    }
                    Constants.ItemTypes.OBSTACLE->{
                        currentObs.visibility = View.VISIBLE
                        currentObs.setImageResource(R.drawable.box)
                        currentObs.setPadding(boxP,boxP,boxP,boxP)
                    }
                    Constants.ItemTypes.COIN->{
                        currentObs.visibility = View.VISIBLE
                        currentObs.setImageResource(R.drawable.coin)
                        currentObs.setPadding(coinP,coinP,coinP,coinP)
                    }
                }
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
        val ssp = SingleSoundPlayer(this)
        ssp.playSound(R.raw.car_crash_sound_effect)
    }

    override fun onLivesUpdate() {
        val livesLeft = main_IMG_hearts.size- gameManager.collisions
        for (i in main_IMG_hearts.indices) {
            main_IMG_hearts[i].visibility = if (i < livesLeft) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun onCoinCollected() {
        val ssp = SingleSoundPlayer(this)
        ssp.playSound(R.raw.coin_collect)
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
        if(RecordManager.isNewRecord(gameManager.score))
            changeActivity(Constants.MESSAGES.NEW_HIGH_SCORE, gameManager.score)
        else {
            changeActivity(Constants.MESSAGES.GAME_OVER, gameManager.score)
        }
    }


    private fun changeActivity(message: String,score: Int){
        Log.d("Game Status",message+score)
        val intent = Intent(this, GameOverActivity::class.java)
        var bundle = Bundle()
        bundle.putString(Constants.BundleKeys.MESSAGE_KEY, message)
        bundle.putInt(Constants.BundleKeys.SCORE_KEY,score)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

}
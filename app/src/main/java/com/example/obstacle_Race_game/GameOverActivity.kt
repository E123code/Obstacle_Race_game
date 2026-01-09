package com.example.obstacle_Race_game

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obstacle_Race_game.model.HighScore
import com.example.obstacle_Race_game.model.RecordManager
import com.example.obstacle_Race_game.utilities.Constants
import com.example.obstacle_Race_game.utilities.SignalManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class GameOverActivity: AppCompatActivity() {


    private lateinit var gameOver_LBL_title: MaterialTextView

    private lateinit var gameOver_BTN_Menu: MaterialButton

    private  lateinit var gameOver_NewRecord : LinearLayout

    private  lateinit var  highScore_BTN_send : MaterialButton

    private lateinit var highScore_ET_text: TextInputEditText

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        gameOver_BTN_Menu = findViewById(R.id.gameOver_BTN_menu)
        gameOver_NewRecord = findViewById(R.id.highScore_dialog)
        highScore_ET_text = findViewById(R.id.start_ET_text)
        highScore_BTN_send = findViewById(R.id.start_BTN_send)
    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras

        val message = bundle?.getString(Constants.BundleKeys.MESSAGE_KEY,"🤷🏻‍♂️ Unknown Status!")
        val score = bundle?.getInt(Constants.BundleKeys.SCORE_KEY, 0)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        gameOver_LBL_title.text = buildString {
            append(message)
            append("\n")
            append("Score:")
            append(score)
        }

        if(message == Constants.MESSAGES.NEW_HIGH_SCORE){
            openRecordDialog(score)
        }else{
            gameOver_NewRecord.visibility = View.GONE
        }

        gameOver_BTN_Menu.setOnClickListener { v: View ->
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    @SuppressLint("MissingPermission")
    fun fetchLocation(onDone: (lat: Double, lon: Double) -> Unit) {
        val cts = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    onDone(location.latitude, location.longitude)
                    Log.d("LOCATION","got location!")
                } else {
                    onDone(0.0, 0.0)
                }
            }
            .addOnFailureListener {
                onDone(0.0, 0.0)
            }
    }
    private fun openRecordDialog(score : Int?) {
        gameOver_NewRecord.visibility = View.VISIBLE

        highScore_BTN_send.setOnClickListener {
            val name = highScore_ET_text.text.toString()
            if (name.isNotEmpty()) {
                highScore_BTN_send.isEnabled = false
                fetchLocation { lat, lon ->
                    val newHighScore =
                        HighScore.Builder().username(name).highScore(score).lat(lat).lon(lon)
                            .build()

                    RecordManager.addRecord(newHighScore)
                    SignalManager.getInstance()
                        .toast("Record Saved!", SignalManager.ToastLength.SHORT)
                    val intent = Intent(this, LeaderBoardActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            } else {
                SignalManager.getInstance()
                    .toast("Please enter name", SignalManager.ToastLength.SHORT)
            }

        }
    }

}
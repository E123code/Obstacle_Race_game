package com.example.Obstacle_Race_game.logic

import com.example.Obstacle_Race_game.Utilities.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import kotlin.random.Random

class GameManager(private val listener: GameListener  ) {

    var collsions = 0
        private set

    var carIndex =1 //left lane =0, center lane =1, right lane =2
        private set

    var isGameRunning = false //flag to know when to start and stop timer
        private  set


    var isCarInvulnerable = false
        private set


//the  matirx represent the obstacles( boxes)
    private val gameGrid = MutableList(Constants.GameLogic.ROAD_DEPTH){ IntArray(Constants.GameLogic.LANES) {0} }


    /**
     * function to initialize the game
     */
    init {
        resetGame();
    }

    /**
     *   reset the game  to initial state
     */

    fun resetGame(){
        collsions =0
        carIndex = 1
        isCarInvulnerable =false

        for(i in 0 until Constants.GameLogic.ROAD_DEPTH){
            gameGrid[i].fill(0);
        }

        //update UI and game status
        listener.onLaneChange(carIndex)
        listener.onCollision(collsions)
        isGameRunning = true
    }


    /**
     * demonstrates the movement of the car towards the obstacles
     */
    private fun shiftMatrix(){
        gameGrid.removeAt(0)
        gameGrid.add(generateRandomObstacles())
        listener.onMatrixUpdate(gameGrid)
    }


    /**
     * generates new row of obstacles so it will  be random
     */
    private fun generateRandomObstacles(): IntArray {
        val newRow = IntArray(Constants.GameLogic.LANES){ 0 }

        if(Random.nextDouble() < 0.6){ // 60% chance for row with obstacle ,40% for clear row
            val numOfObstacles = Random.nextInt(1,3)
            val availableLanes = (0 until  Constants.GameLogic.LANES).toMutableList() //creates list of all available lane indexes

            for (i in  0 until numOfObstacles){
                if(availableLanes.isNotEmpty()){
                    val  obstacleLane = availableLanes.random()
                    newRow[obstacleLane] = 1 // 1 - there's obstacle 0- clear lane
                    availableLanes.remove(obstacleLane)
                }
            }
        }

        //checks if there is one clear passable lane
        if(newRow.all { it == 1 }){// if all full
            newRow[(0 until  Constants.GameLogic.LANES).random()] = 0 // clear this section of one lane
        }

        return  newRow
    }

    /**
     * checks if collision occurred in the game
     * gets the scope the timer works on
     */
    private fun checkCollision(coroutineScope: CoroutineScope){
        if(isCarInvulnerable) return //if the car can't crash

        if (gameGrid[0][carIndex] ==1 )
            handleCrash(coroutineScope)

    }

    /**
     *handles the changes in the game in case of crash
     * gets the scope the timer works on
     */
    private  fun handleCrash(coroutineScope: CoroutineScope){
        collsions ++
        listener.onCollision(collsions)

        if(collsions >= 3){
            listener.onGameOver()
            isGameRunning = false
        }
        else{
            isCarInvulnerable = true
            coroutineScope.launch{
                delay(Constants.GameLogic.INVULNERABILITY_DURATION_MS)
                isCarInvulnerable = false
            }

            gameGrid[0][carIndex] = 0
        }
    }


    /**
     *   executes functions on every tick of clock
     */

    fun onGameTick(coroutineScope: CoroutineScope){
        if(!isGameRunning) return
        shiftMatrix()
        checkCollision(coroutineScope)
    }

    /**
     * function to demonstrate the car changing lanes movement
     */
    fun moveCar (direction: Int){  //1 right  -1 left
        if(!isGameRunning) return

        val newLane = carIndex + direction
        if(newLane in  0 until Constants.GameLogic.LANES){
            carIndex = newLane
            listener.onLaneChange(carIndex) // notify UI on change
        }

    }

}
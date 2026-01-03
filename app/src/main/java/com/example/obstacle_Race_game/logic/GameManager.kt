package com.example.obstacle_Race_game.logic

import com.example.obstacle_Race_game.utilities.Constants
import com.example.obstacle_Race_game.Interfaces.GameListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


import kotlin.random.Random

class GameManager(private val listener: GameListener) {

    var collisions = 0
        private set

    var carIndex =2
        private set

    var isGameRunning = false //flag to know when to start and stop timer
        private  set


    var isCarInvulnerable = false
        private set

    var score: Int = 0
        private set

//the  matrix represent the obstacles( boxes)
    private val gameGrid = MutableList(Constants.GameLogic.ROAD_DEPTH){ IntArray(Constants.GameLogic.LANES) {0} }



    /**
     *   reset the game  to initial state
     */

    fun resetGame(){
        collisions =0
        carIndex = 1
        score =0
        isCarInvulnerable =false

        gameGrid.forEach { it.fill(Constants.ItemTypes.EMPTY) }

        //update UI and game status
        listener.onLaneChange(carIndex)
        listener.onScoreUpdate(score)
        isGameRunning = true
    }


    /**
     * demonstrates the movement of the car towards the obstacles
     */
    private fun shiftMatrix(){
        score += Constants.GameLogic.SCORE_DEFAULT
        gameGrid.removeAt(0)
        gameGrid.add(generateRandomObstacles())
        listener.onMatrixUpdate(gameGrid)
        listener.onScoreUpdate(score)

    }


    /**
     * generates new row of obstacles so it will  be random
     */
    private fun generateRandomObstacles(): IntArray {
        val lanes = Constants.GameLogic.LANES
        val newRow = IntArray(lanes){ Constants.ItemTypes.EMPTY }

        if(Random.nextDouble() < 0.6){ // 60% chance for row with obstacle ,40% for clear row
            val numOfObstacles = Random.nextInt(1,lanes-1)
            val availableLanes = (0 until  lanes).toMutableList() //creates list of all available lane indexes

            for (i in  0 until numOfObstacles){
                if(availableLanes.isNotEmpty()){
                    val  obstacleLane = availableLanes.random()
                    val typeRoll = Random.nextDouble()
                    newRow[obstacleLane] =when{
                        typeRoll <0.7 -> Constants.ItemTypes.OBSTACLE
                        else  -> Constants.ItemTypes.COIN
                    }// 1 - there's obstacle 0- clear lane
                    availableLanes.remove(obstacleLane)
                }
            }
        }

        //checks if there is one clear passable lane
        if(newRow.all { it == Constants.ItemTypes.OBSTACLE }){// if all full
            newRow[(0 until  lanes).random()] =Constants.ItemTypes.EMPTY // clear this section of one lane
        }

        return  newRow
    }

    /**
     * checks if collision occurred in the game
     * gets the scope the timer works on
     */
    private fun checkCollision(coroutineScope: CoroutineScope){
        if(!isGameRunning) return //if the car can't crash
        val  item = gameGrid[0][carIndex]
        when(item){
            Constants.ItemTypes.OBSTACLE->{
                if (!isCarInvulnerable) handleCrash(coroutineScope)
            }
            Constants.ItemTypes.COIN->{
                score += Constants.GameLogic.COIN_DEFAULT
                listener.onScoreUpdate(score)
                gameGrid[0][carIndex] = Constants.ItemTypes.EMPTY
            }
        }



    }

    /**
     *handles the changes in the game in case of crash
     * gets the scope the timer works on
     */
    private  fun handleCrash(coroutineScope: CoroutineScope){
        collisions ++
        listener.onCollision()

        if(collisions >= 3){
            listener.onGameOver()
            isGameRunning = false
        }
        else{// if the game isn't over
            isCarInvulnerable = true //for this period of time make the car invulnerable
            coroutineScope.launch{ // so it won't lose any life in this time period using coroutine
                delay(Constants.GameLogic.INVULNERABILITY_DURATION_MS)
                isCarInvulnerable = false
            }
            gameGrid[0][carIndex] = Constants.ItemTypes.EMPTY
        }
    }


    /**
     * resets the invulnerability of car in case of pause or reset
     */
    fun resetInvulnerability(){
        isCarInvulnerable = false
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
        val lanes = Constants.GameLogic.LANES
        carIndex = (carIndex + direction +lanes) % lanes
        listener.onLaneChange(carIndex) // notify UI on change
    }

}
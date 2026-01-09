package com.example.obstacle_Race_game.interfaces

/**
 * Interface to be implemented in MainActivity class
 * to show the changes in the Status/matrix in the logic of the game (GameManager)
 *
 */
interface GameListener {

    //SIGNAL FOR CHANGE IN MATRIX (OBSTACLE MATRIX)
    fun onMatrixUpdate(grid: List<IntArray>)

    //SIGNAL FOR LANE CHANGE
    fun onLaneChange(newLaneIndex: Int)



    //SIGNAL FOR COLLISION SIGNALING
    fun onCollision()

    //SIGNAL FOR CHANGING NUM OF LIVES
    fun onLivesUpdate()

//SIGNAL FOR COIN SOUND ON COLLECTION
    fun onCoinCollected()

    //SIGNAL FOR GAME OVER
    fun onGameOver()

    fun onScoreUpdate(score: Int)

}
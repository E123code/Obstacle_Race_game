package com.example.Obstacle_Race_game.logic

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


    //SIGNAL FOR CHANGING NUM OF LIVES
    fun onCollision(collisions: Int)

    //SIGNAL FOR GAME OVER
    fun onGameOver()


}








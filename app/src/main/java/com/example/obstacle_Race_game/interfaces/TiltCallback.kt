package com.example.obstacle_Race_game.interfaces

/**
 * callback for the tilt detector
 */
interface TiltCallback {
    fun onTiltX(x: Float)// for detecting tilt on x axis
    fun onTiltY(y: Float)// for detecting tilt on y axis
}
package com.example.obstacle_Race_game.model

/**
 * HighScore object for the HighScores in the list
 */
data class HighScore private constructor(
    val playerName : String,
    val highScore : Int,
    val lat: Double,
    val lon : Double,
    val timestamp: Long = System.currentTimeMillis()
){
    class Builder(
        var playerName : String ="",
        var highScore : Int = 0,
        var lat: Double =0.0,
        var lon : Double =0.0,
    ){
        fun username(username: String) = apply { this.playerName = username }
        fun  highScore(highScore: Int?)  = apply { this.highScore = highScore?:0 }
        fun  lat(lat: Double)  = apply { this.lat = lat }
        fun  lon(lon: Double)  = apply { this.lon = lon }

        fun build() = HighScore(playerName, highScore, lat,lon)
    }


}

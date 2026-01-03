package com.example.obstacle_Race_game.utilities

class Constants {
    object GameLogic {
        const val LANES = 5
        const val ROAD_DEPTH = 7
        const val GAME_TICK_MS = 600L
        const val LANE_CHANGE_DELAY_MS = 200L
        const val INVULNERABILITY_DURATION_MS = 2000L

        const val SCORE_DEFAULT: Int = 10

        const val COIN_DEFAULT: Int = 15
    }

    object ItemTypes{
        const val  EMPTY = 0
        const val OBSTACLE = 1
        const val  COIN =2

    }

}
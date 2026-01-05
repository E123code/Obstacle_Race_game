package com.example.obstacle_Race_game.utilities

class Constants {
    object GameLogic {
        const val LANES = 5
        const val ROAD_DEPTH = 9
      const val GAME_TICK_MS = 500L



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

    object SP_KEYS {
        const val DATA_FILE: String = "DATA_FILE"
    }

    object BundleKeys {
        const val MESSAGE_KEY: String = "MESSAGE_KEY"
        const val SCORE_KEY: String = "SCORE_KEY"
        const val CONTROL_MODE: String = "CONTROL_MODE"
        const val  SENSORS: String = "SENSORS"
        const val  BUTTONS: String = "BUTTONS"
    }

}
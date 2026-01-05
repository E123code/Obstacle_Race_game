package com.example.obstacle_Race_game

import android.app.Application
import com.example.obstacle_Race_game.utilities.ImageLoader
import com.example.obstacle_Race_game.utilities.SignalManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        ImageLoader.init(this)
    }
}
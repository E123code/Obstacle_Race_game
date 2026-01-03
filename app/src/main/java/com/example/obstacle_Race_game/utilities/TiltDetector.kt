package com.example.obstacle_Race_game.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.obstacle_Race_game.Interfaces.TiltCallback


class TiltDetector(context: Context, private val tiltCallback: TiltCallback) {

    private val sensorManager = context.getSystemService(
        Context.SENSOR_SERVICE
    ) as SensorManager

    private val sensor = sensorManager.getDefaultSensor(
        Sensor.TYPE_ACCELEROMETER
    )





    private lateinit var sensorEventListener: SensorEventListener

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                // Pass
            }

            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]

                tiltCallback.onTiltX(x)
                tiltCallback.onTiltY(y)

            }

        }
    }



    fun start() {
        sensorManager
            .registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
    }


    fun stop() {
        sensorManager
            .unregisterListener(
                sensorEventListener,
                sensor
            )
    }
}
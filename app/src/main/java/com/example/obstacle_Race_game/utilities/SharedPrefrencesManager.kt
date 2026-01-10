package com.example.obstacle_Race_game.utilities

import android.content.Context

// the class for handling the shared preferences logic
class SharedPreferencesManager private constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        Constants.SP_KEYS.RECORDS_KEY,
        Context.MODE_PRIVATE
    )

    companion object{
        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun init(context: Context) : SharedPreferencesManager{
            return instance ?: synchronized(this){
                instance ?: SharedPreferencesManager(context).also { instance = it }
            }
        }


        /**
         * the function to make the this class a singleton
         */
        fun getInstance(): SharedPreferencesManager {
            return instance ?: throw IllegalStateException(
                "SharedPreferencesManagerV3 must be initialized by calling init(context) before use."
            )

        }
    }

    /**
     * function to save a string with a key to json shared preferences file
     */
    fun putString( key: String, value: String){
        with(sharedPreferences.edit()){
            putString(key,value)
            apply()
        }

    }

    /**
     * function to get a string by key from json shared preferences file
     */
    fun getString( key: String, defaultValue: String): String{
        return sharedPreferences
            .getString(
                key,
                defaultValue
            ) ?: defaultValue
    }
}

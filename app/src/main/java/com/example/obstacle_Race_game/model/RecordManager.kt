package com.example.obstacle_Race_game.model

import com.example.obstacle_Race_game.utilities.SharedPreferencesManager
import com.example.obstacle_Race_game.utilities.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * class for handling the scores list management
 */
object RecordManager {
    private val storage = SharedPreferencesManager.getInstance()
    private const val MAX_RECORDS = 10
    private val  gson = Gson()


    /**
     * function to save the highScores to the shared preferences
     */
    private fun saveRecords(highScores: List<HighScore>){
        val recordsAsJson = gson.toJson(highScores)
        storage.putString(Constants.SP_KEYS.RECORDS_KEY,recordsAsJson)
    }


    /**
     * function to get the highScores from the shared preferences
     */

    fun getRecords(): List<HighScore>{
        val savedRecords = storage.getString(Constants.SP_KEYS.RECORDS_KEY,"[]")
        return try{
            gson.fromJson(savedRecords, object: TypeToken<List<HighScore>>(){}.type) ?: emptyList()
        }catch (e: Exception){
            emptyList()
        }
    }

    /**
     * checks if the new score is a new highScore for the list
     */
    fun isNewRecord(score: Int) : Boolean{
        if(score == 0) return false
        val  records = getRecords()

        return records.size < MAX_RECORDS || score > (records.lastOrNull()?.highScore?:0)

    }

    /**
     * Function to add New Score object to the List and
     * change the top 10 list accordingly,and save it to the shared preferences
     */
    fun addRecord(newHighScore: HighScore){
        val lRecord = getRecords().toMutableList()
        lRecord.add(newHighScore)

        val sortedList = lRecord.sortedWith(compareByDescending <HighScore>{it.highScore}
            .thenByDescending { it.timestamp})
            .take(MAX_RECORDS)

        saveRecords(sortedList)
    }

}
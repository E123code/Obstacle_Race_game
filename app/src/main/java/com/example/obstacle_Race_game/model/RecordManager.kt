package com.example.obstacle_Race_game.model

import com.example.obstacle_Race_game.utilities.SharedPreferencesManager
import com.example.obstacle_Race_game.utilities.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object RecordManager {
    private val storage = SharedPreferencesManager.getInstance()
    private const val MAX_RECORDS = 10
    private val  gson = Gson()

    private fun saveRecords(highScores: List<HighScore>){
        val recordsAsJson = gson.toJson(highScores)
        storage.putString(Constants.SP_KEYS.RECORDS_KEY,recordsAsJson)
    }

    fun getRecords(): List<HighScore>{
        val savedRecords = storage.getString(Constants.SP_KEYS.RECORDS_KEY,"[]")
        return try{
            gson.fromJson(savedRecords, object: TypeToken<List<HighScore>>(){}.type) ?: emptyList()
        }catch (e: Exception){
            emptyList()
        }
    }

    fun isNewRecord(score: Int) : Boolean{
        if(score == 0) return false
        val  records = getRecords()

        return records.size < MAX_RECORDS || score > (records.lastOrNull()?.highScore?:0)

    }

    fun addRecord(newHighScore: HighScore){
        val lRecord = getRecords().toMutableList()
        lRecord.add(newHighScore)

        val sortedList = lRecord.sortedWith(compareByDescending <HighScore>{it.highScore}
            .thenByDescending { it.timestamp})
            .take(MAX_RECORDS)

        saveRecords(sortedList)
    }

}
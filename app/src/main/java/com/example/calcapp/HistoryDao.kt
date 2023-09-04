package com.example.calcapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)

    @Query("SELECT * FROM history ORDER BY id ASC")
    fun display(): List<History>

    @Query("DELETE FROM history")
    suspend fun clearHistory()


}
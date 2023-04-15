package com.example.quizapplicationtest.room

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizapplicationtest.modal.History
import com.example.quizapplicationtest.modal.User

@androidx.room.Dao
interface HistoryDao {
    @Insert
    fun insertHistory(history : History)
    @Query("SELECT * FROM History WHERE userID LIKE :userID")
    fun retrieveHistory(userID: String):List<History>

    @Query("SELECT SUM(totalScore) FROM History")
    fun getSum():Double

}
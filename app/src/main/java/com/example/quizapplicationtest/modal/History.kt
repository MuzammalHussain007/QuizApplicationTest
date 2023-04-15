package com.example.quizapplicationtest.modal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "History")
data class History(
    @PrimaryKey(autoGenerate = true)
    val history: Int,
    val userID :String,
    val status: String,
    val totalTime: String,
    val totalScore: String,
    val correctAns: String,
    val size: String
)

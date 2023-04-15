package com.example.quizapplicationtest.room

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizapplicationtest.modal.User

@androidx.room.Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun signup(user : User) : Long
    @Query("SELECT * FROM User WHERE email LIKE :email AND password LIKE :password")
    fun login(email: String, password: String):User

    @Query("SELECT * FROM User WHERE email = :userName")
    fun isDataExist(userName: String?): Int
}
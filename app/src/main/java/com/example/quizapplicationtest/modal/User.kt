package com.example.quizapplicationtest.modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(tableName = "User", indices = [Index(value = ["email"], unique = true)])
class User(
    @PrimaryKey(autoGenerate = true)
    val id : Int ,
    @ColumnInfo(name = "email")
    val email : String ,
    @ColumnInfo(name = "password")
    val password : String ,
    @ColumnInfo(name = "fullName")
    val  fullName : String
    )



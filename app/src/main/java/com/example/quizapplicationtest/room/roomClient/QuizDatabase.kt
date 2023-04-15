package com.example.quizapplicationtest.room.roomClient

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizapplicationtest.modal.History
import com.example.quizapplicationtest.modal.User
import com.example.quizapplicationtest.room.Dao
import com.example.quizapplicationtest.room.HistoryDao

@Database(
    entities =  [User::class,History::class],
    version = 1,
    exportSchema = false
)

abstract class QuizDatabase: RoomDatabase() {
    abstract fun getUserDao(): Dao
    abstract fun getHistoryDao(): HistoryDao

    companion object {
        private const val DB_NAME = "user_database.db"
        @Volatile private var instance: QuizDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            QuizDatabase::class.java,
            DB_NAME
        ).build()
    }
}
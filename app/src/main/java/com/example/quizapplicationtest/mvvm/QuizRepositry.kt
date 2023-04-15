package com.example.quizapplicationtest.mvvm

import com.example.quizapplicationtest.modal.User
import com.example.quizapplicationtest.room.roomClient.QuizDatabase

class QuizRepositry(private val quizDatabase: QuizDatabase, val retrofitService: QuizRetrofitService) {

    suspend fun signUp(user: User): Long {
        val value = quizDatabase.getUserDao().signup(user)
        return value
    }

    suspend fun login(user: User) = quizDatabase.getUserDao().login(user.email, user.password)

     suspend fun isDataExists(email : String) = quizDatabase.getUserDao().isDataExist(email)

    suspend fun getQuestionData(amount:String , cateID : String , diffLevel : String , type : String) = retrofitService.apiResponse(amount,cateID,diffLevel, type = type)

    suspend fun getHistoryData(userID : String) = quizDatabase.getHistoryDao().retrieveHistory(userID)

    suspend fun getSum() = quizDatabase.getHistoryDao().getSum()

}
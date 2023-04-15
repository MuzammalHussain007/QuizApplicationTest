package com.example.quizapplicationtest.mvvm

import android.content.Context
import com.example.quizapplicationtest.modal.API_Response.Question
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface QuizRetrofitService {
    @GET("api.php")
    fun apiResponse(
        @Query("amount") amount: String,
        @Query("category") category: String,
        @Query("difficulty") difficulty: String,
        @Query("type") type: String,
    ): Call<Question>
    companion object {
        var retrofitService: QuizRetrofitService? = null
        fun getInstance(context: Context): QuizRetrofitService {
            val gson = GsonBuilder().setLenient().create()

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://opentdb.com/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                retrofitService = retrofit.create(QuizRetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}
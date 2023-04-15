package com.example.quizapplicationtest.modal.API_Response

data class Question(
    val response_code: Int,
    val results: List<Result>
)
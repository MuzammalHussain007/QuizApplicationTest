package com.example.quizapplicationtest.mvvm.viewmodal

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapplicationtest.modal.User
import com.example.quizapplicationtest.mvvm.QuizRepositry

class UserViewModal(val quizRepop: QuizRepositry) : ViewModel() {
    var isAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    var users: MutableLiveData<User> = MutableLiveData()

    suspend fun signup(user: User) {
        quizRepop.signUp(user)
        isAdded.postValue(true)
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun login(user: User) {
    val getUser  =  quizRepop.login(user)
        users.postValue(getUser)
    }

    suspend fun isDataExists(email :String) : Int {
        return quizRepop.isDataExists(email)
    }
}
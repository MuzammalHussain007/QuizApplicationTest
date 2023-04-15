package com.example.quizapplicationtest.mvvm

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizapplicationtest.mvvm.viewmodal.HistoryViewModal
import com.example.quizapplicationtest.mvvm.viewmodal.QuizViewModal
import com.example.quizapplicationtest.mvvm.viewmodal.UserViewModal

class QuizViewModalFactory constructor(private val repository: QuizRepositry,val context : Context , val activity : Activity) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(QuizViewModal::class.java)) {
            QuizViewModal(this.repository,context,activity) as T
        }
        else if (modelClass.isAssignableFrom(UserViewModal::class.java)) {
            UserViewModal(this.repository) as T
        }
        else if (modelClass.isAssignableFrom(HistoryViewModal::class.java)) {
            HistoryViewModal(this.repository) as T
        }
        else
            throw IllegalArgumentException("ViewModel Not Found")
    }
}
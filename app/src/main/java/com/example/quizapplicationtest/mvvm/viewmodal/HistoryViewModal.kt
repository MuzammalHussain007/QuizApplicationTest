package com.example.quizapplicationtest.mvvm.viewmodal

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapplicationtest.modal.History
import com.example.quizapplicationtest.mvvm.QuizRepositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryViewModal(val repository: QuizRepositry) :ViewModel() {
    var historyList : ArrayList<History> = ArrayList()
    var isHistoryAvailable: MutableLiveData<Boolean> = MutableLiveData()



    suspend fun getHistory(userID :String) :ArrayList<History>
    {
        withContext(Dispatchers.IO)
        {
            val list = repository.getHistoryData(userID)
            Log.d("error_____", "viewModal$list")

            historyList = ArrayList(list)
        }
        Log.d("error_____","viewModal"+historyList.size)
        return historyList

    }
}
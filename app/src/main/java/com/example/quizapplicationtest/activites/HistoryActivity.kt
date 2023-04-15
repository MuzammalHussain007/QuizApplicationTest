package com.example.quizapplicationtest.activites

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapplicationtest.R
import com.example.quizapplicationtest.adapter.HistoryAdapter
import com.example.quizapplicationtest.databinding.ActivityHistoryBinding
import com.example.quizapplicationtest.modal.History
import com.example.quizapplicationtest.mvvm.QuizRepositry
import com.example.quizapplicationtest.mvvm.QuizRetrofitService
import com.example.quizapplicationtest.mvvm.QuizViewModalFactory
import com.example.quizapplicationtest.mvvm.viewmodal.HistoryViewModal
import com.example.quizapplicationtest.room.roomClient.QuizDatabase
import com.example.quizapplicationtest.util.AppStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryBinding
    private lateinit var quizViewModal: HistoryViewModal
    private lateinit var quizDatabase: QuizDatabase
    private lateinit var repository: QuizRepositry
    private lateinit var factory: QuizViewModalFactory
    private lateinit var retrofitService: QuizRetrofitService
    private lateinit var list : ArrayList<History>
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_history)
        innit()

    }

    private fun innit() {
        progressDialog = ProgressDialog(this,R.style.MyAlertDialogStyle)
        progressDialog.setMessage("Please Wait")
        progressDialog.show()
        list = ArrayList()
        retrofitService = QuizRetrofitService.getInstance(this)
        quizDatabase = QuizDatabase.invoke(this)
        repository = QuizRepositry(quizDatabase, retrofitService)
        factory = QuizViewModalFactory(repository, this, this)
        quizViewModal = ViewModelProvider(this, factory)[HistoryViewModal::class.java]
        AppStorage.init(this)
        GlobalScope.launch {
         list = quizViewModal.getHistory(AppStorage.getCurrentUserId().toString())
        }
        Handler(Looper.getMainLooper()).postDelayed({
           setUpRecyclarView()
        }, 1000)
    }

    private fun setUpRecyclarView() {
        progressDialog.dismiss()
        binding.historyRecyclarView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclarView.adapter = HistoryAdapter(this,list)
    }
}
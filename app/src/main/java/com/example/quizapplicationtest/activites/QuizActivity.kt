package com.example.quizapplicationtest.activites

import android.app.ProgressDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizapplicationtest.R
import com.example.quizapplicationtest.databinding.ActivityQuizBinding
import com.example.quizapplicationtest.mvvm.QuizRepositry
import com.example.quizapplicationtest.mvvm.QuizRetrofitService
import com.example.quizapplicationtest.mvvm.QuizViewModalFactory
import com.example.quizapplicationtest.mvvm.viewmodal.QuizViewModal
import com.example.quizapplicationtest.room.roomClient.QuizDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat


class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var quizViewModal: QuizViewModal
    private lateinit var quizDatabase: QuizDatabase
    private lateinit var repository: QuizRepositry
    private lateinit var factory: QuizViewModalFactory
    private lateinit var retrofitService: QuizRetrofitService
    private lateinit var progressDialog: ProgressDialog
    private var timeLeft: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)
        innit()
        binding.quizViewModel = quizViewModal
        binding.lifecycleOwner = this

    }

    private fun innit() {
        retrofitService = QuizRetrofitService.getInstance(this)
        quizDatabase = QuizDatabase.invoke(this)
        repository = QuizRepositry(quizDatabase, retrofitService)
        factory = QuizViewModalFactory(repository, this, this)
        quizViewModal = ViewModelProvider(this, factory)[QuizViewModal::class.java]
        progressDialog = ProgressDialog(this,R.style.MyAlertDialogStyle)
        progressDialog.setMessage("Please Wait")
        progressDialog.show()
        try {
            if (intent != null) {
                val amount = intent.getStringExtra("amount")
                val categoryid = intent.getStringExtra("categoryid")
                val difficultyid = intent.getStringExtra("difficultyid")
                val typeid = intent.getStringExtra("typeid")

                GlobalScope.launch {
                    quizViewModal.getQuestionData(
                        amount.toString(),
                        categoryid.toString(),
                        difficultyid.toString(),
                        typeid.toString()
                    )
                }

            }

            quizViewModal.apiResponse.observe(this) {
                if (it != null) {
                    progressDialog.dismiss()

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
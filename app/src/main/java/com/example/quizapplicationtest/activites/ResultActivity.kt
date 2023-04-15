package com.example.quizapplicationtest.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.quizapplicationtest.R
import com.example.quizapplicationtest.databinding.ActivityResultBinding
import com.example.quizapplicationtest.modal.History
import com.example.quizapplicationtest.room.roomClient.QuizDatabase
import com.example.quizapplicationtest.util.AppStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultBinding
    private lateinit var quizDatabase: QuizDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        binding = DataBindingUtil.setContentView(this,R.layout.activity_result)
        quizDatabase = QuizDatabase.invoke(this)
        AppStorage.init(this)
        try {
            if (intent!=null)
            {
                val totalTime = intent.getStringExtra("totalTime")
                val totalPontEarned = intent.getStringExtra("totalPontEarned")
                val status = intent.getStringExtra("status")
                val correctAns = intent.getStringExtra("correctAns")
                val size = intent.getStringExtra("size")

                val second =  (totalTime!!.toLong() / 1000) % 60
                val minute =  (totalTime.toLong() / 1000*60) % 60
                val hrs =  (totalTime.toLong() / 1000*3600) % 24

                binding.resultStatus.text = status
                binding.totalScore.text = totalPontEarned
                binding.totalCorrectAns.text = "$correctAns out of $size"

                binding.totalTimeConsumed.text = "$hrs : $minute : $second"

                var point = AppStorage.getCurrentPoints().toString().toInt()
                point += totalPontEarned!!.toInt()
                AppStorage.setCurrentPoints(point.toString())

                GlobalScope.launch {
                    quizDatabase.getHistoryDao().insertHistory(History(0,AppStorage.getCurrentUserId().toString(),
                        status.toString(),
                        totalTime.toString(),
                        totalPontEarned.toString(),
                        correctAns.toString(),
                        size.toString()))
                }

                binding.backBtn.setOnClickListener {
                    finish()
                }



            }
        }catch (e : Exception)
        {
            e.printStackTrace()
        }


    }


}
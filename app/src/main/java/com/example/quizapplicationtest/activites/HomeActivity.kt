package com.example.quizapplicationtest.activites

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizapplicationtest.R
import com.example.quizapplicationtest.databinding.ActivityHomeBinding
import com.example.quizapplicationtest.modal.User
import com.example.quizapplicationtest.mvvm.QuizRepositry
import com.example.quizapplicationtest.mvvm.QuizRetrofitService
import com.example.quizapplicationtest.mvvm.QuizViewModalFactory
import com.example.quizapplicationtest.mvvm.viewmodal.QuizViewModal
import com.example.quizapplicationtest.room.roomClient.QuizDatabase
import com.example.quizapplicationtest.util.AppStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var quizViewModal: QuizViewModal
    private lateinit var quizDatabase: QuizDatabase
    private lateinit var repository: QuizRepositry
    private lateinit var factory: QuizViewModalFactory
    private lateinit var retrofitService: QuizRetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        AppStorage.init(this)
        binding.listener = com.example.quizapplicationtest.util.ClickHandler(this,::exitDialog)
        binding.user = User(AppStorage.getCurrentUserId()!!.toInt(),"","",AppStorage.getFullName().toString())
        innit()
    }

    private fun innit() {
        retrofitService = QuizRetrofitService.getInstance(this)
        quizDatabase = QuizDatabase.invoke(this)
        repository = QuizRepositry(quizDatabase, retrofitService)
        factory = QuizViewModalFactory(repository, this, this)
        quizViewModal = ViewModelProvider(this, factory)[QuizViewModal::class.java]



    }

    private fun exitDialog()
    {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this)
            .inflate(R.layout.activty_exit, findViewById(R.id.container), false)
        dialog.setContentView(view)
        view.findViewById<View>(R.id.button_exit_no).setOnClickListener { view1: View? ->
            dialog.cancel()
            dialog.dismiss()
        }
        view.findViewById<View>(R.id.button_exit_yes)
            .setOnClickListener { view2: View? ->
             AppStorage.deletePreference()
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("finish", true)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        dialog.show()

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO) {
            quizViewModal.getSum()
        }
        quizViewModal.sumOfValue.observe(this){
            binding.points.text = it.toString()
        }
    }

}
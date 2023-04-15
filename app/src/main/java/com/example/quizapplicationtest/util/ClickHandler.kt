package com.example.quizapplicationtest.util

import android.content.Context
import android.content.Intent
import android.view.View
import com.example.quizapplicationtest.R
import com.example.quizapplicationtest.activites.AboutActivity
import com.example.quizapplicationtest.activites.HistoryActivity
import com.example.quizapplicationtest.activites.QuizSelectionActivity

class ClickHandler(val context: Context, val exit: () -> Unit) : View.OnClickListener {
    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.cvQuiz->{
                context.startActivity(Intent(context,QuizSelectionActivity::class.java))
            }
            R.id.cvHistory->{
                context.startActivity(Intent(context,HistoryActivity::class.java))
            }
            R.id.cvAbout->{
                context.startActivity(Intent(context,AboutActivity::class.java))
            }
            R.id.cvLogout->{
                exit()
            }
        }
    }
}
package com.example.quizapplicationtest

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapplicationtest.activites.HomeActivity
import com.example.quizapplicationtest.activites.LoginActivity
import com.example.quizapplicationtest.util.AppStorage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_main)
        AppStorage.init(this)

        moveToNext()

    }

    private fun moveToNext() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (AppStorage.getCurrentUserId()!="0")
            {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else
            {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000)
    }
}
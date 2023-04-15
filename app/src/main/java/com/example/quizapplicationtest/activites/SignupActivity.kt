package com.example.quizapplicationtest.activites

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizapplicationtest.R
import com.example.quizapplicationtest.databinding.ActivitySignupBinding
import com.example.quizapplicationtest.modal.User
import com.example.quizapplicationtest.mvvm.QuizRepositry
import com.example.quizapplicationtest.mvvm.QuizRetrofitService
import com.example.quizapplicationtest.mvvm.QuizViewModalFactory
import com.example.quizapplicationtest.mvvm.viewmodal.UserViewModal
import com.example.quizapplicationtest.room.roomClient.QuizDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: UserViewModal
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        innit()
        binding.signupListener = SignupToLoginListener(this, ::validateAndAuthenticateUser)
    }

    private fun innit() {
        retrofitService = QuizRetrofitService.getInstance(this)
        quizDatabase = QuizDatabase.invoke(this)
        repository = QuizRepositry(quizDatabase,retrofitService)
        factory = QuizViewModalFactory(repository,this,this)
        viewModel = ViewModelProvider(this, factory)[UserViewModal::class.java]

        viewModel.isAdded.observe(this){
            if (it)
            {
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }

        }

    }

    private fun validateAndAuthenticateUser() {
        val email = binding.SignUpScreenEmail.text.toString()
        val pass = binding.signupScreenPassword.text.toString()
        val fullname = binding.SignUpScreenFullName.text.toString()

        if (fullname.isEmpty()) {
            binding.SignUpScreenFullName.setError("Full Name must not empty")
            return
        }
        if (email.isEmpty()) {
            binding.SignUpScreenEmail.setError("Email must not empty")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.SignUpScreenEmail.setError("Email pattern is invalid")
            return
        }
        if (pass.isEmpty()) {
            binding.signupScreenPassword.setError("Password must not empty")
            return
        }
        if (pass.length < 6) {
            binding.signupScreenPassword.setError("Password length must be greater then 6")
            return
        }
        createAccount(email, pass, fullname)
    }

    private fun createAccount(email: String, pass: String, fullName: String) {
        GlobalScope.launch (Dispatchers.IO){
            viewModel.signup(User(0,email,pass,fullName))
        }



    }


}

class SignupToLoginListener(private val context: Context, val moveToHome: () -> Unit) :
    View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.signScreenLogin -> {
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
            R.id.signupBtn -> {
                moveToHome()
            }
        }

    }
}
package com.example.quizapplicationtest.activites

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizapplicationtest.R
import com.example.quizapplicationtest.databinding.ActivityLoginBinding
import com.example.quizapplicationtest.modal.User
import com.example.quizapplicationtest.mvvm.QuizRepositry
import com.example.quizapplicationtest.mvvm.QuizRetrofitService
import com.example.quizapplicationtest.mvvm.QuizViewModalFactory
import com.example.quizapplicationtest.mvvm.viewmodal.UserViewModal
import com.example.quizapplicationtest.room.roomClient.QuizDatabase
import com.example.quizapplicationtest.util.AppStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        innit()
        binding.listener = ClickListener(this,::validateAndAuthenticateUser)
        viewModel.users.observe(this){
            try
            {

                if (it.email.toString().isNotEmpty() && it.password.toString().isNotEmpty())
                {
                    AppStorage.setCurrentUserId(it.id.toString())
                    AppStorage.setFullName(it.fullName.toString())
                    Log.d("loginUser_____",""+it.email)
                    startActivity(Intent(this,HomeActivity::class.java))
                }

            }catch (e : Exception)
            {
                Toast.makeText(applicationContext,"Email or Password is incorrect",Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

        }
    }

    private fun innit() {
        retrofitService = QuizRetrofitService.getInstance(this)
        quizDatabase = QuizDatabase.invoke(this)
        repository = QuizRepositry(quizDatabase,retrofitService)
        factory = QuizViewModalFactory(repository,this,this)
        viewModel = ViewModelProvider(this, factory)[UserViewModal::class.java]
        AppStorage.init(this)


    }


    private fun validateAndAuthenticateUser()
    {


        val email = binding.LoginEmail.text.toString()
        val pass = binding.LoginPassword.text.toString()

        if (email.isEmpty())
        {
            binding.LoginEmail.setError("Email must not empty")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.LoginEmail.setError("Email pattern is invalid")
            return
        }
        if (pass.isEmpty())
        {
            binding.LoginPassword.setError("Password must not empty")
            return
        }
        if (pass.length<6)
        {
            binding.LoginPassword.setError("Password length must be greater then 6")
            return
        }
        authenticate(email,pass)
    }

    private fun authenticate(email: String, pass: String) {
          val user = User(0,email,pass,"")

        GlobalScope.launch(Dispatchers.IO) {
            if (viewModel.isDataExists(email)==0)
            {
                runOnUiThread {
                    Toast.makeText(applicationContext,"Wrong Email or Password",Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                viewModel.login(user)
            }
        }

//        GlobalScope.launch(Dispatchers.IO) {
//            viewModel.login(user)
//        }


    }
}



class ClickListener(private val context: Context, private val moveToHomeActivity: () -> Unit) : View.OnClickListener{
    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
         R.id.loginPageSignUp->{
             context.startActivity(Intent(context,SignupActivity::class.java))
         }
            R.id.moveToHome->{
                moveToHomeActivity()
            }
        }

    }
}
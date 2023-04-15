package com.example.quizapplicationtest.mvvm.viewmodal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapplicationtest.R
import com.example.quizapplicationtest.activites.ResultActivity
import com.example.quizapplicationtest.modal.API_Response.Question
import com.example.quizapplicationtest.modal.QuestionForUi
import com.example.quizapplicationtest.mvvm.QuizRepositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class QuizViewModal(val quizRepo: QuizRepositry, val context: Context,val activity: Activity) : ViewModel() {
    var apiResponse: MutableLiveData<Question> = MutableLiveData()
    var isMultipleChoice: MutableLiveData<Boolean> = MutableLiveData()
    var questionForUi: MutableLiveData<QuestionForUi> = MutableLiveData<QuestionForUi>()
    var sumOfValue: MutableLiveData<Double> = MutableLiveData<Double>()
    var currentQuestionIndex: Int? = 0
    var correctAns: Int? = 0
    var list: ArrayList<com.example.quizapplicationtest.modal.API_Response.Result> = ArrayList()
    var questionTime : MutableLiveData<String> = MutableLiveData()
    private lateinit var condownTimer: CountDownTimer
    private var startTime : Long = 0L
    private var endTIime : Long = 0L



    suspend fun getQuestionData(amount: String, cateID: String, diffLevel: String, type: String) {
        withContext(Dispatchers.IO)
        {
            val resposne = quizRepo.getQuestionData(amount, cateID, diffLevel, type)
            resposne.enqueue(object : Callback<Question> {
                override fun onResponse(call: Call<Question>, response: Response<Question>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.response_code == 0) {
                            apiResponse.postValue(response.body())
                            startTime = System.currentTimeMillis()

                            var resList = ArrayList(response.body()!!.results)
                            list = resList
                            Log.d("error_______", "" + list)
                            Log.d("error_______", "" + apiResponse.toString())
                            if (response.body()!!.results[0].type == "multiple") {
                                val time = setTimeForQuestion(response.body()!!.results[0].difficulty)
                                questionTime.value = time
                                setTimer(time.toInt())
                                condownTimer.start()
                                val questionForUis = QuestionForUi(
                                    response.body()!!.results[0].question,
                                    response.body()!!.results[0].incorrect_answers[0],
                                    response.body()!!.results[0].incorrect_answers[1],
                                    response.body()!!.results[0].incorrect_answers[2],
                                    response.body()!!.results[0].correct_answer,
                                )
                                questionForUi.value = questionForUis
                                isMultipleChoice.value = true

                            } else if (response.body()!!.results[0].type == "boolean") {
                                val time = setTimeForQuestion(response.body()!!.results[0].difficulty)
                                questionTime.value = time
                                setTimer(time.toInt())
                                condownTimer.start()

                                val questionForUis = QuestionForUi(
                                    response.body()!!.results[0].question,
                                    response.body()!!.results[0].correct_answer,
                                    response.body()!!.results[0].incorrect_answers[0],
                                    "", ""
                                )
                                questionForUi.value = questionForUis
                                isMultipleChoice.value = false
                            }
                        }
                    }


                }

                override fun onFailure(call: Call<Question>, t: Throwable) {
                    Log.d("error_______", "" + t.toString())
                }
            })
        }

    }

    fun nextQuestion()  //Btn
    {
        Log.d("error_______", "this is ")
    }

    fun answers(view: View) {
        when (view.id) {
            R.id.radioButton1 -> {
                condownTimer.cancel()
                next()
            }
            R.id.radioButton2 -> {
                condownTimer.cancel()
                if (list[currentQuestionIndex!!].type=="boolean")
                {
                    correctAns = correctAns!!+1
                }
                next()
            }
            R.id.radioButton3 -> {
                condownTimer.cancel()
                next()
            }
            R.id.radioButton4 -> {
                condownTimer.cancel()
                if (list[currentQuestionIndex!!].type=="multiple")
                {
                    correctAns = correctAns!!+1
                }
                next()
            }
        }

    }

    fun next() {
        currentQuestionIndex = currentQuestionIndex!! + 1
        if (currentQuestionIndex == list.size) {

            endTIime = System.currentTimeMillis()
            var totalTime = endTIime-startTime


            var totalPontEarned = correctAns!!*10

            var percent = (correctAns!!*100)/list.size
            var status = ""
            status = if (percent>60) {
                "PASS"
            }else {
                "FAIL"
            }



            context.startActivity(Intent(context,ResultActivity::class.java).also {
                it.putExtra("totalTime",totalTime.toString())
                it.putExtra("totalPontEarned",totalPontEarned.toString())
                it.putExtra("status",status.toString())
                it.putExtra("correctAns",correctAns.toString())
                it.putExtra("size",list.size.toString())
            })
            activity.finish()
        }
        else
        {
            if (currentQuestionIndex != list.size) {
                if (list[currentQuestionIndex!!].type=="multiple")
                {
                    val time = setTimeForQuestion(list[currentQuestionIndex!!].difficulty)
                 //   questionTime.value = time
                    setTimer(time.toInt())
                    condownTimer.start()

                 //   condownTimer.start()
                 //   setTimeForQuestion(list[currentQuestionIndex!!].difficulty)
                    isMultipleChoice.value = true
                    val questionForUis = QuestionForUi(
                        list[currentQuestionIndex!!].question,
                        list[currentQuestionIndex!!].incorrect_answers[0],
                        list[currentQuestionIndex!!].incorrect_answers[1],
                        list[currentQuestionIndex!!].incorrect_answers[2],
                        list[currentQuestionIndex!!].correct_answer)

                    questionForUi.value = questionForUis

                }
                else
                {
                  //  setTimeForQuestion(list[currentQuestionIndex!!].difficulty)
                    isMultipleChoice.value = false
                    val time = setTimeForQuestion(list[currentQuestionIndex!!].difficulty)
                        //   questionTime.value = time
                    setTimer(time.toInt())
                    condownTimer.start()

                    val questionForUis = QuestionForUi(
                        list[currentQuestionIndex!!].question,
                        list[currentQuestionIndex!!].incorrect_answers[0],
                        list[currentQuestionIndex!!].correct_answer,
                        "","")
                    questionForUi.value = questionForUis
                }

            }

        }
    }

    private fun setTimeForQuestion(difficulty: String) : String {
       return when(difficulty)
        {
            "medium"->{
                 "30"
            }
            "hard"->{
               "45"
            }
            "easy"->{
                 "15"
            }
           else -> {
               "00"
           }
       }

    }

    private fun setTimer(time : Int){
        val timeInLong = time *1000L
     condownTimer = object : CountDownTimer(timeInLong, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val f: NumberFormat = DecimalFormat("00")
                val hour = millisUntilFinished / 3600000 % 24
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60

                questionTime.value = sec.toString()

                


            }


            override fun onFinish() {
                Log.d("timer______", "Time up")
                condownTimer.cancel()
                next()
            }
        }
    }

    suspend fun getSum() {
        var sum = 0.0
        withContext(Dispatchers.IO)
        {
             sum = quizRepo.getSum()
            sumOfValue.postValue(sum)
            Log.d("error_____",""+sum)
        }


    }



}
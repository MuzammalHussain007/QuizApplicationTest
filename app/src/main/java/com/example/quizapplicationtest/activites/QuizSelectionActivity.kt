package com.example.quizapplicationtest.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizapplicationtest.R
import com.example.quizapplicationtest.databinding.ActivityQuizSelectionBinding
import com.example.quizapplicationtest.modal.User
import com.example.quizapplicationtest.mvvm.QuizRepositry
import com.example.quizapplicationtest.mvvm.QuizRetrofitService
import com.example.quizapplicationtest.mvvm.QuizViewModalFactory
import com.example.quizapplicationtest.mvvm.viewmodal.QuizViewModal
import com.example.quizapplicationtest.room.roomClient.QuizDatabase
import com.example.quizapplicationtest.util.AppStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class QuizSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizSelectionBinding
    private var categoryid: Int? = -1
    private var difficultyid: String? = ""
    private var typeid: String? = ""
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz_selection)
        binding.user = User(
            AppStorage.getCurrentUserId()!!.toInt(),
            "",
            "",
            AppStorage.getFullName().toString()
        )

        innit()
    }

    private fun innit() {
        retrofitService = QuizRetrofitService.getInstance(this)
        quizDatabase = QuizDatabase.invoke(this)
        repository = QuizRepositry(quizDatabase, retrofitService)
        factory = QuizViewModalFactory(repository, this, this)
        quizViewModal = ViewModelProvider(this, factory)[QuizViewModal::class.java]

        AppStorage.init(this)
        ArrayAdapter.createFromResource(this!!, R.array.category, R.layout.spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding.categoryspinner.adapter = adapter
            }
        binding.categoryspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {

                    0 -> {
//                        when((1..5).random())
//                        {
//                            1 -> {
//                                categoryid = 9
//                            }
//                            2 -> {
//                                categoryid = 17
//                            }
//                            3 -> {
//                                categoryid = 18
//                            }
//                            4 -> {
//                                categoryid = 19
//                            }
//                            5 -> {
//                                categoryid = 21
//                            }
//                        }
                    }
                    1 -> {
                        categoryid = 9

                    }
                    2 -> {
                        categoryid = 17

                    }
                    3 -> {
                        categoryid = 18
                    }
                    4 -> {
                        categoryid = 19
                    }
                    5 -> {
                        categoryid = 21
                    }

                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // another interface callback
            }
        }

        ArrayAdapter.createFromResource(this!!, R.array.diffculity, R.layout.spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding.spinnerDifficulty.adapter = adapter
            }
        binding.spinnerDifficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {

                    0 -> {
//                        when((1..3).random())
//                        {
//                            1 -> {
//                                difficultyid = "easy"
//                            }
//                            2 -> {
//                                difficultyid = "medium"
//                            }
//                            3 -> {
//                                difficultyid = "hard"
//                            }
//                        }
                    }
                    1 -> {
                        difficultyid = "easy"

                    }
                    2 -> {
                        difficultyid = "medium"

                    }
                    3 -> {
                        difficultyid = "hard"
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // another interface callback
            }
        }

        binding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId -> // on below line we are getting radio button from our group.
            val radioButton = findViewById<RadioButton>(checkedId)
            val radioBtnName = radioButton.text.toString()
            typeid = when(radioBtnName) {
                "True/False"->{
                    "boolean"
                }
                "Multiple Choice"->{
                    "multiple"
                }else->{
                    ""
                }
            }
        })

        cliclListener()

    }

    private fun cliclListener() {
        binding.startQuiz.setOnClickListener {
            hitQuizApi(categoryid,difficultyid,typeid)
        }
    }

    private fun hitQuizApi(categoryid: Int?, difficultyid: String?, typeid: String?) {
        if (categoryid==null && difficultyid!!.isEmpty() && typeid!!.isEmpty())
        {
            Toast.makeText(applicationContext,"Fill at least one field",Toast.LENGTH_SHORT).show()
        }else
        {
            val amount = binding.noOfQuestion.text.toString()
            startActivity(Intent(this,QuizActivity::class.java).also {
                it.putExtra("amount",amount.toString())
                it.putExtra("categoryid",categoryid.toString())
                it.putExtra("difficultyid",difficultyid)
                it.putExtra("typeid",typeid)
            })
        }

    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            quizViewModal.getSum()
        }
        binding.noOfQuestion.setText("")
        binding.categoryspinner.setSelection(0)
        binding.spinnerDifficulty.setSelection(0)
        binding.trueFalseCheckBox.isChecked = false
        binding.multipleChoiceCheckBox.isChecked = false

        quizViewModal.sumOfValue.observe(this){
            binding.points.text = it.toString()
        }


    }
}
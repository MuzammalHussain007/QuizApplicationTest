package com.example.quizapplicationtest.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapplicationtest.R
import com.example.quizapplicationtest.databinding.CustomResultBinding
import com.example.quizapplicationtest.modal.History

class HistoryAdapter(
    private val baseActivity: Activity,
    private val list: ArrayList<History>,


    ) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder?>() {



    inner class HistoryViewHolder(val binding: CustomResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = DataBindingUtil.inflate<CustomResultBinding>(
            LayoutInflater.from(baseActivity), R.layout.custom_result, parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: HistoryViewHolder, @SuppressLint("RecyclerView") position: Int
    ) {
        holder.binding.history = list[position]
    }

    companion object{
        @BindingAdapter("setCustomCorrectAnswer")
        @JvmStatic
        fun setCustomCorrectAnswer(textView : TextView , history: History)
        {
            textView.text = "${history.correctAns} out of ${history.size}"
        }

        @BindingAdapter("setCustomCorrectTime")
        @JvmStatic
        fun setCustomCorrectTime(textView : TextView , history: History)
        {
            val second =  (history.totalTime.toLong() / 1000) % 60
            val minute =  (history.totalTime.toLong() / 1000*60) % 60
            val hrs =  (history.totalTime.toLong() / 1000*3600) % 24
            textView.text = "$hrs : $minute : $second"
        }
    }
}
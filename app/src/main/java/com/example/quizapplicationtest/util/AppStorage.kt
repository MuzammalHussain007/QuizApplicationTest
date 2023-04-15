package com.example.quizapplicationtest.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object AppStorage {
    private lateinit var prefs: SharedPreferences



    fun init(context: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)

    }

    fun setCurrentUserId(id: String) {
        prefs.edit().putString("user", id).apply()
    }
    fun setCurrentPoints(no: String) {
        prefs.edit().putString("points", no).apply()
    }
    fun getCurrentPoints(): String? {
        return prefs.getString("points","0")
    }

    fun setFullName(name: String) {
        prefs.edit().putString("fullname", name).apply()
    }

    fun getFullName(): String? {
        return prefs.getString("fullname","0")
    }
    fun getCurrentUserId(): String? {
        return prefs.getString("user","0")
    }

    fun deletePreference()
    {
        prefs.edit().clear().apply()
    }



}
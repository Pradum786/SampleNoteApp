package com.pradum786.samplenote.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences("PREFS_TOKEN_FILE", Context.MODE_PRIVATE)

    fun saveToken(token:String){
            val edit=prefs.edit()
        edit.putString("USER_TOKEN",token)
        edit.apply()
    }

    fun getToken() : String? {
        return prefs.getString("USER_TOKEN",null)
    }

}
package com.example.polyschedule.data

import android.content.Context


object CacheUtils {
    fun setString(key: String?, text: String?, context: Context) {
        val editor = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE).edit()
        editor.putString(key, text)
        editor.apply()
    }


    fun removeString(key: String?, context: Context) {
        val editor = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE).edit()
        editor.remove(key)
        editor.apply()
    }

    fun hasKey(key: String?, context: Context): Boolean {
        val pref = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE)
        return pref.contains(key)
    }

    fun getBoolean(name: String?, context: Context): Boolean {
        val pref = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE)
        return pref.getBoolean(name, false)
    }

    fun getString(name: String?, context: Context): String? {
        val pref = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE)
        return pref.getString(name, null)
    }

    fun setBoolean(key: String?, bool: Boolean?, context: Context) {
        val editor = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE).edit()
        editor.putBoolean(key, bool!!)
        editor.apply()
    }

    fun clean(context: Context) {
        val editor = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }






    const val SESSION = "SESSION"
    private const val IDENTIFIER = "APP_SETTINGS"

    const val DIRECTION = "direction"
    const val REGISTRATION_TOKEN = "registration_token"




}

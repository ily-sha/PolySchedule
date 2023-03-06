package com.example.polyschedule.data

import android.content.Context


class CacheUtils {
    // Запись строки по ключу
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

    // Получение boolean по ключу
    fun getBoolean(name: String?, context: Context): Boolean {
        val pref = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE)
        return pref.getBoolean(name, false)
    }

    // Получение строки по ключу
    fun getString(name: String?, context: Context): String? {
        val pref = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE)
        return pref.getString(name, null)
    }

    // Запись boolean по ключу
    fun setBoolean(key: String?, bool: Boolean?, context: Context) {
        val editor = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE).edit()
        editor.putBoolean(key, bool!!)
        editor.apply()
    }

    // Стереть все сохранённые данные
    fun clean(context: Context) {
        val editor = context.getSharedPreferences(IDENTIFIER, Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        const val SESSION = "SESSION"
        private const val IDENTIFIER = "APP_SETTINGS"

        const val GROUP_KEY = "group"
        const val INSTITUTE_KEY = "institute"
        const val COURSE_KEY = "course"


        // Инициализация контекста
        var instance: CacheUtils? = null
            get() {
                if (field == null) {
                    field = CacheUtils()
                }
                return field
            }
            private set
    }
}

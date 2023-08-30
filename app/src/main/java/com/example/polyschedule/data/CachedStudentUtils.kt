package com.example.polyschedule.data

import android.content.Context

data class CachedStudent(
    val token: String,
    val name: String,
    val surname: String,
    val email: String,
    val studentGroups: Map<Int, String>
)

object CachedStudentUtils {


    private const val NAME = "name"
    private const val SURNAME = "surname"
    private const val EMAIL = "email"
    private const val TOKEN = "token"
    private const val GROUPS = "groups"
    private const val HAS_ACCOUNT = "has_account"
    private const val HAS_GROUPS = "has_groups"
    fun setUserInfo(
        token: String,
        name: String,
        surname: String,
        email: String,
        studentGroups: Map<Int, String>,
        applicationContext: Context
    ) {
        CacheUtils.setString(TOKEN, token, applicationContext)
        CacheUtils.setString(NAME, name, applicationContext)
        CacheUtils.setString(SURNAME, surname, applicationContext)
        CacheUtils.setString(EMAIL, email, applicationContext)
        CacheUtils.setBoolean(HAS_ACCOUNT, true, applicationContext)
        if (studentGroups.isNotEmpty()) {
            CacheUtils.setBoolean(HAS_GROUPS, true, applicationContext)
            CacheUtils.setString(
                GROUPS,
                studentGroups.entries.joinToString(separator = ";") { "${it.key}:${it.value}" },
                applicationContext
            )
        } else {
            CacheUtils.setBoolean(HAS_GROUPS, false, applicationContext)
            CacheUtils.removeString(GROUPS, applicationContext)
        }
    }

    fun updateStudentGroups(studentGroups: Map<Int, String>, applicationContext: Context) {
        if (studentGroups.isEmpty()) {
            CacheUtils.setBoolean(HAS_GROUPS, false, applicationContext)
            CacheUtils.removeString(GROUPS, applicationContext)
        } else {
            CacheUtils.setBoolean(HAS_GROUPS, true, applicationContext)
            CacheUtils.removeString(GROUPS, applicationContext)

            CacheUtils.setString(
                GROUPS, studentGroups.entries.joinToString(separator = ";") { "${it.key}:${it.value}" },
                applicationContext
            )
        }
    }

    fun getToken(applicationContext: Context) = CacheUtils.getString(TOKEN, applicationContext)


    fun removeUserInfo(applicationContext: Context) {
        CacheUtils.removeString(TOKEN, applicationContext)
        CacheUtils.removeString(NAME, applicationContext)
        CacheUtils.removeString(SURNAME, applicationContext)
        CacheUtils.removeString(EMAIL, applicationContext)
        CacheUtils.removeString(GROUPS, applicationContext)
        CacheUtils.setBoolean(HAS_ACCOUNT, false, applicationContext)
        CacheUtils.setBoolean(HAS_GROUPS, false, applicationContext)
    }

    fun getStudentInfo(applicationContext: Context): CachedStudent? {
        if (CacheUtils.hasKey(HAS_ACCOUNT, applicationContext)) {
            val name = CacheUtils.getString(NAME, applicationContext) ?: return null
            val surname = CacheUtils.getString(SURNAME, applicationContext) ?: return null
            val email = CacheUtils.getString(EMAIL, applicationContext) ?: return null
            val token = CacheUtils.getString(TOKEN, applicationContext) ?: return null
            val groups =
                CacheUtils.getString(GROUPS, applicationContext)?.split(";")?.associate { val (key, value) = it.split(":")
                    key.toInt() to value}
                    ?: mapOf()
            return CachedStudent(
                token = token,
                name = name,
                surname = surname,
                email = email,
                studentGroups = groups
            )
        }
        return null

    }
}


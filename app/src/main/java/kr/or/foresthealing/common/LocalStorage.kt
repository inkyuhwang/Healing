package kr.or.foresthealing.common

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kr.or.foresthealing.ext.parseJsonData
import kr.or.foresthealing.model.Quiz

/**
 * SharedPreferences LocalStorage
 */
class LocalStorage {

    companion object {
        /**
         * Single Instance
         */
        val instance: LocalStorage by lazy {
            LocalStorage()
        }
    }

    /**
     * LocalStorage Keys
     */
    private enum class Key {
        team_id, quiz, current_quiz_id
    }

    /**
     * SharedPreferences
     */
    private lateinit var sharedPreferences: SharedPreferences
    /**
     * SharedPreferences use context
     */
    lateinit var context: Context

    fun init(context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)
    }

    /**
     * 팀 아이디
     */
    var teamID: Int
        get() = sharedPreferences.getInt(Key.team_id.name, -1)
        set(value) = sharedPreferences.edit().putInt(Key.team_id.name, value).apply()

    var currentQuizID: Int
        get() = sharedPreferences.getInt(Key.current_quiz_id.name, -1)
        set(value) = sharedPreferences.edit().putInt(Key.current_quiz_id.name, value).apply()


    var quiz: Quiz
        get() {
            val data = sharedPreferences.getString(Key.quiz.name, "{}") ?: "{}"
            return data.parseJsonData(Quiz::class.java)
        }

        set(value) {
            val data = Gson().toJson(value)
            sharedPreferences.edit().putString(Key.quiz.name, data).apply()
        }


}
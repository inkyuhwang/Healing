package kr.or.foresthealing.common

import android.content.Intent
import kr.or.foresthealing.model.Quiz
import kr.or.foresthealing.ui.activity.StampActivity

class Utils{
    companion object{
        fun replaceUrl(data : Quiz) : Quiz{
            val quiz = data.data
            quiz.forEach {
                it.guide = it.guide.replace("http://localhost:8080", "")
                it.map = it.map.replace("http://localhost:8080", "")
                it.video = it.video.replace("http://localhost:8080", "")
            }
            data.data = quiz
            return data
        }
    }
}
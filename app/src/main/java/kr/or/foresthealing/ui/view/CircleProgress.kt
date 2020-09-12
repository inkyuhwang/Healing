package kr.or.foresthealing.ui.view

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

class CircleProgress(context: Context) : CircularProgressDrawable(context) {

    init{
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }
}
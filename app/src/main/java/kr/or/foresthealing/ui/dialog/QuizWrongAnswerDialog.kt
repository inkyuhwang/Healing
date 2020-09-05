package kr.or.foresthealing.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.ImageButton
import kotlinx.android.synthetic.main.dialog_intro_confirm.*
import kotlinx.android.synthetic.main.dialog_quiz_confirm.*
import kotlinx.android.synthetic.main.dialog_quiz_wrong_answer.*
import kr.or.foresthealing.R


class QuizWrongAnswerDialog(context: Context) : Dialog(context) {

    constructor(context: Context,
                hint:String):this(context){
        mHint = hint
    }

    fun setPositiveListener(positiveListener: View.OnClickListener?){
        mPositiveListener = positiveListener
    }

    private var mPositiveButton: ImageButton? = null
    private var mPositiveListener: View.OnClickListener? = null

    private var mHint : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_quiz_wrong_answer)

        mPositiveButton = findViewById<ImageButton>(R.id.quiz_wrong_answer_retry_btn)
        mPositiveButton?.setOnClickListener(mPositiveListener)

        quiz_wrong_dialog_info.text = mHint
    }
}
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
import kr.or.foresthealing.R


class IntroConfirmDialog(context: Context) : Dialog(context) {

    constructor(context: Context,
                teamName:String,
                helper:String,
                helperTel:String):this(context){
        mTeamName = teamName
        mHelper = helper
        mHelperTel = helperTel
    }

    fun setPositiveListener(positiveListener: View.OnClickListener?){
        mPositiveListener = positiveListener
    }
    fun setNegativeListener(negativeListener: View.OnClickListener?){
        mNegativeListener = negativeListener
    }

    private var mPositiveButton: Button? = null
    private var mNegativeButton: Button? = null

    private var mPositiveListener: View.OnClickListener? = null
    private var mNegativeListener: View.OnClickListener? = null

    private var mTeamName : String? = null
    private var mHelper : String? = null
    private var mHelperTel : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_intro_confirm)

        mPositiveButton = findViewById<Button>(R.id.intro_confirm_positive)
        mNegativeButton = findViewById<Button>(R.id.intro_confirm_negative)

        mPositiveButton?.setOnClickListener(mPositiveListener)
        mNegativeButton?.setOnClickListener(mNegativeListener)

        val infoMsg = String.format(context.resources.getString(R.string.intro_confirm_dialog_info_format), mTeamName, mHelper, mHelperTel)
        intro_confirm_dialog_info.text = infoMsg

    }
}
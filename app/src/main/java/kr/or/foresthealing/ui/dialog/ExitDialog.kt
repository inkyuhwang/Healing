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


class ExitDialog(context: Context) : Dialog(context) {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_exit)

        mPositiveButton = findViewById<Button>(R.id.exit_confirm_positive)
        mNegativeButton = findViewById<Button>(R.id.exit_confirm_negative)

        mPositiveButton?.setOnClickListener(mPositiveListener)
        mNegativeButton?.setOnClickListener(mNegativeListener)

    }
}
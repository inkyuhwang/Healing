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
import kotlinx.android.synthetic.main.dialog_wrong_qr.*
import kr.or.foresthealing.R


class WrongQRDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_wrong_qr)

        wrong_qr_confirm.setOnClickListener{
            dismiss()
        }
    }
}
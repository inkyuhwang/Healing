package kr.or.foresthealing.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_confirm.*
import kr.or.foresthealing.R


class ConfirmDialog(context: Context, msg:String, clickListener : View.OnClickListener?) : Dialog(context) {

    var mMsg = msg
    var mListener = clickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_confirm)

        confirm_text.text = mMsg

        btn_confirm.setOnClickListener{
            dismiss()
            mListener?.onClick(btn_confirm)
        }
    }
}
package kr.or.foresthealing.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_permission_deny.*

import kotlinx.android.synthetic.main.dialog_wrong_qr.*
import kr.or.foresthealing.R


class PermissionDenyDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_permission_deny)
        setCancelable(false)

        permission_deny_confirm.setOnClickListener{
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
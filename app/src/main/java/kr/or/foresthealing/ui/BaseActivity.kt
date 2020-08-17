package kr.or.foresthealing.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.or.foresthealing.R
import kr.or.foresthealing.ui.dialog.ExitDialog

open class BaseActivity : AppCompatActivity(){

    var toast:Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        toast = Toast(this)
        toast?.setGravity(Gravity.CENTER, 0, 390)
        val layout = layoutInflater.inflate(
            R.layout.toast_intro,
            findViewById<ViewGroup>(R.id.intro_toast_layout)
        )
        toast?.view = layout

    }

    override fun onBackPressed() {

        val exitDialog = ExitDialog(this)
        exitDialog.setNegativeListener(View.OnClickListener {
            exitDialog.dismiss()
        })

        exitDialog.setPositiveListener(View.OnClickListener {
            exitDialog.dismiss()
            finish()
            android.os.Process.killProcess(android.os.Process.myPid());
        })
        exitDialog.show()
    }

    open fun showCustomToast(msg:String){
        toast?.view?.findViewById<TextView>(R.id.intro_toast_text)?.text = msg
        toast?.show()
    }
}
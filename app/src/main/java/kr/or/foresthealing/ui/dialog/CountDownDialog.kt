package kr.or.foresthealing.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlinx.android.synthetic.main.dialog_countdown.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.LocalStorage


class CountDownDialog(context: Context) : Dialog(context) {

    private val mHandler = Handler(Looper.getMainLooper()){
        when(it.what){
            Const.COUNT_DOWN->{
                if(remainTime <= 0){
                    dismiss()
                    LocalStorage.instance.showCountDown = false
                }
                remainTime--
                countdown()
            }
        }
        true
    }

    private var remainTime = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(R.layout.dialog_countdown)
    }

    override fun show() {
        LocalStorage.instance.showCountDown = true
        remainTime = 30
        super.show()
        mHandler.postDelayed(Runnable {countdown()}, 1000)

    }
    private fun countdown(){
        mHandler.sendEmptyMessageDelayed(Const.COUNT_DOWN, 1000)
        count_number.text = "$remainTime"
    }
}
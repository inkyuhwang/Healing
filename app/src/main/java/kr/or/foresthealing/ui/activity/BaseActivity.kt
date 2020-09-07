package kr.or.foresthealing.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.os.Process
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.ExceptionHandler
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.model.Quiz
import kr.or.foresthealing.observer.CommonObserver
import kr.or.foresthealing.ui.dialog.ConfirmDialog
import kr.or.foresthealing.ui.dialog.ExitDialog
import java.util.*

open class BaseActivity : AppCompatActivity(), Observer{

    private var toast:Toast? = null
    open var mQuiz : Quiz.Data = Quiz.Data()

    val baseHandler = Handler(Looper.getMainLooper()){
        when(it.what){
            Const.FINISH_ACTIVITES->{
                finish()
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonObserver.instance.addObserver(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        toast = Toast(this)
        toast?.setGravity(Gravity.CENTER, 0, 390)
        val layout = layoutInflater.inflate(
            R.layout.toast_intro,
            findViewById<ViewGroup>(R.id.intro_toast_layout)
        )
        toast?.view = layout

        val quiz = LocalStorage.instance.quiz.data
        val filterdList = quiz.filter {
            it.question_id == LocalStorage.instance.currentQuizID
        }
        if(filterdList.isNotEmpty()){
            mQuiz = quiz.filter {
                it.question_id == LocalStorage.instance.currentQuizID
            }[0]
        }
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

    override fun onDestroy() {
        CommonObserver.instance.deleteObserver(this)
        super.onDestroy()
    }

    open fun showNetworkErrorDialog(msg:String, listener:View.OnClickListener?){
        val dialog = ConfirmDialog(this, msg, listener)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun update(o: Observable?, data: Any?) {
        when(data){
            Const.FINISH_ACTIVITES->{
                finish()
            }
        }
    }
}
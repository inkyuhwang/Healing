package kr.or.foresthealing.ui.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_intro.*
import kr.or.foresthealing.R
import kr.or.foresthealing.ui.dialog.IntroConfirmDialog

class IntroActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent(IntroActivity@this, MapActivity::class.java).let {
            startActivity(it)
            finish()
            return
        }


        setContentView(R.layout.activity_intro)



        btn_start.setOnClickListener{

            input_team_name.setText("테스트팀")
            input_helper_name.setText("테스트헬퍼")
            input_helper_tel.setText("1234-1234")

            if(input_team_name.text.trim().isEmpty()) {
                showCustomToast(getString(R.string.hint_intro_input_name))
                return@setOnClickListener
            }
            if(input_helper_name.text.trim().isEmpty()) {
                showCustomToast(getString(R.string.hint_intro_input_helper))
                return@setOnClickListener
            }
            if(input_helper_tel.text.trim().isEmpty()) {
                showCustomToast(getString(R.string.hint_intro_input_helper_tel))
                return@setOnClickListener
            }

            showStartDialog()
        }

    }

    private fun showStartDialog(){
        val startDialog = IntroConfirmDialog(this,
            input_team_name.text.toString(),
            input_helper_name.text.toString(),
            input_helper_tel.text.toString())

        startDialog.setPositiveListener(View.OnClickListener {
            startDialog.dismiss()
            Intent(IntroActivity@this, QuizActivity::class.java).let {
                startActivity(it)
                finish()
            }
        })
        startDialog.setNegativeListener(View.OnClickListener {
            startDialog.dismiss()
        })
        startDialog.show()
    }


    override fun onResume() {
        super.onResume()
        (logo_icon.background as AnimationDrawable).start()
    }

    override fun onPause() {
        super.onPause()
        (logo_icon.background as AnimationDrawable).stop()
    }
}
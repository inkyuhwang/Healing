package kr.or.foresthealing.ui.activity

import android.animation.Animator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_map.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.ui.dialog.ConfirmDialog


class MapActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        LocalStorage.instance.currentStep = Const.STEP_MAP

        initAnimation()

        Glide.with(this).load(Const.SERVER + mQuiz.map).into(map_image_view)

        qr_btn.setOnClickListener {
            IntentIntegrator(this).apply {
                setOrientationLocked(true)
                setBeepEnabled(false)
                captureActivity = CaptureActivityPortrait::class.java
                initiateScan()
            }
        }

    }

    private fun initAnimation(){
        map_con3.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                map_con3.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                map_con3.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        result?.let{
            result.contents?.let {

                if(Uri.parse(it).toString() == mQuiz!!.video){
                    startActivity(Intent(MapActivity@this, MissionActivity::class.java))
                    finish()
                }else{
                    ConfirmDialog(this, getString(R.string.wrong_qr_msg), null).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
package kr.or.foresthealing.ui.activity

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_map.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.Hlog
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.ui.dialog.ConfirmDialog
import kr.or.foresthealing.ui.view.CircleProgress


class MapActivity : BaseActivity(){

    private var mPlayer : MediaPlayer? = null
    private var mWrongSoundPlayer : MediaPlayer? = null
    private var mVibrator : Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        LocalStorage.instance.currentStep = Const.STEP_MAP

        playCheerSound()
        initAnimation()

        mVibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        Glide.with(this).load(Const.SERVER + mQuiz.map)
            .placeholder(CircleProgress(this))
            .into(map_image_view)

        qr_btn.setOnClickListener {
            IntentIntegrator(this).apply {
                setOrientationLocked(true)
                setBeepEnabled(false)
                captureActivity = CaptureActivityPortrait::class.java
                initiateScan()
            }
        }
    }

    private fun playCheerSound(){
        mPlayer = MediaPlayer.create(MapActivity@this, R.raw.cheer)
        mPlayer?.start()

        mWrongSoundPlayer = MediaPlayer.create(MapActivity@this, R.raw.wrong_answer_sound)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer?.release()
        mWrongSoundPlayer?.release()
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
                    mWrongSoundPlayer?.start()
                    mVibrator?.vibrate(VibrationEffect.createOneShot(1000, 255))

                    ConfirmDialog(this, getString(R.string.wrong_qr_msg), null).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
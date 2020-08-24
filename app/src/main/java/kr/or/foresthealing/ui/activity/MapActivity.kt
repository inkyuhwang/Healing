package kr.or.foresthealing.ui.activity

import android.animation.Animator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_map.*
import kr.or.foresthealing.R


class MapActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_map)
        initAnimation()

        qr_btn.setOnClickListener {
            val qrScan = IntentIntegrator(this).apply {
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
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(it), "video/mp4")
                startActivity(intent)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
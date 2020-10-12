package kr.or.foresthealing.ui.activity

import android.Manifest
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_splash.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.Hlog
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.common.Utils
import kr.or.foresthealing.ext.parseJsonData
import kr.or.foresthealing.model.Quiz
import kr.or.foresthealing.network.NetworkHandler
import kr.or.foresthealing.network.NetworkManager
import kr.or.foresthealing.ui.dialog.PermissionDenyDialog


class SplashActivity : BaseActivity(){

    private fun clearData(){
        (getSystemService(ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
        LocalStorage.instance.clear()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //clearData()

        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            getPermission()
        }, 2000)
    }

    private fun init(){

        checkLastStep()
    }

    private fun checkLastStep(){
        //이전에 종료한 이력이 있어 이어하기를 해야하는 상황인지 확인
        var intent : Intent? = when(LocalStorage.instance.currentStep){
            Const.STEP_INTRO -> Intent(IntroActivity@ this, IntroActivity::class.java)
            Const.STEP_QUIZ -> Intent(IntroActivity@ this, QuizActivity::class.java)
            Const.STEP_MAP -> Intent(IntroActivity@ this, MapActivity::class.java)
            Const.STEP_MISSION -> Intent(IntroActivity@ this, MissionActivity::class.java)
            Const.STEP_STAMP -> Intent(IntroActivity@ this, StampActivity::class.java)
            else -> null
        }
        intent?.let {
            startActivity(it)
            finish()
        }
    }

    private fun getPermission(){
        if(checkPermission()){
            init()
        }else{
            requestPermission()
        }
    }

    //카메라 권한 요청
    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ), 1
        )
    }

    //카메라 권한 체크
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    // 권한요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            init()
        }else{
            PermissionDenyDialog(this).show()
        }
    }

    override fun onResume() {
        super.onResume()
        (splash_icon.background as AnimationDrawable).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        (splash_icon.background as AnimationDrawable).stop()
    }

    override fun onBackPressed() {}

}
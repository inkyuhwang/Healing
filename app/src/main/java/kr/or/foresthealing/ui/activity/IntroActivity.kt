package kr.or.foresthealing.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.loopj.android.http.RequestParams
import kotlinx.android.synthetic.main.activity_intro.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.Hlog
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.common.Utils
import kr.or.foresthealing.ext.parseJsonData
import kr.or.foresthealing.model.Api
import kr.or.foresthealing.model.Quiz
import kr.or.foresthealing.model.TeamNew
import kr.or.foresthealing.network.NetworkHandler
import kr.or.foresthealing.network.NetworkManager
import kr.or.foresthealing.ui.dialog.CountDownDialog
import kr.or.foresthealing.ui.dialog.IntroConfirmDialog
import kr.or.foresthealing.ui.dialog.PermissionDenyDialog

class IntroActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //LocalStorage.instance.clear()
        setContentView(R.layout.activity_intro)
        getPermission()
    }

    private fun init(){

        checkContinue()

        btn_start.setOnClickListener{

            /*input_team_name.setText("테스트팀3")
            input_helper_name.setText("테스트헬퍼3")
            input_helper_tel.setText("1234-1234 3")*/

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

    //이전에 종료한 이력이 있어 이어하기를 해야하는 상황인지 확인
    private fun checkContinue(){

        var intent : Intent? = when(LocalStorage.instance.currentStep){
            Const.STEP_INTRO -> null
            Const.STEP_QUIZ -> Intent(IntroActivity@this, QuizActivity::class.java)
            Const.STEP_MAP -> Intent(IntroActivity@this, MapActivity::class.java)
            Const.STEP_MISSION -> Intent(IntroActivity@this, MissionActivity::class.java)
            Const.STEP_STAMP -> Intent(IntroActivity@this, StampActivity::class.java)
            else -> null
        }
        intent?.let {
            startActivity(it)
            finish()
        }
    }

    private fun showStartDialog(){
        val startDialog = IntroConfirmDialog(this,
            input_team_name.text.toString(),
            input_helper_name.text.toString(),
            input_helper_tel.text.toString())

        startDialog.setPositiveListener(View.OnClickListener {
            startDialog.dismiss()
            sendTeamInfo()
        })
        startDialog.setNegativeListener(View.OnClickListener {
            startDialog.dismiss()
        })
        startDialog.show()
    }

    private fun sendTeamInfo(){

        val param = RequestParams().apply {
            put("team", input_team_name.text.toString())
            put("manager", input_helper_name.text.toString())
            put("phone", input_helper_tel.text.toString())
        }
        NetworkManager.getInstance().post(Const.URL_REGIST_TEAM, param, object: NetworkHandler {
            override fun onSuccess(result: String) {
                val team = result.parseJsonData(TeamNew::class.java)
                team.data?.id.let {
                    LocalStorage.instance.teamID = team.data!!.id
                }
                if(LocalStorage.instance.teamID != -1){
                    requestQuiz()
                }else{
                    //에러 처리 필요.
                }

            }

            override fun onFail(statusCode : Int, result:String) {
                Toast.makeText(this@IntroActivity, String.format(getString(R.string.network_error), statusCode), Toast.LENGTH_LONG).show()
            }

        })
    }
    private fun requestQuiz(){
        val context = IntroActivity@this
        NetworkManager.getInstance().post(Const.URL_QUIZLIST_ALL, null, object: NetworkHandler {
            override fun onSuccess(result: String) {
                Hlog.e(result)

                val quiz = Utils.replaceUrl(result.parseJsonData(Quiz::class.java))
                val data = quiz.data.toMutableList().apply { shuffle() }
                quiz.data = data.toTypedArray()

                LocalStorage.instance.quiz = quiz

                Intent(context, QuizActivity::class.java).let {
                    startActivity(it)
                    finish()
                }

            }

            override fun onFail(statusCode : Int, result:String) {
                showNetworkErrorToast(statusCode)
            }
        })
    }

    private fun getApiList(){
        NetworkManager.getInstance().post(Const.URL_API_LIST, null, object: NetworkHandler {
            override fun onSuccess(result: String) {
                val api = result.parseJsonData(Api::class.java)
            }

            override fun onFail(statusCode : Int, result:String) {
                Toast.makeText(this@IntroActivity, String.format(getString(R.string.network_error), statusCode), Toast.LENGTH_LONG).show()
            }
        })
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
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 1)
    }

    //카메라 권한 체크
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
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
        (logo_icon.background as AnimationDrawable).start()
    }

    override fun onPause() {
        super.onPause()
        (logo_icon.background as AnimationDrawable).stop()
    }
}
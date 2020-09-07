package kr.or.foresthealing.ui.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.loopj.android.http.RequestParams
import kotlinx.android.synthetic.main.activity_intro.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.ext.parseJsonData
import kr.or.foresthealing.model.Api
import kr.or.foresthealing.model.TeamNew
import kr.or.foresthealing.network.NetworkHandler
import kr.or.foresthealing.network.NetworkManager
import kr.or.foresthealing.ui.dialog.IntroConfirmDialog

class IntroActivity : BaseActivity(){

    private var mPlayer : MediaPlayer? = null
    private var mPlayerSeekPosition = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        LocalStorage.instance.currentStep = Const.STEP_INTRO

        mPlayer = MediaPlayer.create(MapActivity@this, R.raw.forest_sound_1)
        mPlayer?.isLooping = true
        mPlayer?.start()

        init()
    }

    private fun init(){
        btn_start.setOnClickListener{

            input_team_name.setText("abc 5")
            input_helper_name.setText("테스트헬퍼3")
            input_helper_tel.setText("1234-1234 3")

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
            sendTeamInfo()
        })
        startDialog.setNegativeListener(View.OnClickListener {
            startDialog.dismiss()
        })
        startDialog.show()
    }

    private fun sendTeamInfo(){
        val context = IntroActivity@this
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
                    //팀 데이터를 받아오면 퀴즈 엑티비티로 보냄
                    startActivity(Intent(context, QuizActivity::class.java))
                }else{
                    val msg = getString(R.string.network_error) + "TeamID = ${LocalStorage.instance.teamID}"
                    showNetworkErrorDialog(msg, View.OnClickListener {
                        finish()
                        android.os.Process.killProcess(android.os.Process.myPid())
                    })
                }

            }
            override fun onFail(statusCode : Int, result:String) {
                val msg = getString(R.string.network_error) + statusCode
                showNetworkErrorDialog(msg, View.OnClickListener {
                    finish()
                    android.os.Process.killProcess(android.os.Process.myPid())
                })
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer?.stop()
        mPlayer?.release()
        (logo_icon.background as AnimationDrawable).stop()
    }

    override fun onResume() {
        super.onResume()
        mPlayer?.seekTo(mPlayerSeekPosition)
        mPlayer?.start()
        (logo_icon.background as AnimationDrawable).start()
    }

    override fun onPause() {
        super.onPause()
        mPlayer?.pause()
        mPlayerSeekPosition = mPlayer?.currentPosition ?: 0
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
}
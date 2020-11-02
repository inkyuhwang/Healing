package kr.or.foresthealing.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.loopj.android.http.RequestParams
import kotlinx.android.synthetic.main.activity_mission.*
import kr.or.foresthealing.BuildConfig
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.Hlog
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.ext.parseJsonData
import kr.or.foresthealing.model.Api
import kr.or.foresthealing.network.NetworkHandler
import kr.or.foresthealing.network.NetworkManager
import kr.or.foresthealing.ui.dialog.ConfirmDialog
import kr.or.foresthealing.ui.dialog.PreviewDialog
import kr.or.foresthealing.ui.dialog.WaitDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MissionActivity : BaseActivity(), View.OnClickListener{

    lateinit var currentPhotoPath: String

    var mIsVideoClicked = false
    var mIsGuideClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission)
        LocalStorage.instance.currentStep = Const.STEP_MISSION

        btn_play_video.setOnClickListener(this)
        btn_mission_guide.setOnClickListener(this)
        btn_sc.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_play_video ->{
                Intent(Intent.ACTION_VIEW).let {
                    val url = Const.SERVER + mQuiz.video
                    it.setDataAndType(Uri.parse(url), "video/mp4")
                    startActivity(it)
                }
                mIsVideoClicked = true
            }
            R.id.btn_mission_guide ->{
                /**
                 * 비디오 시청을 먼저 해야 미션 가이드를 볼 수 있다.
                 */
                if(!mIsVideoClicked){
                    showCommonDialog(getString(R.string.mission_dialog_need_show_video), null)
                }else{
                    startActivity(Intent(MissionActivity@this, MissionGuideActivity::class.java))
                    mIsGuideClicked = true
                }
            }
            R.id.btn_sc ->{
                if(!mIsVideoClicked){
                    showCommonDialog(getString(R.string.mission_dialog_need_show_video), null)
                }else if(!mIsGuideClicked){
                    showCommonDialog(getString(R.string.mission_dialog_need_show_guide), null)
                }else{
                    startCamera()
                }
            }
        }
    }

    private fun startCamera(){

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${BuildConfig.APPLICATION_ID}.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, Const.REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }
    private fun showPreviewDialog(){
        val context = MissionActivity@this
        val prevDialog = PreviewDialog(this, currentPhotoPath)
        prevDialog.setPreviewDialogListener(object:PreviewDialog.PreviewDialogListener{
            override fun reCapture() {
                startCamera()
            }

            override fun submit() {
                mQuiz.certImage = currentPhotoPath

                val raw = LocalStorage.instance.quiz
                val data = LocalStorage.instance.quiz.data
                data.forEachIndexed { index, quiz ->
                    if(quiz.question_id == LocalStorage.instance.currentQuizID){
                        quiz.certImage = currentPhotoPath
                        data[index] = quiz
                        return@forEachIndexed
                    }
                }
                raw.data = data
                LocalStorage.instance.quiz = raw

                uploadFile()
            }

        })
        prevDialog.show()
    }

    private fun uploadFile(){
        val waitDialog = WaitDialog(this).apply {
            show()
        }

        val context = MissionActivity@this
        val param = RequestParams().apply {
            put("file", File(currentPhotoPath))
            put("team_id", LocalStorage.instance.teamID)
        }
        NetworkManager.getInstance().post(Const. URL_FILE_UPLOAD, param, object: NetworkHandler {
            override fun onSuccess(result: String) {
                Hlog.i("file upload : $result")
                Intent(context, StampActivity::class.java).let {
                    waitDialog.dismiss()
                    startActivity(it)
                    finish()
                }

            }

            override fun onFail(statusCode : Int, result:String) {
                waitDialog.dismiss()
                val msg = getString(R.string.network_error) + statusCode
                showNetworkErrorDialog(msg, View.OnClickListener {
                    finish()
                    android.os.Process.killProcess(android.os.Process.myPid())
                })
            }
        })
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            showPreviewDialog()
        }
    }

}
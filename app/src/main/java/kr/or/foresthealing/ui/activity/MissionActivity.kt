package kr.or.foresthealing.ui.activity

import android.animation.Animator
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_mission.*
import kr.or.foresthealing.BuildConfig
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.ext.parseJsonData
import kr.or.foresthealing.model.Api
import kr.or.foresthealing.network.NetworkHandler
import kr.or.foresthealing.network.NetworkManager
import kr.or.foresthealing.ui.dialog.PreviewDialog
import kr.or.foresthealing.ui.dialog.WrongQRDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MissionActivity : BaseActivity(), View.OnClickListener{

    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission)

        btn_play_video.setOnClickListener(this)
        btn_mission_guide.setOnClickListener(this)
        btn_sc.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_play_video ->{
                Intent(Intent.ACTION_VIEW).let {
                    it.setDataAndType(Uri.parse(Const.SAMPLE_VIDEO), "video/mp4")
                    startActivity(it)
                }
            }
            R.id.btn_mission_guide ->{
                startActivity(Intent(MissionActivity@this, MissionGuideActivity::class.java))
            }
            R.id.btn_sc ->{
                startCamera()
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
        val prevDialog = PreviewDialog(this, currentPhotoPath)
        prevDialog.setPreviewDialogListener(object:PreviewDialog.PreviewDialogListener{
            override fun reCapture() {
                startCamera()
            }

            override fun submit() {
                Log.e("@@@@@", "path : $currentPhotoPath")
            }


        })
        prevDialog.show()
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
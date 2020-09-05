package kr.or.foresthealing.ui.activity

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_stamp.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.Hlog
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.model.StampItem
import kr.or.foresthealing.ui.adapter.StampAdapter

class StampActivity : BaseActivity(){

    private var mStampList = mutableListOf<StampItem>()

    private lateinit var mAdapter : StampAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stamp)
        LocalStorage.instance.currentStep = Const.STEP_STAMP

        val data = LocalStorage.instance.quiz
        val certedList = data.data.filter {
            it.certImage.isNotEmpty()
        }

        for(index in 0..17){
            val item = StampItem().apply {

                if(certedList.size > index){

                    path = certedList[index].certImage
                }
            }
            mStampList.add(item)
        }
        mAdapter = StampAdapter(this, mStampList)
        stamp_grid.adapter = mAdapter

        stamp_confirm_btn.setOnClickListener{

            val raw = LocalStorage.instance.quiz
            val data = LocalStorage.instance.quiz.data
            data.forEachIndexed { index, quiz ->
                if(quiz.question_id == LocalStorage.instance.currentQuizID){
                    quiz.complete = true
                    data[index] = quiz
                    return@forEachIndexed
                }
            }
            raw.data = data
            LocalStorage.instance.quiz = raw

            Intent(StampActivity@this, QuizActivity::class.java).let {
                startActivity(it)
                finish()
            }
        }
    }

}
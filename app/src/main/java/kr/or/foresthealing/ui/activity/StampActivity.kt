package kr.or.foresthealing.ui.activity

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.os.Process
import android.provider.MediaStore
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_stamp.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.Hlog
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.model.Quiz
import kr.or.foresthealing.model.StampItem
import kr.or.foresthealing.ui.adapter.StampAdapter
import kr.or.foresthealing.ui.dialog.ConfirmDialog

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

            next()
        }
    }

    fun next(){
        val quiz = LocalStorage.instance.quiz
        var allComplete = true

        quiz.data?.forEach {
            if(!it.complete){
                allComplete = false
                return@forEach
            }
        }
        //더이상 풀 문제가 없을때
        if(allComplete){
            ConfirmDialog(this, getString(R.string.no_exist_quiz), View.OnClickListener {
            }).show()
        }else{
            Intent(StampActivity@this, QuizActivity::class.java).let {
                startActivity(it)
                finish()
            }
        }
    }

}
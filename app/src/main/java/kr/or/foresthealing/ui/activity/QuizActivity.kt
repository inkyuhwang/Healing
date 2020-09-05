package kr.or.foresthealing.ui.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import kotlinx.android.synthetic.main.activity_quiz.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.Hlog
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.model.Quiz
import kr.or.foresthealing.ui.adapter.MChoiceAdapter
import kr.or.foresthealing.ui.dialog.ConfirmDialog
import kr.or.foresthealing.ui.dialog.CountDownDialog
import kr.or.foresthealing.ui.dialog.QuizConfirmDialog
import kr.or.foresthealing.ui.dialog.QuizWrongAnswerDialog


class QuizActivity : BaseActivity(){

    private lateinit var mChoiceAdapter : MChoiceAdapter

    private var mPlayer : MediaPlayer? = null
    private var mVibrator : Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        LocalStorage.instance.currentStep = Const.STEP_QUIZ

        mPlayer = MediaPlayer.create(MapActivity@this, R.raw.wrong_answer_sound)
        mVibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        getCurrentQuiz()

        if(mQuiz != null){
            //정상일 경우
            init()

        }else{
            //비정상일 경우 처리

        }
    }

    private fun init(){

        Hlog.e("quiz type : ${mQuiz!!.type}, ${mQuiz!!.type == Const.QUIZ_TYPE_MULTIPLE}")

        //문제 텍스트 출력
        quiz_content.text = mQuiz!!.content

        if(mQuiz!!.type == Const.QUIZ_TYPE_MULTIPLE){
            m_choice_layout.visibility = View.VISIBLE
            s_choice_layout.visibility = View.GONE
            setupRecyclerView()
        }else{
            m_choice_layout.visibility = View.GONE
            s_choice_layout.visibility = View.VISIBLE
        }

        quiz_btn_answer.setOnClickListener {

            //객관식일경우
            if(mQuiz!!.type == Const.QUIZ_TYPE_MULTIPLE){
                if(mChoiceAdapter.selectedPos == -1){
                    showCustomToast(getString(R.string.quiz_answer_no_selected))
                }else{
                    showAnswerConfirmDialog(mChoiceAdapter.mDataset[mChoiceAdapter.selectedPos])
                }

                //주관식일 경우
            }else{
                if(edittext_s_answer.text.trim().isEmpty()){
                    showCustomToast(getString(R.string.quiz_answer_no_answered))
                }else{
                    showAnswerConfirmDialog(mQuiz!!.example[0])
                }
            }
        }

        //꼼수로 오답일경우 강제종료시켜서 들어올 경우 방지
        if(LocalStorage.instance.showCountDown) CountDownDialog(this).show()

    }

    private fun getCurrentQuiz(){
        val quiz = LocalStorage.instance.quiz
        quiz.data?.forEach {
            if(!it.complete){
                mQuiz = it
                Hlog.e("get quiz : ${mQuiz.toString()}")
                mQuiz?.let {quiz ->
                    LocalStorage.instance.currentQuizID = quiz.question_id
                }
                return@getCurrentQuiz
            }
        }
        //더이상 풀 문제가 없을때
        mQuiz = Quiz.Data()
        ConfirmDialog(this, getString(R.string.no_exist_quiz), View.OnClickListener {
            finish()
            android.os.Process.killProcess(android.os.Process.myPid());
        }).show()
    }

    private fun showAnswerConfirmDialog(answer:Quiz.Example){

        var msg = answer.content
        if(mQuiz!!.type == Const.QUIZ_TYPE_SHORT){
            msg = edittext_s_answer.text.trim().toString()
        }

        val confirmDialog = QuizConfirmDialog(this, msg)

        confirmDialog.setNegativeListener(View.OnClickListener {
            confirmDialog.dismiss()
        })
        confirmDialog.setPositiveListener(View.OnClickListener {
            confirmDialog.dismiss()

            if(mQuiz!!.type == Const.QUIZ_TYPE_MULTIPLE) {
                if(answer.isAnswer == Const.QUIZ_ANSWER_CORRECT){
                    Intent(QuizActivity@this, MapActivity::class.java).let {
                        startActivity(it)
                        finish()
                    }
                }else{
                    showWrongAnswerDialog()
                }
            }else{
                if(answer.content.trim() == msg){
                    Intent(QuizActivity@this, MapActivity::class.java).let {
                        startActivity(it)
                        finish()
                    }
                }else{
                    showWrongAnswerDialog()
                }
            }
        })
        confirmDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer?.release()
    }

    private fun showWrongAnswerDialog(){
        mVibrator?.vibrate(VibrationEffect.createOneShot(1000, 255))
        mPlayer?.start()
        val hint = "[힌트] " + mQuiz!!.hint
        val wrongAnswerDialog = QuizWrongAnswerDialog(this, hint)
        wrongAnswerDialog.setPositiveListener(View.OnClickListener {
            wrongAnswerDialog.dismiss()
            CountDownDialog(this).show()
        })
        wrongAnswerDialog.show()
    }

    private fun setupRecyclerView(){
        m_choice_recycler.setHasFixedSize(true)

        mChoiceAdapter = MChoiceAdapter(mQuiz!!.example)
        m_choice_recycler.adapter = mChoiceAdapter
    }

}
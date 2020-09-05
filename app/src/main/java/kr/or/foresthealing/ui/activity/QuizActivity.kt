package kr.or.foresthealing.ui.activity

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_quiz.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.common.Hlog
import kr.or.foresthealing.common.LocalStorage
import kr.or.foresthealing.model.Quiz
import kr.or.foresthealing.ui.adapter.MChoiceAdapter
import kr.or.foresthealing.ui.dialog.QuizConfirmDialog
import kr.or.foresthealing.ui.dialog.QuizWrongAnswerDialog

class QuizActivity : BaseActivity(){

    private lateinit var mChoiceAdapter : MChoiceAdapter
    private var mQuiz : Quiz.Data? = null

    companion object{
        const val ANSWER = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

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
    }

    private fun showAnswerConfirmDialog(answer:Quiz.Example){

        val confirmDialog = QuizConfirmDialog(this, answer.content)
        confirmDialog.setNegativeListener(View.OnClickListener {
            confirmDialog.dismiss()
        })
        confirmDialog.setPositiveListener(View.OnClickListener {
            confirmDialog.dismiss()

            showWrongAnswerDialog()
        })
        confirmDialog.show()
    }

    private fun showWrongAnswerDialog(){
        val hint = "[힌트] 지능이 높은 새로도 알려져 있습니다. 무슨 새 일까요?"
        val wrongAnswerDialog = QuizWrongAnswerDialog(this, hint)
        wrongAnswerDialog.setPositiveListener(View.OnClickListener {
            wrongAnswerDialog.dismiss()
        })
        wrongAnswerDialog.show()
    }

    private fun setupRecyclerView(){
        m_choice_recycler.setHasFixedSize(true)

        mChoiceAdapter = MChoiceAdapter(mQuiz!!.example)
        m_choice_recycler.adapter = mChoiceAdapter
    }

}
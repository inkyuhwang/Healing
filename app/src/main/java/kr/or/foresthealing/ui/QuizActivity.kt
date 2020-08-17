package kr.or.foresthealing.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz.*
import kr.or.foresthealing.R
import kr.or.foresthealing.ui.adapter.MChoiceAdapter
import kr.or.foresthealing.ui.dialog.QuizConfirmDialog
import kr.or.foresthealing.ui.dialog.QuizWrongAnswerDialog

class QuizActivity : BaseActivity(){

    private lateinit var mChoiceAdapter : MChoiceAdapter

    companion object{
        var QUIZ_TYPE = "s"
        const val ANSWER = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        if(QUIZ_TYPE == "m"){
            m_choice_layout.visibility = View.VISIBLE
            s_choice_layout.visibility = View.GONE

            setupRecyclerView()
        }else{
            m_choice_layout.visibility = View.GONE
            s_choice_layout.visibility = View.VISIBLE
        }



        quiz_btn_answer.setOnClickListener {

            //객관식일경우
            if(QUIZ_TYPE == "m"){
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
                    showAnswerConfirmDialog(edittext_s_answer.text.trim().toString())
                }
            }
        }
    }

    private fun showAnswerConfirmDialog(answer:String){
        val confirmDialog = QuizConfirmDialog(this, answer)
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
        val dummy = mutableListOf<String>()
        dummy.add("1. 비둘기")
        dummy.add("2. 까마귀")
        dummy.add("3. 독수리")
        dummy.add("4. 까치")

        mChoiceAdapter = MChoiceAdapter(dummy)
        m_choice_recycler.adapter = mChoiceAdapter
    }

}
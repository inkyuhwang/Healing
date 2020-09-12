package kr.or.foresthealing.ui.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_map.*
import kr.or.foresthealing.R
import kr.or.foresthealing.common.Const
import kr.or.foresthealing.ui.view.CircleProgress


class MissionGuideActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission_guide)

        Glide.with(this).load(Const.SERVER + mQuiz.guide)
            .placeholder(CircleProgress(this))
            .into(map_image_view)
    }

    override fun onBackPressed() {
        finish()
    }
}
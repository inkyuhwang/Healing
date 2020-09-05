package kr.or.foresthealing.ui.activity

import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_stamp.*
import kr.or.foresthealing.R
import kr.or.foresthealing.model.StampItem
import kr.or.foresthealing.ui.adapter.StampAdapter

class StampActivity : BaseActivity(){

    val mPath = "/storage/emulated/0/Android/data/kr.or.foresthealing/files/Pictures/JPEG_20200905_075459_1382429633643393015.jpg"
    var mStampList = mutableListOf<StampItem>()

    private lateinit var mAdapter : StampAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stamp)
        for(i in 1..15){
            val item = StampItem().apply {
                if( i < 6){
                    path = mPath
                    quizId = i
                }
            }
            mStampList.add(item)
        }

        mAdapter = StampAdapter(this, mStampList)
        stamp_grid.adapter = mAdapter
    }

}
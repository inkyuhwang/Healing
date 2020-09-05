package kr.or.foresthealing.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.ThumbnailUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.bg_stamp_item.view.*
import kr.or.foresthealing.R
import kr.or.foresthealing.model.StampItem

class StampItemView(context: Context) : RelativeLayout(context) {

    init{
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.bg_stamp_item, this, true)
    }

    fun setItem(item : StampItem){
        item.path?.let {
            Glide.with(context).load(it).into(stamp_image)
        }
        stamp_clear.visibility = if(item.path != null) View.VISIBLE else View.GONE
    }
}
package kr.or.foresthealing.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_preview.*
import kr.or.foresthealing.R


class PreviewDialog(context: Context, path:String) : Dialog(context) {

    var mListener : PreviewDialogListener? = null
    var mPath = path

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_preview)

        Glide.with(context).load(mPath).into(prev_image_view)

        prev_recapture_btn.setOnClickListener{
            dismiss()
            mListener?.reCapture()
        }
        prev_submit_btn.setOnClickListener{
            dismiss()
            mListener?.submit()
        }
    }

    fun setPreviewDialogListener(listener:PreviewDialogListener){
        this.mListener = listener
    }
    interface PreviewDialogListener{
        fun reCapture()
        fun submit()
    }
}
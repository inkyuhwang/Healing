package kr.or.foresthealing.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.ImageButton
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_intro_confirm.*
import kotlinx.android.synthetic.main.dialog_preview.*
import kotlinx.android.synthetic.main.dialog_quiz_confirm.*
import kotlinx.android.synthetic.main.dialog_quiz_wrong_answer.*
import kotlinx.android.synthetic.main.dialog_wrong_qr.*
import kr.or.foresthealing.R
import java.io.File


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
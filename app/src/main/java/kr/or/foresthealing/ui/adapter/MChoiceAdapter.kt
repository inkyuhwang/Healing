package kr.or.foresthealing.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.or.foresthealing.R

class MChoiceAdapter() : RecyclerView.Adapter<MChoiceAdapter.ViewHolder>() {

    lateinit var mDataset:List<String>
    var selectedPos = -1

    constructor(dataset: List<String>) : this() {
        mDataset = dataset
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView = v.findViewById<View>(R.id.mchoice_row_text) as TextView
        val itemLayout: RelativeLayout = v.findViewById<View>(R.id.mchoice_row_layout) as RelativeLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mchoice_adapter_row, parent, false)
        val holder = ViewHolder(view)
        view.setOnClickListener {
            selectedPos = holder.adapterPosition
            notifyDataSetChanged()
        }
        return holder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        when(position){
            0 ->viewHolder.itemLayout.setBackgroundResource(R.drawable.selector_mchoice_btn_1)
            1 ->viewHolder.itemLayout.setBackgroundResource(R.drawable.selector_mchoice_btn_2)
            2 ->viewHolder.itemLayout.setBackgroundResource(R.drawable.selector_mchoice_btn_3)
            3 ->viewHolder.itemLayout.setBackgroundResource(R.drawable.selector_mchoice_btn_4)
        }

        viewHolder.itemView.isSelected = selectedPos == position

        val text = mDataset[position]
        viewHolder.textView.text = text
    }
    override fun getItemCount(): Int {
        return mDataset?.size
    }
}
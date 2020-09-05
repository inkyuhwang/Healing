package kr.or.foresthealing.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kr.or.foresthealing.model.StampItem
import kr.or.foresthealing.ui.view.StampItemView

class StampAdapter(context: Context, items : List<StampItem>) : BaseAdapter() {

    var mContext :Context = context
    var mItem = items

    override fun getCount(): Int {
        return mItem.size
    }

    override fun getItemId(id: Int): Long {
        return id.toLong()
    }

    override fun getItem(idx: Int): Any {
        return mItem[idx]
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val item = StampItemView(mContext)
        item.setItem(mItem[position])
        return item
    }

}
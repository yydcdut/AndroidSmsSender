package com.yydcdut.sms.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.yydcdut.sms.R

/**
 * Created by yuyidong on 2017/11/14.
 */
class ListAdapter(context: Context, list: MutableList<String>) : BaseAdapter() {
    private val mContext = context
    private val mDataList = list

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.item_ignore, null, false)
        val holder = view.tag ?: ListHolder(view)
        (holder as ListHolder).text.text = mDataList[position]
        view.tag = holder
        return view
    }

    override fun getItem(position: Int): String = mDataList[position]

    override fun getItemId(position: Int): Long = mDataList[position].hashCode() as Long

    override fun getCount(): Int = mDataList.size

}
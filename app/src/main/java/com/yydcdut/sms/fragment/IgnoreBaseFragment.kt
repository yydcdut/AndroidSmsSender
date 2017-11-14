package com.yydcdut.sms.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yydcdut.sdlv.Menu
import com.yydcdut.sdlv.MenuItem
import com.yydcdut.sdlv.SlideAndDragListView
import com.yydcdut.sms.R
import com.yydcdut.sms.list.ListAdapter
import kotlinx.android.synthetic.main.frag_ignore.*

/**
 * Created by yuyidong on 2017/11/12.
 */
open abstract class IgnoreBaseFragment : Fragment(), SlideAndDragListView.OnItemDeleteListener {
    private var mDataList: MutableList<String>? = null
    private val mMenu: Menu = Menu(true)

    init {
        val builder = MenuItem.Builder()
        builder.background = ColorDrawable(activity.resources.getColor(R.color.colorPrimary))
        builder.text = "删除"
        builder.width = activity.resources.getDimension(R.dimen.slv_item_bg_btn_width).toInt()
        mMenu.addItem(builder.build())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.frag_ignore, null, false)

    fun setData(list: MutableList<String>) {
        mDataList = list
        sdlv_ignore.setMenu(mMenu)
        sdlv_ignore.adapter = ListAdapter(activity, list)
    }
}
package com.yydcdut.sms.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.yydcdut.sdlv.Menu
import com.yydcdut.sdlv.MenuItem
import com.yydcdut.sdlv.SlideAndDragListView
import com.yydcdut.sms.R
import com.yydcdut.sms.list.ListAdapter
import kotlinx.android.synthetic.main.frag_ignore.*

/**
 * Created by yuyidong on 2017/11/12.
 */
open abstract class IgnoreBaseFragment : Fragment(), SlideAndDragListView.OnItemDeleteListener,
        View.OnClickListener, Handler.Callback {

    private var mDataList: MutableList<String>? = null
    private val mMenu = Menu(true)
    private val mHandler = Handler(Looper.getMainLooper(), this)

    private fun initMenu() {
        val builder = MenuItem.Builder()
        builder.background = ColorDrawable(activity.resources.getColor(R.color.colorPrimary))
        builder.text = "删除"
        builder.width = activity.resources.getDimension(R.dimen.slv_item_bg_btn_width).toInt()
        builder.textSize = 14
        builder.direction = MenuItem.DIRECTION_RIGHT
        mMenu.addItem(builder.build())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.frag_ignore, null, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenu()
        Thread(getDataRunnable).start()
        fab_ignore.setOnClickListener(this)
    }

    abstract fun getData(): MutableList<String>

    private fun setData(list: MutableList<String>) {
        mDataList = list
        sdlv_ignore.setMenu(mMenu)
        sdlv_ignore.setOnItemDeleteListener(this)
        sdlv_ignore.adapter = ListAdapter(activity, list)
    }

    override fun onItemDeleteAnimationFinished(view: View?, position: Int) {
        mDataList!!.removeAt(position)
        (sdlv_ignore.adapter as BaseAdapter).notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Thread(replaceRunnable).start()
    }

    abstract fun replace(list: MutableList<String>)

    override fun handleMessage(msg: Message): Boolean {
        setData(msg.obj as MutableList<String>)
        return false
    }

    private val getDataRunnable = Runnable {
        val msg = mHandler.obtainMessage()
        msg.obj = getData()
        mHandler.sendMessage(msg)
    }

    private val replaceRunnable = Runnable {
        replace(mDataList!!)
    }

    override fun onClick(v: View) {
        if (v == fab_ignore) {
//            val dialogView = LayoutInflater.from(v.context).inflate(R.layout.dialog_phone, null, false)
//            AlertDialog.Builder(activity)
//                    .setTitle("过滤数据")
//                    .setView(dialogView)
//                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
//                        Utils.savePhone(edit_phone.text.toString())
//                        dialog.dismiss()
//                    })
//                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
//                    .show()
        } else {

        }

    }

}
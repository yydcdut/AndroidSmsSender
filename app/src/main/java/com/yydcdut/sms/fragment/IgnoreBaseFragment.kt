package com.yydcdut.sms.fragment

import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.yydcdut.sdlv.Menu
import com.yydcdut.sdlv.MenuItem
import com.yydcdut.sdlv.SlideAndDragListView
import com.yydcdut.sms.R
import com.yydcdut.sms.interfaces.OnDataChangedCallback
import com.yydcdut.sms.interfaces.OnDialogClickCallback
import com.yydcdut.sms.list.ListAdapter
import kotlinx.android.synthetic.main.dialog_phone.*
import kotlinx.android.synthetic.main.dialog_phone.view.*
import kotlinx.android.synthetic.main.frag_ignore.*

/**
 * Created by yuyidong on 2017/11/12.
 */
open abstract class IgnoreBaseFragment : Fragment(), SlideAndDragListView.OnItemDeleteListener,
        SlideAndDragListView.OnMenuItemClickListener, View.OnClickListener, Handler.Callback, OnDataChangedCallback {

    private var isDestroy = false
    private var dataList: MutableList<String>? = null
    private val menu = Menu(true)
    private val handler = Handler(Looper.getMainLooper(), this)
    private var adapter: ListAdapter? = null

    private fun initMenu() {
        val builder = MenuItem.Builder()
        builder.background = ColorDrawable(activity.resources.getColor(R.color.colorPrimary))
        builder.text = "删除"
        builder.textColor = activity.resources.getColor(android.R.color.white)
        builder.width = activity.resources.getDimension(R.dimen.slv_item_bg_btn_width).toInt()
        builder.textSize = 14
        builder.direction = MenuItem.DIRECTION_RIGHT
        menu.addItem(builder.build())
        val builder2 = MenuItem.Builder()
        builder2.background = ColorDrawable(activity.resources.getColor(R.color.colorAccent))
        builder2.text = "修改"
        builder2.textColor = activity.resources.getColor(android.R.color.white)
        builder2.width = activity.resources.getDimension(R.dimen.slv_item_bg_btn_width).toInt()
        builder2.textSize = 14
        builder2.direction = MenuItem.DIRECTION_RIGHT
        menu.addItem(builder2.build())
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
        dataList = list
        sdlv_ignore.setMenu(menu)
        sdlv_ignore.setOnItemDeleteListener(this)
        sdlv_ignore.setOnMenuItemClickListener(this)
        adapter = ListAdapter(activity, list)
        sdlv_ignore.adapter = adapter
        checkDataEmpty()
    }

    override fun onMenuItemClick(v: View?, itemPosition: Int, buttonPosition: Int, direction: Int): Int =
            when (buttonPosition) {
                0 -> {
                    dataList?.removeAt(itemPosition)
                    adapter?.notifyDataSetChanged()
                    checkDataEmpty()
                    Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP
                }
                1 -> {
                    showDialog(getDialogTitle(), adapter?.let { it.getItem(itemPosition) }!!, ChangeIgnoreClick(dataList?.get(itemPosition)!!, this))
                    Menu.ITEM_NOTHING
                }
                else -> Menu.ITEM_NOTHING
            }

    override fun onItemDeleteAnimationFinished(view: View?, position: Int) {
        dataList?.removeAt(position)
        adapter?.notifyDataSetChanged()
    }

    abstract fun replace(list: MutableList<String>)

    override fun handleMessage(msg: Message): Boolean {
        if (isDestroy) return false
        when (msg.what) {
            0 -> {
                setData(msg.obj as MutableList<String>)
            }
            1 -> {
            }
        }
        return false
    }

    private val getDataRunnable = Runnable {
        val msg = handler.obtainMessage()
        msg.what = 0
        msg.obj = getData()
        handler.sendMessage(msg)
    }

    private val replaceRunnable = Runnable {
        dataList?.let { replace(it) }
    }

    abstract fun getDialogTitle(): String

    override fun onClick(v: View) {
        if (v == fab_ignore) {
            showDialog(getDialogTitle(), "", addIgnoreClick)
        }
    }

    abstract fun isRight(content: String): Boolean

    private fun showDialog(title: String, original: String, callback: OnDialogClickCallback) {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_phone, null, false)
        dialogView.edit_phone.setText(original)
        AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton("OK", { dialog, _ -> callback?.onClick(dialog, ((dialog as AlertDialog).edit_phone as EditText).text.toString()) })
                .setNegativeButton("Cancel", { dialog, _ -> dialog.dismiss() })
                .show()
    }

    private val addIgnoreClick = object : OnDialogClickCallback {
        override fun onClick(dialog: DialogInterface, content: String) {
            if (isRight(content)) {
                dataList?.add(content)
                adapter?.notifyDataSetChanged()
                checkDataEmpty()
                Thread(replaceRunnable).start()
                dialog.dismiss()
            } else {
                Toast.makeText(activity, "输入不正确", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class ChangeIgnoreClick : OnDialogClickCallback {
        private val original: String
        private val callback: OnDataChangedCallback

        constructor(original: String, callback: OnDataChangedCallback) {
            this.original = original
            this.callback = callback
        }

        override fun onClick(dialog: DialogInterface, content: String) {
            var isDismissDialog = true
            if (!TextUtils.equals(content, original)) {
                isDismissDialog = callback?.onChanged(original, content)
            }
            if (isDismissDialog) {
                dialog.dismiss()
            }
        }
    }

    override fun onChanged(original: String, newContent: String): Boolean {
        if (isRight(newContent)) {
            dataList?.remove(original)
            dataList?.add(newContent)
            adapter?.notifyDataSetChanged()
            Thread(replaceRunnable).start()
            return true
        }
        return false
    }

    private fun checkDataEmpty() {
        if (sdlv_ignore.adapter.count == 0) {
            txt_no_data.visibility = View.VISIBLE
        } else {
            txt_no_data.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroy = true
    }
}
package com.yydcdut.sms.fragment

import android.text.TextUtils
import com.yydcdut.sms.Utils
import com.yydcdut.sms.lock.Lock

/**
 * Created by yuyidong on 2017/11/12.
 */
class IgnoreTextFragment : IgnoreBaseFragment() {

    companion object {
        fun getInstance(): IgnoreTextFragment = IgnoreTextFragment()
    }

    override fun getData(): MutableList<String> = Utils.readIgnoreText(Lock.getInstance().getIgnoreTextLock())

    override fun replace(list: MutableList<String>) = Utils.saveIgnoreText(list, Lock.getInstance().getIgnoreTextLock())

    override fun getDialogTitle(): String = "忽略信息内容"

    override fun isRight(content: String): Boolean = !TextUtils.isEmpty(content)
}
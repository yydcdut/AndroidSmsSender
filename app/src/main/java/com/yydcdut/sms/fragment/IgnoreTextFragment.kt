package com.yydcdut.sms.fragment

import com.yydcdut.sms.Utils

/**
 * Created by yuyidong on 2017/11/12.
 */
class IgnoreTextFragment : IgnoreBaseFragment() {
    companion object {
        fun getInstance(): IgnoreTextFragment = IgnoreTextFragment()
    }

    override fun getData(): MutableList<String> = Utils.readIgnoreText()

    override fun replace(list: MutableList<String>) = Utils.saveIgnoreText(list)
}
package com.yydcdut.sms.fragment

import com.yydcdut.sms.Utils

/**
 * Created by yuyidong on 2017/11/12.
 */
class IgnoreNumberFragment : IgnoreBaseFragment() {

    companion object {
        fun getInstance(): IgnoreNumberFragment = IgnoreNumberFragment()
    }

    override fun getData(): MutableList<String> = Utils.readIgnoreNumber()

    override fun replace(list: MutableList<String>) = Utils.saveIgnoreNumber(list)


}
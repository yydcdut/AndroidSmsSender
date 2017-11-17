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

    override fun isRight(content: String): Boolean = isNumber(content)

    override fun getDialogTitle(): String = "忽略号码"

    private fun isNumber(content: String): Boolean {
        var isNumber = true
        for (char in content) {
            if (!Character.isDigit(char)) {
                isNumber = false
                break
            }
        }
        return isNumber
    }

}
package com.yydcdut.sms.interfaces

/**
 * Created by yuyidong on 2017/11/18.
 */
interface OnDataChangedCallback {
    fun onChanged(original: String, newContent: String): Boolean
}
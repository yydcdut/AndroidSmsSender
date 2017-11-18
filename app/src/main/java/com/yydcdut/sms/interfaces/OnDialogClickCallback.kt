package com.yydcdut.sms.interfaces

import android.content.DialogInterface

/**
 * Created by yuyidong on 2017/11/18.
 */
interface OnDialogClickCallback {
    fun onClick(dialog: DialogInterface, content: String)
}
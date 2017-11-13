package com.yydcdut.sms.observer

import com.yydcdut.sms.entity.SmsSender

/**
 * Created by yuyidong on 2017/11/12.
 */
interface SmsObservable {
    fun onReceiveSms(sms: SmsSender)
}
package com.yydcdut.sms.observer

import com.yydcdut.sms.entity.SmsSender

/**
 * Created by yuyidong on 2017/11/12.
 */
class SmsObserver private constructor() {
    private val list: MutableList<SmsObservable> = mutableListOf()

    companion object {
        fun getInstance(): SmsObserver = Holder.sInstance
    }

    private object Holder {
        val sInstance = SmsObserver()
    }

    fun register(observable: SmsObservable) {
        list.add(observable)
    }

    fun unregister(observable: SmsObservable) {
        list.remove(observable)
    }

    fun smsReceived(smsSender: SmsSender) {
        for (smsObserver in list) {
            smsObserver.onReceiveSms(smsSender)
        }
    }
}
package com.yydcdut.sms.lock

/**
 * Created by yuyidong on 2017/11/18.
 */
class Lock {
    private val lock4Number = Any()
    private val lock4Text = Any()

    companion object {
        fun getInstance(): Lock = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = Lock()
    }

    fun getIgnoreNumberLock() = lock4Number

    fun getIgnoreTextLock() = lock4Text
}
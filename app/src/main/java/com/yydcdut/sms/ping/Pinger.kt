package com.yydcdut.sms.ping

import android.os.Handler
import android.os.Message


/**
 * Created by yuyidong on 2017/11/19.
 */
class Pinger : Handler.Callback {
    private val handler = Handler(this)
    private var onMainActivityPing: OnMainActivityPing? = null
    private var onSmsServicePing: OnSmsServicePing? = null
    private var onDeamonServicePing: OnDeamonServicePing? = null

    companion object {
        val DELAY_PING = 60 * 1000L
        fun getInstance() = Holder.INSTANCE
    }

    object Holder {
        val INSTANCE = Pinger()
    }

    fun registerOnMainActivity(ping: OnMainActivityPing) {
        onMainActivityPing = ping
    }

    fun unregisterOnMainActivity() {
        onMainActivityPing = null
    }

    fun sendMainActivityPing() {
        onMainActivityPing?.onPing(System.currentTimeMillis())
    }

    fun registerOnSmsService(ping: OnSmsServicePing) {
        onSmsServicePing = ping
        handler.sendEmptyMessageDelayed(0, DELAY_PING)
    }

    fun unregisterOnSmsService() {
        onSmsServicePing = null
        onDeamonServicePing = null
        handler.removeCallbacksAndMessages(null)
    }

    fun registerOnDeamonService(ping: OnDeamonServicePing) {
        onDeamonServicePing = ping
    }

    fun unregisterOnDeamonService() {
        onDeamonServicePing = null
    }

    override fun handleMessage(msg: Message?): Boolean {
        onSmsServicePing?.onPing(System.currentTimeMillis())
        onDeamonServicePing?.onPing(System.currentTimeMillis())
        onSmsServicePing?.let { handler.sendEmptyMessageDelayed(0, DELAY_PING) }
        return true
    }
}
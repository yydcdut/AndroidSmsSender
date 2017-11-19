package com.yydcdut.sms.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.yydcdut.sms.IAliveInterface
import com.yydcdut.sms.ping.OnDeamonServicePing
import com.yydcdut.sms.ping.Pinger

/**
 * Created by yuyidong on 2017/11/12.
 */
class DeamonService : Service(), OnDeamonServicePing {

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        Pinger.getInstance().registerOnDeamonService(this)
        if (!ServiceManager.getInstance().isSmsAlive()) {
            ServiceManager.getInstance().bindSmsService()
            ServiceManager.getInstance().startSmsService()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = Notification()
        notification.flags = Notification.FLAG_ONGOING_EVENT
        notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
        notification.flags = notification.flags or Notification.FLAG_FOREGROUND_SERVICE
        startForeground(2, notification)
        return START_STICKY
    }

    private val binder: Binder = object : IAliveInterface.Stub() {
        override fun isAlive(): Boolean = true
    }

    override fun onPing(time: Long) {
        if (!ServiceManager.getInstance().isSmsAlive()) {
            ServiceManager.getInstance().bindSmsService()
            ServiceManager.getInstance().startSmsService()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Pinger.getInstance().unregisterOnDeamonService()
    }
}
package com.yydcdut.sms.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message

/**
 * Created by yuyidong on 2017/11/12.
 */
class DeamonService : Service(), Handler.Callback {
    private val handler = Handler(Looper.getMainLooper(), this)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = Notification()
        notification.flags = Notification.FLAG_ONGOING_EVENT
        notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
        notification.flags = notification.flags or Notification.FLAG_FOREGROUND_SERVICE
        startForeground(2, notification)
        return START_STICKY
    }

    override fun handleMessage(msg: Message?): Boolean {
        return true
    }
}
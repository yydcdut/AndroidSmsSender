package com.yydcdut.sms.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.SmsManager
import com.yydcdut.sms.Utils
import com.yydcdut.sms.entity.SmsSender
import com.yydcdut.sms.observer.SmsObservable
import com.yydcdut.sms.observer.SmsObserver


/**
 * Created by yuyidong on 2017/11/12.
 */
class SmsService : Service() {

    private val SENT_SMS_ACTION = "SENT_SMS_ACTION"
    private val DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION"

    private var sendPiBroadcastReceiver: BroadcastReceiver? = null
    private var deliverPIBroadcastReceiver: BroadcastReceiver? = null

    private var sentPI: PendingIntent? = null
    private var deliverPI: PendingIntent? = null
    private var sendSms: SendSms? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (sendPiBroadcastReceiver == null) {
            sentPI = PendingIntent.getBroadcast(this, 0, Intent(SENT_SMS_ACTION), 0)
            registerReceiver(sendPiBroadcastReceiver, IntentFilter(SENT_SMS_ACTION))
        }
        if (deliverPIBroadcastReceiver == null) {
            deliverPI = PendingIntent.getBroadcast(this, 0, Intent(DELIVERED_SMS_ACTION), 0)
            registerReceiver(deliverPIBroadcastReceiver, IntentFilter(DELIVERED_SMS_ACTION))
        }
        if (sendSms == null) {
            sendSms = SendSms(sentPI!!, deliverPI!!)
            SmsObserver.getInstance().register(sendSms!!)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        upperPriority()
        return START_STICKY
    }

    private fun upperPriority() {
        val notification = Notification()
        notification.flags = Notification.FLAG_ONGOING_EVENT
        notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
        notification.flags = notification.flags or Notification.FLAG_FOREGROUND_SERVICE
        startForeground(1, notification)
    }

    class SendSms(private val sentPI: PendingIntent, private val deliverPI: PendingIntent) : SmsObservable {

        override fun onReceiveSms(smsSender: SmsSender) {
            val sms = SmsManager.getDefault()
            val divideContents = sms.divideMessage(smsSender.address + "\n" + smsSender.content)
            val phone = Utils.getPhone()
            for (text in divideContents) {
                sms.sendTextMessage(phone, null, text, sentPI, deliverPI)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sendSms?.let { SmsObserver.getInstance().unregister(it) }
    }
}
package com.yydcdut.sms.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.telephony.SmsMessage
import android.text.TextUtils
import com.yydcdut.sms.Utils
import com.yydcdut.sms.entity.SmsSender
import com.yydcdut.sms.lock.Lock
import com.yydcdut.sms.observer.SmsObserver

/**
 * Created by yuyidong on 2017/11/12.
 */
class SmsReceiver : BroadcastReceiver(), Runnable, Handler.Callback {

    private val handler: Handler = Handler(Looper.getMainLooper(), this)
    private var smsSender: SmsSender? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || intent.extras == null) {
            return
        }
        val bundle = intent.extras
        val pdusArray = bundle.get("pdus") as Array<Any>
        pdusArray
                .map { SmsMessage.createFromPdu(it as ByteArray) }
                .map { SmsSender(it.originatingAddress, it.displayMessageBody) }
                .map { smsSender = it }
                .forEach { Thread(this).start() }
    }

    override fun handleMessage(msg: Message): Boolean {
        SmsObserver.getInstance().smsReceived(msg.obj as SmsSender)
        return false
    }

    override fun run() {
        if (smsSender == null) {
            return
        }
        val numberList = Utils.readIgnoreNumber(Lock.getInstance().getIgnoreNumberLock())
        var ignore: Boolean = numberList.any { TextUtils.equals(it, smsSender!!.address) }
        if (ignore) {
            return
        }
        val textList = Utils.readIgnoreText(Lock.getInstance().getIgnoreTextLock())
        ignore = textList.any { smsSender!!.content.contains(it, true) }
        if (ignore) {
            return
        }
        val msg = handler.obtainMessage()
        msg.obj = smsSender
        handler.sendMessage(msg)
    }

}
package com.yydcdut.sms.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.yydcdut.sms.App
import com.yydcdut.sms.IAliveInterface

/**
 * Created by yuyidong on 2017/11/19.
 */
class ServiceManager {
    private var deamonBinder: IAliveInterface? = null
    private var smsBinder: IAliveInterface? = null


    object Holder {
        val INSTANCE = ServiceManager()
    }

    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    fun startSmsService() {
        App.instance().startService(Intent(App.instance(), SmsService::class.java))
    }

    fun bindSmsService() {
        App.instance().bindService(Intent(App.instance(), SmsService::class.java), smsServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private val smsServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            smsBinder = IAliveInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bindSmsService()
        }
    }

    fun isSmsAlive(): Boolean {
        if (smsBinder == null) {
            return false
        }
        return smsBinder!!.isAlive
    }

    fun startDeamonService() {
        App.instance().startService(Intent(App.instance(), DeamonService::class.java))
    }

    fun bindDeamonService() {
        App.instance().bindService(Intent(App.instance(), DeamonService::class.java), deamonServiceConnection, Context.BIND_AUTO_CREATE)
    }

    fun isDeamonAlive(): Boolean {
        if (deamonBinder == null) {
            return false
        }
        return deamonBinder!!.isAlive
    }

    private val deamonServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            deamonBinder = IAliveInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bindDeamonService()
        }
    }


}
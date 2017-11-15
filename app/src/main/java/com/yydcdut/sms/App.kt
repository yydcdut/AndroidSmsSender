package com.yydcdut.sms

import android.app.Application

/**
 * Created by yuyidong on 2017/11/15.
 */
class App : Application() {
    companion object {
        private var instance: Application? = null
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}
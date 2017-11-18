package com.yydcdut.sms.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yydcdut.sms.R
import com.yydcdut.sms.entity.SmsSender
import com.yydcdut.sms.observer.SmsObservable
import com.yydcdut.sms.observer.SmsObserver
import com.yydcdut.sms.service.SmsService
import kotlinx.android.synthetic.main.frag_main.*

/**
 * Created by yuyidong on 2017/11/12.
 */
open class MainFragment : Fragment(), View.OnClickListener, SmsObservable {
    companion object {
        fun getInstance(): MainFragment = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.frag_main, null, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_main.setOnClickListener(this)
        SmsObserver.getInstance().register(this)
    }

    override fun onClick(v: View) {
        activity.startService(Intent(activity, SmsService::class.java))
    }

    override fun onReceiveSms(sms: SmsSender) {
    }

    override fun onDestroy() {
        super.onDestroy()
        SmsObserver.getInstance().unregister(this)
    }
}
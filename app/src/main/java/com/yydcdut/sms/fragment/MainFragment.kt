package com.yydcdut.sms.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yydcdut.sms.R
import com.yydcdut.sms.service.ServiceManager
import kotlinx.android.synthetic.main.frag_main.*
import java.text.SimpleDateFormat

/**
 * Created by yuyidong on 2017/11/12.
 */
open class MainFragment : Fragment(), View.OnClickListener {
    private var startTime: Long? = null

    companion object {
        fun getInstance(time: Long): MainFragment {
            val f = MainFragment()
            val bundle = Bundle()
            bundle.putLong("time", time)
            f.arguments = bundle
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startTime = arguments.getLong("time", System.currentTimeMillis())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.frag_main, null, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_main.setOnClickListener(this)
        ServiceManager.getInstance().startSmsService()
    }

    override fun onClick(v: View) {
        ServiceManager.getInstance().startSmsService()
    }

    override fun onResume() {
        super.onResume()
        txt_main_start.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
    }

}
package com.yydcdut.sms

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.yydcdut.sms.fragment.IgnoreNumberFragment
import com.yydcdut.sms.fragment.IgnoreTextFragment
import com.yydcdut.sms.fragment.MainFragment
import com.yydcdut.sms.ping.OnMainActivityPing
import com.yydcdut.sms.ping.Pinger
import com.yydcdut.sms.service.ServiceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_phone.*
import kotlinx.android.synthetic.main.dialog_phone.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, OnMainActivityPing, Handler.Callback {

    private var currentFragment: Fragment? = null
    private var startTime: Long? = null

    private var phoneTextView: TextView? = null

    private var lastPingTime: Long = 0L
    private val handler = Handler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        startTime = System.currentTimeMillis()

        currentFragment = MainFragment.getInstance(startTime!!)
        replaceFragment(currentFragment as MainFragment)

        phoneTextView = nav_view.getHeaderView(0).txt_nav_title
        nav_view.getHeaderView(0).img_nav.setOnClickListener(this)
        phoneTextView!!.setOnClickListener(this)
        phoneTextView!!.text = Utils.getPhone()

        Pinger.getInstance().registerOnMainActivity(this)
        handler.sendEmptyMessageDelayed(0, 1000)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) drawer_layout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> true
        else -> super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_main -> {
                if (currentFragment !is MainFragment) {
                    currentFragment = MainFragment.getInstance(startTime!!)
                    replaceFragment(currentFragment as MainFragment)
                }
            }
            R.id.nav_ignore_number -> {
                if (currentFragment !is IgnoreNumberFragment) {
                    currentFragment = IgnoreNumberFragment.getInstance()
                    replaceFragment(currentFragment as IgnoreNumberFragment)
                }
            }
            R.id.nav_ignore_text -> {
                if (currentFragment !is IgnoreTextFragment) {
                    currentFragment = IgnoreTextFragment.getInstance()
                    replaceFragment(currentFragment as IgnoreTextFragment)
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        var ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.layout_fragment, fragment)
        ft.commit()
    }

    override fun onClick(v: View) {
        val dialogView = LayoutInflater.from(v.context).inflate(R.layout.dialog_phone, null, false)
        dialogView.edit_dialog.setText(Utils.getPhone())
        AlertDialog.Builder(this)
                .setTitle("电话")
                .setView(dialogView)
                .setPositiveButton("OK", { dialog, _ ->
                    val phone = ((dialog as AlertDialog).edit_dialog as EditText).text.toString()
                    if (!TextUtils.isEmpty(phone) && Utils.isPhone(phone)) {
                        Utils.savePhone(phone)
                        phoneTextView!!.text = phone
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "输入有误", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("Cancel", { dialog, _ -> dialog.dismiss() })
                .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Pinger.getInstance().unregisterOnMainActivity()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onPing(time: Long) {
        lastPingTime = time
        handler.removeCallbacksAndMessages(null)
        handler.sendEmptyMessageDelayed(0, Pinger.DELAY_PING)
    }

    override fun handleMessage(msg: Message?): Boolean {
        if (lastPingTime == 0L || (System.currentTimeMillis() - lastPingTime!! > 2 * Pinger.DELAY_PING)) {
            ServiceManager.getInstance().startSmsService()
        }
        handler.removeCallbacksAndMessages(null)
        handler.sendEmptyMessageDelayed(0, Pinger.DELAY_PING)
        return true
    }
}

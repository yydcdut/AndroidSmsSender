package com.yydcdut.sms

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.yydcdut.sms.fragment.IgnoreNumberFragment
import com.yydcdut.sms.fragment.IgnoreTextFragment
import com.yydcdut.sms.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_phone.*
import kotlinx.android.synthetic.main.dialog_phone.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    private var currentFragment: Fragment? = null

    private var phoneTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        currentFragment = MainFragment.getInstance()
        replaceFragment(currentFragment as MainFragment)

        phoneTextView = nav_view.getHeaderView(0).txt_nav_title
        phoneTextView!!.setOnClickListener(this)
        phoneTextView!!.text = Utils.getPhone()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_main -> {
                if (currentFragment !is MainFragment) {
                    currentFragment = MainFragment.getInstance()
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
//            R.id.nav_manage -> {
//
//            }
//            R.id.nav_share -> {
//
//            }
//            R.id.nav_send -> {
//
//            }
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
        dialogView.edit_phone.setOnClickListener(this)
        AlertDialog.Builder(this)
                .setTitle("电话")
                .setView(dialogView)
                .setPositiveButton("OK", { dialog, _ ->
                    val phone = ((dialog as AlertDialog).edit_phone as EditText).text.toString()
                    Utils.savePhone(phone)
                    phoneTextView!!.text = phone
                    dialog.dismiss()
                })
                .setNegativeButton("Cancel", { dialog, _ -> dialog.dismiss() })
                .show()
    }
}

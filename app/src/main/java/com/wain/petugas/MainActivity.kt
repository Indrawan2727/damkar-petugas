package com.wain.petugas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wain.petugas.activity.LoginActivity
import com.wain.petugas.halper.SharedPref
import com.wain.petugas.petugas.fragments.HomeFragmentp
import com.wain.petugas.petugas.fragments.InfoFragmentp
import com.wain.petugas.petugas.fragments.ProfileFragmentp

class MainActivity : AppCompatActivity() {

    private val fragmentHomep: Fragment = HomeFragmentp()
    private val fragmentInfop: Fragment = InfoFragmentp()
    private var fragmentProfilep: Fragment = ProfileFragmentp()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentHomep


    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView

    private var statusLogin = false
    private lateinit var s:SharedPref

    private var dariDetail : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        s = SharedPref(this)

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessage, IntentFilter("event:laporans"))

        if (s.getStatusLogin()) {
            setUpBottomNav()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    val mMessage: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            dariDetail = true
        }
    }

    fun setUpBottomNav() {
        fm.beginTransaction().add(R.id.container, fragmentHomep).show(fragmentHomep).commit()
        fm.beginTransaction().add(R.id.container,  fragmentInfop).hide( fragmentInfop).commit()
        fm.beginTransaction().add(R.id.container, fragmentProfilep).hide(fragmentProfilep).commit()

        bottomNavigationView = findViewById(R.id.bottom_navigation_petugas)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.homeFragmentP -> {
                    callFargment(0, fragmentHomep)
                }
                R.id.infoFragmentP -> {
                    callFargment(1, fragmentInfop)
                }
                R.id.profileFragmentP -> {
                    callFargment(2, fragmentProfilep)
                }
            }

            false
        }
    }
    fun callFargment(int: Int, fragment: Fragment) {
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }

    override fun onResume() {
        if (dariDetail) {
            dariDetail = false
            callFargment(0, fragmentHomep)
        }
        super.onResume()
    }

}
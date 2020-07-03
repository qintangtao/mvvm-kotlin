package com.qin.wan.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.ViewPropertyAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.animation.AnimationUtils
import com.qin.wan.R
import com.qin.wan.ui.main.dashboard.DashboardFragment
import com.qin.wan.ui.main.home.HomeFragment
import com.qin.wan.ui.main.notifications.NotificationsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fragments: Map<Int, Fragment>
    private var bottomNavigationViewAnimtor: ViewPropertyAnimator? = null
    private var currentBottomNavagtionState = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragments = mapOf(
            R.id.navigation_home to HomeFragment.newInstance(),
            R.id.navigation_dashboard to DashboardFragment.newInstance(),
            R.id.navigation_notifications to NotificationsFragment.newInstance()
        )

        showFragment(nav_view.selectedItemId)

        nav_view.run {
            setOnNavigationItemSelectedListener{
                showFragment(it.itemId)
                true
            }
        }
    }

    fun showFragment(menuItemId: Int) {
        val currentFragment = supportFragmentManager.fragments.find {
            it.isVisible && it in fragments.values
        }
        val targetFragment = fragments.entries.find { it.key == menuItemId }?.value
        supportFragmentManager.beginTransaction().apply {
            currentFragment?.let { if (it.isVisible) hide(it) }
            targetFragment?.let {
                if (it.isAdded) show(it) else add(R.id.container, it)
            }
        }.commit()
    }

    fun animateBottomNavigationView(show: Boolean) {
        if (currentBottomNavagtionState == show) {
            return
        }
        if (bottomNavigationViewAnimtor != null) {
            bottomNavigationViewAnimtor?.cancel()
            nav_view.clearAnimation()
        }
        currentBottomNavagtionState = show
        val targetY = if (show) 0F else nav_view.measuredHeight.toFloat()
        val duration = if (show) 225L else 175L
        bottomNavigationViewAnimtor = nav_view.animate()
            .translationY(targetY)
            .setDuration(duration)
            .setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    bottomNavigationViewAnimtor = null
                }
            })
    }
}

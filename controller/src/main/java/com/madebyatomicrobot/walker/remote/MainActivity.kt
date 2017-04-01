package com.madebyatomicrobot.walker.remote

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tabPagerAdapter: SectionsPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById(R.id.tabs) as TabLayout
        viewPager = findViewById(R.id.container) as ViewPager
        tabPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        viewPager.adapter = tabPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        for (i in 0..tabPagerAdapter.count - 1) {
            var icon = ContextCompat.getDrawable(this, tabPagerAdapter.getPageIcon(i))
            icon = DrawableCompat.wrap(icon)
            DrawableCompat.setTint(icon, ContextCompat.getColor(this, R.color.white))
            tabLayout.getTabAt(i)!!.icon = icon
        }
    }

    private inner class SectionsPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence {
            when (position) {
                0 -> return getString(R.string.commands)
                1 -> return getString(R.string.servos)
                2 -> return getString(R.string.config)
                else -> throw IllegalStateException()
            }
        }

        @DrawableRes fun getPageIcon(position: Int): Int {
            when (position) {
                0 -> return R.drawable.directions
                1 -> return R.drawable.engine
                2 -> return R.drawable.settings
                else -> throw IllegalStateException()
            }
        }

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return CommandsFragment.newInstance()
                1 -> return ServosFragment.newInstance()
                2 -> return ActionsFragment.newInstance()
                else -> throw IllegalStateException()
            }
        }
    }
}

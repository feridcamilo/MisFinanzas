package com.android.misfinanzas.utils

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

fun TabLayout.setupWithViewPager2(viewPager2: ViewPager2) {
    TabLayoutMediator(this, viewPager2) { tab, position ->
        tab.text = (viewPager2.adapter as TabsAdapter).getPageTitle(position)
    }.attach()
}

fun TabLayout.onTabSelected(action: (ab: TabLayout.Tab) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            action.invoke(tab ?: return)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    })
}
